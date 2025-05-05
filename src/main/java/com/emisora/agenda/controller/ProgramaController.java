package com.emisora.agenda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        ProgramaDTO ProgramaAdded = programaService.saveProgram(programaDTO);
        return new ResponseEntity<>(ProgramaAdded, HttpStatus.CREATED);
    }

    
}
