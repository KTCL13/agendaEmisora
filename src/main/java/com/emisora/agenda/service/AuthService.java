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
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Persona persona = personaRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Solo usamos ROLE_USER ahora
        String jwtRole = "ROLE_USER";

        String token = jwtUtil.generateToken(persona.getUsername(), jwtRole);

        return new AuthResponseDTO(token, persona.getUsername(), jwtRole);
    }

    public AuthResponseDTO register(PersonaDTO dto) {
        if (personaRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Nombre de usuario ya está en uso");
        }

        Persona persona = new Persona();
        persona.setNombre(dto.getNombre());
        persona.setCorreo(dto.getCorreo());
        persona.setUsername(dto.getUsername());
        persona.setPassword(passwordEncoder.encode(dto.getPassword()));

        List<String> roles = dto.getRoles().stream()
                .map(RolDTO::getTipo)
                .collect(Collectors.toList());

        persona.setRoles(roles);
        Persona savedPersona = personaRepository.save(persona);

        // Siempre será ROLE_USER
        String token = jwtUtil.generateToken(savedPersona.getUsername(), "ROLE_USER");

        return new AuthResponseDTO(token, savedPersona.getUsername(), "ROLE_USER");
    }
}