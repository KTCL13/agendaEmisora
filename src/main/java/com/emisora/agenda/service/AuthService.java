package com.emisora.agenda.service;

import com.emisora.agenda.config.JwtUtil;
import com.emisora.agenda.dto.AuthResponseDTO;
import com.emisora.agenda.dto.LoginRequestDTO;
import com.emisora.agenda.dto.PersonaDTO;
import com.emisora.agenda.dto.RolDTO;
import com.emisora.agenda.model.Persona;
import com.emisora.agenda.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponseDTO login(LoginRequestDTO request) {

        //1. Autenticar al usuario usando el AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        //2. Establecer autenticación en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //3. Buscar persona por username
        Persona persona = personaRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        //4. Determinar rol para el token: ROLE_ADMIN si contiene "ROLE_ADMIN"
        String jwtRole = persona.getRoles().contains("ROLE_ADMIN") ? "ROLE_ADMIN" : "ROLE_USER";

        //5. Generar token JWT
        String token = jwtUtil.generateToken(persona.getUsername(), jwtRole);

        //6. Devolver respuesta con token
        return new AuthResponseDTO(token, persona.getUsername(), jwtRole);
    }

    public AuthResponseDTO register(PersonaDTO dto) {
        // Verificar si ya existe el username
        if (personaRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Nombre de usuario ya está en uso");
        }

        // Convertir DTO a Entidad
        Persona persona = new Persona();
        persona.setNombre(dto.getNombre());
        persona.setCorreo(dto.getCorreo());
        persona.setUsername(dto.getUsername());
        persona.setPassword(passwordEncoder.encode(dto.getPassword()));

        List<String> roles = dto.getRoles().stream()
                .map(RolDTO::getTipo)
                .collect(Collectors.toList());

        persona.setRoles(roles);

        // Guardar en la base de datos
        Persona savedPersona = personaRepository.save(persona);

        // Generar token JWT
        String jwtRole = savedPersona.getRoles().contains("ROLE_ADMIN") ? "ROLE_ADMIN" : "ROLE_USER";
        String token = jwtUtil.generateToken(savedPersona.getUsername(), jwtRole);

        return new AuthResponseDTO(token, savedPersona.getUsername(), jwtRole);
    }
}