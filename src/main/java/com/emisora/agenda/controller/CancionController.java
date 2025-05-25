package com.emisora.agenda.controller;

import com.emisora.agenda.dto.CancionDTO;
import com.emisora.agenda.dto.OperationResponseDTO;
import com.emisora.agenda.exceptions.ResourceNotFoundException;
import com.emisora.agenda.mapper.CancionMapper;
import com.emisora.agenda.model.Cancion;
import com.emisora.agenda.repository.CancionRepository;
import com.emisora.agenda.service.CancionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/canciones")
@RequiredArgsConstructor
public class CancionController {

    private final CancionService cancionService;

    @Autowired
    private CancionRepository cancionRepository;

    @Autowired
    private CancionMapper cancionMapper;

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

    @PutMapping("/{id}")
    public ResponseEntity<CancionDTO> actualizarCancion(
            @PathVariable Long id,
            @RequestBody @Valid CancionDTO cancionDTO
    ) {
        CancionDTO actualizada = cancionService.actualizarCancion(id, cancionDTO);
        return ResponseEntity.ok(actualizada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CancionDTO> obtenerCancionPorId(@PathVariable Long id) {
        Cancion cancion = cancionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Canción no encontrada"));
        return ResponseEntity.ok(cancionMapper.toDto(cancion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OperationResponseDTO> eliminarCancion(@PathVariable Long id) {
        cancionService.eliminarCancion(id);
        return ResponseEntity.ok(new OperationResponseDTO("Canción eliminada correctamente"));
    }

}
