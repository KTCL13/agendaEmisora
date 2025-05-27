package com.emisora.agenda.controller;

import com.emisora.agenda.dto.PersonaDTO;

import com.emisora.agenda.service.PersonaService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    @Autowired
    private PersonaService personaService;


    

    @GetMapping
    public ResponseEntity<Page<PersonaDTO>> obtenerPersonasPorEstado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombresPersona,asc") String[] sort,
            @RequestParam(required = false) String search) {
            System.out.println("Obteniendo personas con los siguientes parámetros: "
                    + "page=" + page + ", size=" + size + ", sort=" + String.join(",", sort) + ", search=" + search);
            String ordenarPor = sort[0];
            String direccionOrden = (sort.length > 1 && sort[1].equalsIgnoreCase("desc")) ? "desc" : "asc";

         Page<PersonaDTO> personas = personaService.getAllActivePersons(
                page,
                size,
                ordenarPor,
                direccionOrden,
                search 
        );
        return new ResponseEntity<>(personas, HttpStatus.OK);
    }

    @PostMapping ("/crearPersona")
     public ResponseEntity<PersonaDTO> crearPersona(
            @Valid @RequestBody PersonaDTO personaRequestDTO) { 
        PersonaDTO personaCreada = personaService.crearPersonaConRoles(personaRequestDTO);
        return new ResponseEntity<>(personaCreada, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonaDTO> obtenerPersonaPorId(@PathVariable Long id) {
        PersonaDTO persona = personaService.obtenerPersonaPorId(id);
        return new ResponseEntity<>(persona, HttpStatus.OK);
    }

    @GetMapping("/allPersonas")
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
