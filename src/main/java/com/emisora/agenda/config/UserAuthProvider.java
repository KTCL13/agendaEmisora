package com.emisora.agenda.config;

import java.util.Base64;
import java.util.Collections; 
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority; 
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException; 
import com.auth0.jwt.interfaces.DecodedJWT;
import com.emisora.agenda.dto.UserDto;
import com.emisora.agenda.service.UserService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; 

@RequiredArgsConstructor
@Component
@Slf4j // Para logging
public class UserAuthProvider {

    @Value("${security.jwt.secret-key:secret-value}")
    private String secretKey;

    private final  UserService userService;

    private Algorithm algorithm;
    private JWTVerifier verifier;

    private static final long TOKEN_VALIDITY_MS = 3_600_000L; // 1 hora

    @PostConstruct
    protected void init() {
        String processedSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.algorithm = Algorithm.HMAC256(processedSecretKey);
        this.verifier = JWT.require(this.algorithm).build();
        log.info("UserAuthProvider inicializado y JWT secret key procesada.");
    }

    /**
     * Crea un token JWT para el usuario proporcionado, incluyendo una lista de roles.
     * @param user DTO del usuario que contiene login y lista de roles.
     * @return El token JWT generado como String.
     */
    public String createToken(UserDto user) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + TOKEN_VALIDITY_MS);

        // Convertir la lista de Enums Role a una lista de Strings con el prefijo "ROLE_"
        List<String> roleClaims = Collections.emptyList();
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            roleClaims = user.getRoles().stream()
                    .map(roleEnum -> "ROLE_" + roleEnum.name()) // ej: ROLE_ADMIN, ROLE_USER
                    .collect(Collectors.toList());
        } else {
            // Opcional: asignar un rol por defecto si la lista está vacía
            // roleClaims = List.of("ROLE_USER");
            log.warn("Usuario {} no tiene roles asignados al crear el token. Se asignará una lista vacía de roles en el token.", user.getUsername());
        }

        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withClaim("roles", roleClaims) // Almacena la lista de roles
                .sign(this.algorithm);
    }

    /**
     * Valida un token JWT y, si es válido, construye un objeto Authentication de Spring Security.
     * Los roles/autoridades se obtienen del UserDto cargado desde la base de datos.
     * @param token El token JWT a validar.
     * @return Un objeto Authentication si el token es válido y el usuario existe y está activo.
     * @throws JWTVerificationException Si el token es inválido.
     */
    public Authentication validateTokenAndGetAuthentication(String token) throws JWTVerificationException {
        DecodedJWT decodedJWT = verifier.verify(token);
        UserDto user = userService.findUserByUsername(decodedJWT.getSubject());

        if (user == null) {
            log.warn("Usuario no encontrado para el token: {}", decodedJWT.getSubject());
            throw new JWTVerificationException("Usuario asociado al token no encontrado.");
        }

        // Verificar si el usuario está activo (asumiendo que UserDto tiene un método isActive())
        if (!user.isActive()) {
             log.warn("Usuario inactivo intentando autenticarse con token: {}", user.getUsername());
             throw new JWTVerificationException("La cuenta del usuario está inactiva.");
        }

        List<GrantedAuthority> authorities = Collections.emptyList();
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            authorities = user.getRoles().stream()
                    .map(roleEnum -> new SimpleGrantedAuthority("ROLE_" + roleEnum.name()))
                    .collect(Collectors.toList());
        } else {
            log.warn("Usuario {} recuperado de la BD no tiene roles. Se autenticará sin autoridades específicas.", user.getUsername());
        }

        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }

    public String extractUsernameFromToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getSubject();
        } catch (Exception e) {
            log.warn("No se pudo decodificar el token para extraer el nombre de usuario (sin verificación): {}", e.getMessage());
            return null;
        }
    }
}