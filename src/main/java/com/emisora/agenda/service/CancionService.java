package com.emisora.agenda.service;

import com.emisora.agenda.dto.CancionDTO;
import com.emisora.agenda.exceptions.AppException;
import com.emisora.agenda.exceptions.ResourceNotFoundException;
import com.emisora.agenda.mapper.CancionMapper;
import com.emisora.agenda.model.Cancion;
import com.emisora.agenda.repository.CancionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CancionService {

    private final CancionRepository cancionRepository;
    private final CancionMapper cancionMapper;

    public CancionDTO crearCancion(@Valid CancionDTO cancionDTO) {
        // Validar que la canción no exista ya
        validarCancion(cancionDTO);

        Cancion cancion = CancionMapper.INSTANCE.toEntity(cancionDTO);
        Cancion guardada = cancionRepository.save(cancion);
        return CancionMapper.INSTANCE.toDto(guardada);
    }

    public List<CancionDTO> obtenerTodasLasCanciones() {
        return cancionRepository.findAll().stream()
                .map(cancionMapper::toDto)
                .toList();
    }

    public CancionDTO actualizarCancion(Long id, @Valid CancionDTO cancionDTO) {
        Cancion existente = cancionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Canción no encontrada con ID: " + id));

        // Actualizar campos
        validarCancion(cancionDTO);
        existente.setTitulo(cancionDTO.getTitulo());
        existente.setArtista(cancionDTO.getArtista());

        Cancion actualizada = cancionRepository.save(existente);

        return cancionMapper.toDto(actualizada);
    }

    public void eliminarCancion(Long id) {
        if (!cancionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Canción no encontrada con ID: " + id);
        }
        cancionRepository.deleteById(id);
    }


    private void validarCancion(CancionDTO cancionDTO) {
        if (cancionRepository.findByTituloAndArtista(cancionDTO.getTitulo(), cancionDTO.getArtista()).isPresent()) {
            throw new AppException("Ya existe una canción con ese título y artista", HttpStatus.CONFLICT);
        }
    }

    @Transactional(readOnly = true)
    public List<CancionDTO> buscarCanciones(String term, int limit) {
        if (term == null || term.trim().isEmpty()) {
            return List.of(); 
        }

        Pageable pageable = PageRequest.of(0, limit); 
        
        Page<Cancion> paginaCanciones = cancionRepository.findByTituloOrArtistaContainingIgnoreCase(term, pageable);
        
        return paginaCanciones.getContent() 
                .stream()
                .map(cancionMapper::toDto)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<CancionDTO> obtenerCancionesPorIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList(); // Devuelve lista vacía si no se proporcionan IDs
        }
        // Filtra cualquier ID nulo que pudiera venir en la lista para evitar errores con findAllById
        List<Long> validIds = ids.stream()
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());
        
        if (validIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<Cancion> canciones = cancionRepository.findAllById(validIds);
        return canciones.stream()
                        .map(cancionMapper::toDto)
                        .collect(Collectors.toList());
    }
}       
