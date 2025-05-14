package com.emisora.agenda.controller;

import com.emisora.agenda.dto.PersonaDTO;
import com.emisora.agenda.exceptions.ResourceNotFoundException;
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

    @PostMapping ("/crearPersona")
    public ResponseEntity<PersonaDTO> crearPersona(@RequestBody PersonaDTO personaDTO) {
        PersonaDTO nuevaPersona = personaService.crearPersona(personaDTO);
        return new ResponseEntity<>(nuevaPersona, HttpStatus.CREATED);
    }

    @GetMapping ("/allPersonas")
    public ResponseEntity<List<PersonaDTO>> getAllPersonas() {
        List<PersonaDTO> personas = personaService.getAllPersonas();
        return new ResponseEntity<>(personas, HttpStatus.OK);
    }

    @GetMapping ("/getPersona/{id}")
    public ResponseEntity<PersonaDTO> getPersona(@PathVariable Long id) {
        PersonaDTO persona = personaService.getPersona(id);
        return new ResponseEntity<>(persona, HttpStatus.OK);
    }

    @PutMapping ("/updatePersona/{id}")
    public ResponseEntity<PersonaDTO> updatePersona(@PathVariable Long id, @RequestBody @Valid PersonaDTO personaDTO) {
        try {
            PersonaDTO updatedPersona = personaService.actualizarPersona(id, personaDTO);
            return new ResponseEntity<>(updatedPersona, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) { // Suponiendo que tienes esta excepción personalizada
            // Si el servicio lanza esta excepción cuando la persona no se encuentra
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            // Para otros errores inesperados
            // Considera loggear el error
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping ("/deletePersona/{id}")
    public ResponseEntity<Void> deletePersona(@PathVariable Long id) {
        try {
            personaService.deletePersona(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException ex) { // Suponiendo que tienes esta excepción personalizada
            // Si el servicio lanza esta excepción cuando la persona no se encuentra
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            // Para otros errores inesperados
            // Considera loggear el error
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
