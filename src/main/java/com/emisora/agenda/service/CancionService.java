package com.emisora.agenda.service;

import com.emisora.agenda.dto.CancionDTO;
import com.emisora.agenda.mapper.CancionMapper;
import com.emisora.agenda.model.Cancion;
import com.emisora.agenda.repository.CancionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CancionService {

    private final CancionRepository cancionRepository;
    private final CancionMapper cancionMapper;

    public CancionDTO crearCancion(@Valid CancionDTO cancionDTO) {
        Cancion nuevaCancion = cancionRepository.save(cancionMapper.toEntity(cancionDTO));
        return cancionMapper.toDto(nuevaCancion);
    }

    public List<CancionDTO> obtenerTodasLasCanciones() {
        return cancionRepository.findAll().stream()
                .map(cancionMapper::toDto)
                .toList();
    }
}
