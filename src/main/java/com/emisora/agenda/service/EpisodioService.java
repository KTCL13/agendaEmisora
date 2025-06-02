package com.emisora.agenda.service;

import java.util.Set;

import com.emisora.agenda.dto.EpisodioDTO;
import com.emisora.agenda.dto.EpisodioResponseDTO;
import com.emisora.agenda.mapper.EpisodioMapper;
import com.emisora.agenda.model.Cancion;
import com.emisora.agenda.model.Episodio;
import com.emisora.agenda.model.Programa;
import com.emisora.agenda.model.personas.Persona;
import com.emisora.agenda.repository.CancionRepository;
import com.emisora.agenda.repository.EpisodioRepository;
import com.emisora.agenda.repository.PersonaRepository;
import com.emisora.agenda.repository.ProgramaRepository;



import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@Service
@RequiredArgsConstructor
public class EpisodioService {

    private final EpisodioRepository episodioRepository;
    private final PersonaRepository personaRepository; 
    private final ProgramaRepository programaRepository; 
    private final CancionRepository cancionRepository; 
    private final EpisodioMapper episodioMapper; 
  

    @Transactional
    public EpisodioResponseDTO crearEpisodio( Long programaId,EpisodioDTO episodioDTO) { 
        System.out.println("Creando episodio para el programa con ID: " + programaId);
        Episodio episodio = episodioMapper.episodioDTOToEpisodio(episodioDTO);
        episodio.setPrograma(programaRepository.findById(programaId)
                .orElseThrow(() -> new EntityNotFoundException("Programa no encontrado con id: " + programaId)));
        episodio.setProductor(personaRepository.findById(episodioDTO.getProductorId())
                .orElseThrow(() -> new EntityNotFoundException("Productor no encontrado con id: " + episodioDTO.getProductorId())));
        episodio.setLocutor(personaRepository.findById(episodioDTO .getLocutorId())
                .orElseThrow(() -> new EntityNotFoundException("Locutor no encontrado con id: " + episodioDTO.getLocutorId())));

        if (episodioDTO.getInvitadosIds() != null) {
            Set<Persona> invitados = new HashSet<>(personaRepository.findAllById(episodioDTO.getInvitadosIds()));
            episodio.setInvitados(invitados);
        } else {
            episodio.setInvitados(new HashSet<>());
        }

        if (episodioDTO.getCancionIds() != null) {
            Set<Cancion> canciones = new HashSet<>(cancionRepository.findAllById(episodioDTO.getCancionIds()));
            episodio.setCanciones(canciones);
        } else {
            episodio.setCanciones(new HashSet<>());
            
        }

        Episodio episodioGuardado = episodioRepository.save(episodio);
        return episodioMapper.episodioToEpisodioResponseDTO(episodioGuardado);
    }


    @Transactional
    public EpisodioDTO obtenerEpisodioPorId(Long id) {
        Episodio episodio = episodioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Episodio no encontrado con id: " + id));
        return episodioMapper.episodioToEpisodioDTO(episodio);
    }

    public List<EpisodioDTO> obtenerTodosLosEpisodios() {
        List<Episodio> episodios = episodioRepository.findAll();
        List<EpisodioDTO> episodioDTOs = new ArrayList<>();
        for (Episodio episodio : episodios) {
            episodioDTOs.add(episodioMapper.episodioToEpisodioDTO(episodio));
        }
        return episodioDTOs;
    }



    @Transactional
    public EpisodioDTO actualizarEpisodio(Long id, EpisodioDTO episodioDTO) {
        Episodio episodioExistente = episodioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Episodio no encontrado con id: " + id));

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
        return (episodioMapper.episodioToEpisodioDTO(episodioActualizado));
    }


    @Transactional
    public void eliminarEpisodio(Long id) {
        if (!episodioRepository.existsById(id)) {
            throw new EntityNotFoundException("Episodio no encontrado con id: " + id);
        }
        episodioRepository.deleteById(id);
    }


    public Page<EpisodioResponseDTO> obtenerEpisodiosPorPrograma(Long programaId, int page, int size, String ordenarPor,
            String direccionOrden) {
        System.out.println("Obteniendo episodios para el programa con ID: " + programaId);
        Pageable pageable= PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direccionOrden), ordenarPor));
        Page<Episodio> episodiosPage;

        episodiosPage = episodioRepository.findByProgramaId(programaId, pageable);

        System.out.println("Episodios obtenidos: " + episodiosPage.getContent().size());

        return episodiosPage.map(episodioMapper::episodioToEpisodioResponseDTO);
    }


}