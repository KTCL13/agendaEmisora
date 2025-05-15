package com.emisora.agenda.controller;

import com.emisora.agenda.dto.PersonaDTO;

import com.emisora.agenda.service.PersonaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

}
