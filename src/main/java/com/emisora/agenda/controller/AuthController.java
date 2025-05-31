package com.emisora.agenda.controller;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.emisora.agenda.config.UserAuthProvider;
import com.emisora.agenda.dto.SignUpDto;
import com.emisora.agenda.dto.UserDto;
import com.emisora.agenda.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// DTO para la petición de login
record AuthenticationRequest(String usernameOrEmail, String password) {}

// DTO para la respuesta de login
record AuthenticationResponse(String jwt, String username, List<String> roles) {}

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j // Para logging
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserAuthProvider userAuthProvider;
    private final UserService userService; // Para obtener el UserDto

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody AuthenticationRequest authenticationRequest) {

        if (authenticationRequest.usernameOrEmail() == null || authenticationRequest.usernameOrEmail().trim().isEmpty() ||
            authenticationRequest.password() == null || authenticationRequest.password().isEmpty()) {
            log.warn("Intento de login con campos vacíos: {}", authenticationRequest.usernameOrEmail());
            return ResponseEntity.badRequest().body("Nombre de usuario/email y contraseña son obligatorios.");
        }

        Authentication authentication;
        try {
            // 1. Autenticar al usuario usando Spring Security AuthenticationManager
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.usernameOrEmail(),
                            authenticationRequest.password()
                    )
            );
            // Si la autenticación es exitosa, 'authentication.isAuthenticated()' será true.
            // El principal en 'authentication' será el UserDetails cargado por CustomUserDetailsService.
            log.info("Usuario autenticado exitosamente: {}", authentication.getName());

        } catch (BadCredentialsException e) {
            log.warn("Intento de login fallido (credenciales inválidas) para: {}", authenticationRequest.usernameOrEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nombre de usuario o contraseña incorrectos.");
        }  catch (AuthenticationException e) {
            log.error("Error de autenticación inesperado para {}: {}", authenticationRequest.usernameOrEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error de autenticación: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error interno del servidor durante el login para {}: {}", authenticationRequest.usernameOrEmail(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error inesperado. Intente más tarde.");
        }

        String login = authentication.getName();
        UserDto userDto = userService.findUserByUsername(login); 

        if (userDto == null) {
            log.error("Usuario autenticado '{}' no encontrado como UserDto. Inconsistencia de datos.", login);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al recuperar los detalles del usuario.");
        }


        if (!userDto.isActive()) { 
            log.warn("Usuario '{}' autenticado pero marcado como inactivo en UserDto.", login);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Su cuenta está inactiva o bloqueada. Por favor, contacte al administrador.");
        }

   
        final String jwt = userAuthProvider.createToken(userDto);


        List<String> roles = userDto.getRoles().stream()
                                .map(roleEnum -> "ROLE_" + roleEnum.name()) 
                                .collect(Collectors.toList());

        log.info("Token JWT generado para el usuario: {}", userDto.getUsername());
        return ResponseEntity.ok(new AuthenticationResponse(jwt, userDto.getUsername(), roles));
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpDto signUpDto) {
        try {
            UserDto registeredUser = userService.registerNewUserAccount(signUpDto);
            log.info("Usuario registrado exitosamente: {}", registeredUser.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (RuntimeException e) { 
            log.warn("Error durante el registro del usuario {}: {}", signUpDto.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado durante el registro del usuario {}: {}", signUpDto.getUsername(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error inesperado durante el registro.");
        }
    }
}