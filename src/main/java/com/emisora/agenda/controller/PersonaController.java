package com.emisora.agenda.controller;

import com.emisora.agenda.dto.PersonaDTO;

import com.emisora.agenda.service.PersonaService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    @PostMapping ("/crearPersona")
     public ResponseEntity<PersonaDTO> crearPersona(
            @Valid @RequestBody PersonaDTO personaRequestDTO) { 
        PersonaDTO personaCreada = personaService.crearPersonaConRoles(personaRequestDTO);
        return new ResponseEntity<>(personaCreada, HttpStatus.CREATED);
    }

    @GetMapping("/allpersonas")
    public ResponseEntity<List<PersonaDTO>> obtenerTodasLasPersonas() {
        List<PersonaDTO> personas = personaService.obtenerTodasLasPersonas();
        return new ResponseEntity<>(personas, HttpStatus.OK);
    }

    @PutMapping("/actualizarPersona/{id}")
    public ResponseEntity<PersonaDTO> actualizarPersona(
            @PathVariable Long id,
            @Valid @RequestBody PersonaDTO personaRequestDTO) {
        PersonaDTO personaActualizada = personaService.actualizarPersona(id, personaRequestDTO);
        return new ResponseEntity<>(personaActualizada, HttpStatus.OK);
    }

    @DeleteMapping("/eliminarPersona/{id}")
    public ResponseEntity<Void> eliminarPersona(@PathVariable Long id) {
        personaService.eliminarPersona(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
