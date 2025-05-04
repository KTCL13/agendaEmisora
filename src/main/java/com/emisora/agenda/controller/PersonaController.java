package com.emisora.agenda.controller;

import com.emisora.agenda.dto.PersonaDTO;
import com.emisora.agenda.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    @PostMapping
    public PersonaDTO crearPersona(@RequestBody PersonaDTO personaDTO) {
        return personaService.crearPersona(personaDTO);
    }

    @GetMapping
    public List<PersonaDTO> obtenerTodasLasPersonas() {
        return personaService.obtenerTodasLasPersonas();
    }
}
