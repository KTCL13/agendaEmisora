package com.emisora.agenda.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.emisora.agenda.dto.PersonaDTO;
import com.emisora.agenda.mapper.PersonaMapper;
import com.emisora.agenda.model.Estudiante;
import com.emisora.agenda.model.Funcionario;
import com.emisora.agenda.model.Persona;
import com.emisora.agenda.model.PersonaFactory;
import com.emisora.agenda.repository.PersonaRepository;

import java.util.*;

class PersonaServiceTest {

    @Mock
    private PersonaRepository personaRepo;

    @Mock
    private PersonaFactory personaFactory;

    @Mock
    private PersonaMapper personaMapper;

    @InjectMocks
    private PersonaService personaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearPersona() {
        // Arrange
        PersonaDTO dto = new PersonaDTO();
        dto.nombre = "Ana";
        dto.apellido = "López";
        dto.correo = "ana@example.com";
        dto.cedula = "12345";
        dto.tipo = "estudiante";

        Persona persona = new Estudiante();
        PersonaDTO resultDto = new PersonaDTO();
        resultDto.nombre = "Ana";

        when(personaFactory.crearDesdeDTO(dto)).thenReturn(persona);
        when(personaRepo.save(persona)).thenReturn(persona);
        when(personaMapper.toDto(persona)).thenReturn(resultDto);

        // Act
        PersonaDTO result = personaService.crearPersona(dto);

        // Assert
        assertEquals("Ana", result.nombre);
        verify(personaFactory).crearDesdeDTO(dto);
        verify(personaRepo).save(persona);
        verify(personaMapper).toDto(persona);
    }

    @Test
    void testGetAllPersonas() {
        // Arrange
        Persona persona1 = new Estudiante();
        Persona persona2 = new Funcionario();

        List<Persona> lista = Arrays.asList(persona1, persona2);
        when(personaRepo.findAll()).thenReturn(lista);

        PersonaDTO dto1 = new PersonaDTO();
        dto1.nombre = "Ana";
        PersonaDTO dto2 = new PersonaDTO();
        dto2.nombre = "Carlos";

        when(personaMapper.toDto(persona1)).thenReturn(dto1);
        when(personaMapper.toDto(persona2)).thenReturn(dto2);

        // Act
        List<PersonaDTO> result = personaService.getAllPersonas();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Ana", result.get(0).nombre);
        assertEquals("Carlos", result.get(1).nombre);
    }
}