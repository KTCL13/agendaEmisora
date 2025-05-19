package com.emisora.agenda.service;

import java.util.List;
import java.util.Set;

import com.emisora.agenda.dto.EpisodioDTO;
import com.emisora.agenda.mapper.EpisodioMapper;
import com.emisora.agenda.model.Cancion;
import com.emisora.agenda.model.Episodio;
import com.emisora.agenda.model.Programa;
import com.emisora.agenda.model.personas.Persona;
import com.emisora.agenda.repository.CancionRepository;
import com.emisora.agenda.repository.EpisodioRepository;
import com.emisora.agenda.repository.PersonaRepository;
import com.emisora.agenda.repository.ProgramaRepository;

import jakarta.validation.Valid;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Inyección de dependencias por constructor de Lombok
public class EpisodioService {

    private final EpisodioRepository episodioRepository;
    private final PersonaRepository personaRepository; 
    private final ProgramaRepository programaRepository; 
    private final CancionRepository cancionRepository; 
    private final EpisodioMapper episodioMapper; 
  

    @Transactional
    public EpisodioDTO crearEpisodio(EpisodioDTO episodioDTO) {
        Episodio episodio = episodioMapper.toEntity(episodioDTO);
        Episodio episodioGuardado = episodioRepository.save(episodio);
        return episodioMapper.toDTO(episodioGuardado);
    }


    @Transactional(readOnly = true)
    public EpisodioDTO obtenerEpisodioPorId(Long id) {
        Episodio episodio = episodioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Episodio no encontrado con id: " + id));
        return episodioMapper.toDTO(episodio);
    }

 
    @Transactional(readOnly = true)
    public List<EpisodioDTO> obtenerTodosLosEpisodios() {
        return episodioRepository.findAll().stream()
                .map(episodioMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Transactional
    public EpisodioDTO actualizarEpisodio(Long id, EpisodioDTO episodioDTO) {
        Episodio episodioExistente = episodioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Episodio no encontrado con id: " + id));

        // Actualizar campos básicos
        episodioExistente.setNombre(episodioDTO.getNombre());
        episodioExistente.setDescripcion(episodioDTO.getDescripcion());
        episodioExistente.setFechasEmitidas(episodioDTO.getFechasEmitidas());
        episodioExistente.setDuracion(episodioDTO.getDuracion());

        // Actualizar relaciones ManyToOne
        Persona productor = personaRepository.findById(episodioDTO.getProductorId())
                .orElseThrow(() -> new EntityNotFoundException("Productor no encontrado con id: " + episodioDTO.getProductorId()));
        episodioExistente.setProductor(productor);

        Persona locutor = personaRepository.findById(episodioDTO.getLocutorId())
                .orElseThrow(() -> new EntityNotFoundException("Locutor no encontrado con id: " + episodioDTO.getLocutorId()));
        episodioExistente.setLocutor(locutor);

        Programa programa = programaRepository.findById(episodioDTO.getProgramaId())
                .orElseThrow(() -> new EntityNotFoundException("Programa no encontrado con id: " + episodioDTO.getProgramaId()));
        episodioExistente.setPrograma(programa);

        // Actualizar relaciones ManyToMany
        if (episodioDTO.getInvitadosIds() != null) {
            Set<Persona> invitados = new HashSet<>(personaRepository.findAllById(episodioDTO.getInvitadosIds()));
            episodioExistente.setInvitados(invitados);
        } else {
            episodioExistente.getInvitados().clear();
        }

        if (episodioDTO.getCancionIds() != null) {
            Set<Cancion> canciones = new HashSet<>(cancionRepository.findAllById(episodioDTO.getCancionIds()));
            episodioExistente.setCanciones(canciones);
        } else {
            episodioExistente.getCanciones().clear();
        }

        Episodio episodioActualizado = episodioRepository.save(episodioExistente);
        return (episodioMapper.toDTO(episodioActualizado));
    }

    @Transactional
    public void eliminarEpisodio(Long id) {
        if (!episodioRepository.existsById(id)) {
            throw new EntityNotFoundException("Episodio no encontrado con id: " + id);
        }
        episodioRepository.deleteById(id);
    }


}