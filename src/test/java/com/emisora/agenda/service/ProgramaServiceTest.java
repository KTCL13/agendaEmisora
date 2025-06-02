package com.emisora.agenda.service;

import com.emisora.agenda.dto.ProgramaDTO;
import com.emisora.agenda.mapper.ProgramaMapper;
import com.emisora.agenda.model.Programa;
import com.emisora.agenda.repository.ProgramaRepository;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgramaServiceTest {
    @Mock private ProgramaRepository programaRepository;
    @Mock private ProgramaMapper programaMapper;

    @InjectMocks
    private ProgramaService programaService;

    private Programa programa;
    private ProgramaDTO programaDTO;

    @BeforeEach
    void setUp() {
        programa = new Programa();
        programa.setId(1L);
        programa.setTitulo("Programa de prueba");

        programaDTO = new ProgramaDTO();
        programaDTO.setId(1L);
        programaDTO.setTitulo("ProgramaDTO de prueba");
    }



    @Test
    void guardaUnPrograma() {
        when(programaMapper.toEntity(programaDTO)).thenReturn(programa);
        when(programaRepository.save(programa)).thenReturn(programa);
        when(programaMapper.toDto(programa)).thenReturn(programaDTO);

        ProgramaDTO resultado = programaService.savePrograma(programaDTO);

        assertNotNull(resultado);
        assertEquals(programaDTO.getTitulo(), resultado.getTitulo());

        verify(programaMapper).toEntity(programaDTO);
        verify(programaRepository).save(programa);
        verify(programaMapper).toDto(programa);
    }

    @Test
    void eliminaProgramaPorId() {
        Long id = 1L;

        programaService.deletePrograma(id);

        verify(programaRepository).deleteById(id);
    }
}