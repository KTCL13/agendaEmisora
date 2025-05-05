package com.emisora.agenda.controller;

import com.emisora.agenda.dto.LoginRequestDTO;
import com.emisora.agenda.dto.PersonaDTO;
import com.emisora.agenda.service.AuthService;
import com.emisora.agenda.dto.AuthResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid PersonaDTO personaDTO) {
        AuthResponseDTO response = authService.register(personaDTO);
        return ResponseEntity.ok(response);
    }
}
