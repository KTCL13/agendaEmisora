package com.emisora.agenda.controller;

import com.emisora.agenda.dto.PersonaDTO;
import com.emisora.agenda.service.PersonaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    // Crear nueva persona
    @PostMapping
    public ResponseEntity<PersonaDTO> crearPersona(@RequestBody @Valid PersonaDTO personaDTO) {
        PersonaDTO nuevaPersona = personaService.crearPersona(personaDTO);
        return new ResponseEntity<>(nuevaPersona, HttpStatus.CREATED);
    }

    // Obtener todas las personas
    @GetMapping
    public ResponseEntity<List<PersonaDTO>> obtenerTodasLasPersonas() {
        List<PersonaDTO> personas = personaService.obtenerTodasLasPersonas();
        return new ResponseEntity<>(personas, HttpStatus.OK);
    }
}
