package com.emisora.agenda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emisora.agenda.dto.ProgramaDTO;
import com.emisora.agenda.service.ProgramaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/programa")
public class ProgramaController {

    @Autowired
    private ProgramaService programaService;




    @GetMapping
    public ResponseEntity<Page<ProgramaDTO>> getAllProgramas(  
        @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombresPersona,asc") String[] sort,
            @RequestParam(required = false) String searchTerm) {

            String ordenarPor = sort[0];
            String direccionOrden = (sort.length > 1 && sort[1].equalsIgnoreCase("desc")) ? "desc" : "asc";

        Page<ProgramaDTO> programas = programaService.getAllProgramas(
                page,
                size,
                ordenarPor,
                direccionOrden,
                searchTerm
        );  
        return new ResponseEntity<>(programas, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProgramaDTO> getProgramaById(@PathVariable Long id) {
        ProgramaDTO programa = programaService.getProgramaById(id);
        if (programa != null) {
            return new ResponseEntity<>(programa, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/addPrograma")
    public ResponseEntity<ProgramaDTO> savePrograma(@RequestBody ProgramaDTO programaDTO) {
        ProgramaDTO ProgramaAdded = programaService.savePrograma(programaDTO);
        return new ResponseEntity<>(ProgramaAdded, HttpStatus.CREATED);
    }


    @PutMapping("/updatePrograma/{id}")
    public ResponseEntity<ProgramaDTO> updatePrograma(@PathVariable Long id, @RequestBody @Valid ProgramaDTO programaDTO) {
        ProgramaDTO updatedPrograma = programaService.updatePrograma(id, programaDTO);
        if (updatedPrograma == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedPrograma, HttpStatus.OK);}


    @DeleteMapping("/deletePrograma")
    public ResponseEntity<Void> deletePrograma(@RequestBody Long id) {
        programaService.deletePrograma(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    
}
