package com.emisora.agenda.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emisora.agenda.dto.ProgramaDTO;
import com.emisora.agenda.service.ProgramaService;

@RestController
@RequestMapping("/api/programa")
public class ProgramaController {

    @Autowired
    private ProgramaService programaService;

    @GetMapping("/allProgramas")
    public ResponseEntity<List<ProgramaDTO>> getAllProgramas() {
        List<ProgramaDTO> programas = programaService.getAllProgramas();
        return new ResponseEntity<>(programas, HttpStatus.OK);
    }

    @PostMapping("/addPrograma")
    public ResponseEntity<ProgramaDTO> savePrograma(@RequestBody ProgramaDTO programaDTO) {
        ProgramaDTO ProgramaAdded = programaService.savePrograma(programaDTO);
        return new ResponseEntity<>(ProgramaAdded, HttpStatus.CREATED);
    }


    @PostMapping("/updatePrograma")
    public ResponseEntity<ProgramaDTO> updatePrograma(@RequestBody ProgramaDTO programaDTO) {
        ProgramaDTO updatedPrograma = programaService.savePrograma(programaDTO);
        return new ResponseEntity<>(updatedPrograma, HttpStatus.OK);}


    @DeleteMapping("/deletePrograma")
    public ResponseEntity<Void> deletePrograma(@RequestBody Long id) {
        programaService.deletePrograma(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    
}
