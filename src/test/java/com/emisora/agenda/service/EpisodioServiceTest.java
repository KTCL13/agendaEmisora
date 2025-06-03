package com.emisora.agenda.service;

import com.emisora.agenda.dto.EpisodioDTO;
import com.emisora.agenda.dto.EpisodioResponseDTO;
import com.emisora.agenda.mapper.EpisodioMapper;
import com.emisora.agenda.model.*;
import com.emisora.agenda.model.personas.Persona;
import com.emisora.agenda.repository.*;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EpisodioServiceTest {

    @Mock private EpisodioRepository episodioRepository;
    @Mock private PersonaRepository personaRepository;
    @Mock private ProgramaRepository programaRepository;
    @Mock private CancionRepository cancionRepository;
    @Mock private EpisodioMapper episodioMapper;

    @InjectMocks
    private EpisodioService episodioService;

    private Episodio episodio;
    private EpisodioDTO episodioDTO;
    private Persona productor, locutor;
    private Programa programa;
    private Cancion cancion;

    @BeforeEach
    void setUp() {
        episodioDTO = new EpisodioDTO();
        episodioDTO.setId(1L);
        episodioDTO.setNombre("EpisodioDTO de prueba");
        episodioDTO.setProductorId(2L);
        episodioDTO.setLocutorId(3L);
        episodioDTO.setProgramaId(4L);
        episodioDTO.setInvitadosIds(Set.of(5L));
        episodioDTO.setCancionIds(Set.of(6L));

        episodio = new Episodio();
        episodio.setId(1L);
        episodio.setNombre("Episodio de prueba");

        productor = new Persona(); productor.setIdPersona(2L);
        locutor = new Persona(); locutor.setIdPersona(3L);
        programa = new Programa(); programa.setId(4L);
        cancion = new Cancion(); cancion.setId(6L);
    }


    @Test
    void obtieneEpisodioPorId() {
        when(episodioRepository.findById(1L)).thenReturn(Optional.of(episodio));
        when(episodioMapper.episodioToEpisodioDTO(episodio)).thenReturn(episodioDTO);

        EpisodioDTO result = episodioService.obtenerEpisodioPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void lanzaExcepcionSiNoExisteEpisodioPorId() {
        when(episodioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> episodioService.obtenerEpisodioPorId(99L));
    }

    @Test
    void obtieneTodosLosEpisodios() {
        when(episodioRepository.findAll()).thenReturn(List.of(episodio));
        when(episodioMapper.episodioToEpisodioDTO(episodio)).thenReturn(episodioDTO);

        List<EpisodioDTO> resultados = episodioService.obtenerTodosLosEpisodios();

        assertEquals(1, resultados.size());
    }

 

    @Test
    void lanzaExcepcionSiNoExisteEpisodioAActualizar() {
        when(episodioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> episodioService.actualizarEpisodio(1L, episodioDTO));
    }

    @Test
    void lanzaExcepcionSiNoExisteProductor() {
        when(episodioRepository.findById(1L)).thenReturn(Optional.of(episodio));
        when(personaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> episodioService.actualizarEpisodio(1L, episodioDTO));
    }

    @Test
    void lanzaExcepcionAlEliminarEpisodioInexistente() {
        when(episodioRepository.existsById(2L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> episodioService.eliminarEpisodio(2L));
    }
}