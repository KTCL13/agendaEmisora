package com.emisora.agenda.controller;



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

import com.emisora.agenda.dto.EpisodioDTO;
import com.emisora.agenda.dto.EpisodioResponseDTO;
import com.emisora.agenda.service.EpisodioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/episodios") // Ruta base para los endpoints de episodios
@RequiredArgsConstructor
public class EpisodioController {

    private final EpisodioService episodioService;
    

    @GetMapping("por-programa/{programaId}")
    public ResponseEntity<Page<EpisodioResponseDTO>> obtenerEpisodiosPorPrograma(
            @PathVariable Long programaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,    
            @RequestParam(defaultValue = "nombre,asc") String[] sort) {
        System.out.println("Obteniendo episodios para el programa con ID: " + programaId);
        String ordenarPor = sort[0];
        String direccionOrden = (sort.length > 1 && sort[1].equalsIgnoreCase("desc")) ? "desc" : "asc";

        Page<EpisodioResponseDTO> episodios = episodioService.obtenerEpisodiosPorPrograma(
                programaId, page, size, ordenarPor, direccionOrden);

        return new ResponseEntity<>(episodios, HttpStatus.OK);
        
    }

    @GetMapping ("{id}")
    public ResponseEntity<EpisodioDTO> getEpisodioPorId(@PathVariable Long id) {
        System.out.println("Obteniendo episodio con ID: " + id);
        EpisodioDTO episodio = episodioService.obtenerEpisodioPorId(id);
        return new ResponseEntity<>(episodio, HttpStatus.OK);
    }


    @PostMapping("/{programaId}")
    public ResponseEntity<EpisodioResponseDTO> crearEpisodio(@PathVariable Long programaId, @Valid @RequestBody EpisodioDTO episodioDTO) {
        System.out.println("Creando episodio para el programa con ID: " + programaId);
        EpisodioResponseDTO nuevoEpisodio = episodioService.crearEpisodio(programaId,episodioDTO);
        return new ResponseEntity<>(nuevoEpisodio, HttpStatus.CREATED);
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