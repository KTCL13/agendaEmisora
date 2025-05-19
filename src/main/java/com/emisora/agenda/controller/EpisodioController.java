package com.emisora.agenda.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emisora.agenda.dto.EpisodioDTO;
import com.emisora.agenda.service.EpisodioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/episodios") // Ruta base para los endpoints de episodios
@RequiredArgsConstructor
public class EpisodioController {

    private final EpisodioService episodioService;

    @PostMapping
    public ResponseEntity<EpisodioDTO> crearEpisodio(@Valid @RequestBody EpisodioDTO episodioDTO) {
        EpisodioDTO nuevoEpisodio = episodioService.crearEpisodio(episodioDTO);
        return new ResponseEntity<>(nuevoEpisodio, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EpisodioDTO> obtenerEpisodioPorId(@PathVariable Long id) {
        EpisodioDTO episodio = episodioService.obtenerEpisodioPorId(id);
        return ResponseEntity.ok(episodio);
    }

    @GetMapping
    public ResponseEntity<List<EpisodioDTO>> obtenerTodosLosEpisodios() {
        List<EpisodioDTO> episodios = episodioService.obtenerTodosLosEpisodios();
        return ResponseEntity.ok(episodios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EpisodioDTO> actualizarEpisodio(@PathVariable Long id, @Valid @RequestBody EpisodioDTO episodioDTO) {
        EpisodioDTO episodioActualizado = episodioService.actualizarEpisodio(id, episodioDTO);
        return ResponseEntity.ok(episodioActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEpisodio(@PathVariable Long id) {
        episodioService.eliminarEpisodio(id);
        return ResponseEntity.noContent().build(); // HttpStatus 204
    }
}