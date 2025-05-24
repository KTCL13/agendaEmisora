package com.emisora.agenda.controller;

import com.emisora.agenda.dto.CancionDTO;
import com.emisora.agenda.service.CancionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/canciones")
@RequiredArgsConstructor
public class CancionController {

    private final CancionService cancionService;

    @PostMapping
    public ResponseEntity<CancionDTO> crearCancion(@RequestBody @Valid CancionDTO cancionDTO) {
        CancionDTO nueva = cancionService.crearCancion(cancionDTO);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CancionDTO>> obtenerTodasLasCanciones() {
        List<CancionDTO> canciones = cancionService.obtenerTodasLasCanciones();
        return new ResponseEntity<>(canciones, HttpStatus.OK);
    }
}
