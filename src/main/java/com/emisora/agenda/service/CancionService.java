package com.emisora.agenda.service;

import com.emisora.agenda.dto.CancionDTO;
import com.emisora.agenda.exceptions.AppException;
import com.emisora.agenda.exceptions.ResourceNotFoundException;
import com.emisora.agenda.mapper.CancionMapper;
import com.emisora.agenda.model.Cancion;
import com.emisora.agenda.repository.CancionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

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


    private void validarCancion(CancionDTO cancionDTO) {
        if (cancionRepository.findByTituloAndArtista(cancionDTO.getTitulo(), cancionDTO.getArtista()).isPresent()) {
            throw new AppException("Ya existe una canción con ese título y artista", HttpStatus.CONFLICT);
        }
    }
}
