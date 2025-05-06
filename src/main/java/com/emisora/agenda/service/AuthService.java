package com.emisora.agenda.service;

//import com.emisora.agenda.config.JwtUtil;
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
    private PersonaRepository personaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponseDTO login(LoginRequestDTO request) {
        Persona persona = personaRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), persona.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return new AuthResponseDTO("Login exitoso", persona.getUsername(), "ROLE_USER");
    }

    public AuthResponseDTO register(PersonaDTO dto) {
        if (personaRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Nombre de usuario ya está en uso");
        }

        Persona persona = new Persona();
        persona.setNombre(dto.getNombre());
        persona.setCorreo(dto.getCorreo());
        persona.setUsername(dto.getUsername());
        persona.setPassword(dto.getPassword()); // Guardar sin cifrar

        List<String> roles = dto.getRoles().stream()
                .map(RolDTO::getTipo)
                .collect(Collectors.toList());

        persona.setRoles(roles);
        Persona savedPersona = personaRepository.save(persona);

        return new AuthResponseDTO("Registro exitoso", savedPersona.getUsername(), "ROLE_USER");
    }
}