package com.emisora.agenda.service;

import com.emisora.agenda.dto.PersonaDTO;
import com.emisora.agenda.dto.RolDTO;
import com.emisora.agenda.exceptions.ResourceNotFoundException;
import com.emisora.agenda.mapper.PersonaMapper;
import com.emisora.agenda.mapper.RolMapper;
import com.emisora.agenda.model.personas.EstudianteRol;
import com.emisora.agenda.model.personas.Persona;
import com.emisora.agenda.model.personas.ProfesorRol;
import com.emisora.agenda.model.personas.Rol;
import com.emisora.agenda.repository.PersonaRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PersonaServiceTest {
    @Mock private PersonaRepository personaRepo;
    @Mock private PersonaMapper personaMapper;
    @Mock private RolMapper rolMapper;

    @InjectMocks
    private PersonaService personaService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void creaPersonaConRoles() {
        PersonaDTO dto = new PersonaDTO();
        dto.setNombres("Andrés");
        dto.setApellidos("Pérez");
        dto.setTipoId("Cédula");
        dto.setNumeroId("123456");
        RolDTO rolDto = new RolDTO();
        rolDto.setTipoRol("ESTUDIANTE");
        dto.setRoles(List.of(rolDto));

        Persona persona = new Persona();
        persona.setRoles(new ArrayList<>());

        EstudianteRol rol = mock(EstudianteRol.class);

        when(personaMapper.dtoToPersona(dto)).thenReturn(persona);
        when(rolMapper.dtoToEstudianteRol(rolDto)).thenReturn(rol);
        when(personaRepo.save(any())).thenReturn(persona);
        when(personaMapper.toDto(persona)).thenReturn(dto);

        PersonaDTO resultado = personaService.crearPersonaConRoles(dto);

        assertThat(resultado).isNotNull();
        verify(personaRepo).save(persona);
    }

    @Test
    void obtieneTodasLasPersonas() {
        List<Persona> personas = List.of(new Persona(), new Persona());
        List<PersonaDTO> dtos = List.of(new PersonaDTO(), new PersonaDTO());

        when(personaRepo.findAll()).thenReturn(personas);
        when(personaMapper.toDtoList(personas)).thenReturn(dtos);

        List<PersonaDTO> resultado = personaService.obtenerTodasLasPersonas();

        assertThat(resultado).hasSize(2);
    }

    @Test
    void actualizaPersona() {
        Long id = 1L;
        PersonaDTO dto = new PersonaDTO();
        dto.setNombres("Carlos");
        dto.setTipoId("Cédula");
        RolDTO rolDTO = new RolDTO();
        rolDTO.setTipoRol("ESTUDIANTE");
        dto.setRoles(List.of(rolDTO));

        Persona persona = new Persona();
        persona.setRoles(new ArrayList<>());

        ProfesorRol nuevoRol = mock(ProfesorRol.class);

        when(personaRepo.findById(id)).thenReturn(Optional.of(persona));
        when(rolMapper.dtoToProfesorRol(any())).thenReturn(nuevoRol);
        when(personaRepo.save(any())).thenReturn(persona);
        when(personaMapper.toDto(persona)).thenReturn(dto);

        PersonaDTO resultado = personaService.actualizarPersona(id, dto);

        assertThat(resultado).isNotNull();
        verify(personaRepo).save(persona);
    }

    @Test
    void eliminaPersona() {
        Long id = 1L;
        Persona persona = new Persona();

        when(personaRepo.findById(id)).thenReturn(Optional.of(persona));

        personaService.eliminarPersona(id);

        verify(personaRepo).delete(persona);
    }

    @Test
    void lanzaExcepcionSiEliminaPersonaInexistente() {
        when(personaRepo.findById(0L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> personaService.eliminarPersona(0L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Persona no encontrada");
    }

    @Test
    void obtienePersonaPorId() {
        Long id = 1L;
        Persona persona = new Persona();
        PersonaDTO dto = new PersonaDTO();

        when(personaRepo.findById(id)).thenReturn(Optional.of(persona));
        when(personaMapper.toDto(persona)).thenReturn(dto);

        PersonaDTO resultado = personaService.obtenerPersonaPorId(id);

        assertThat(resultado).isNotNull();
    }

    @Test
    void lanzaExcepcionSiPersonaPorIdNoExiste() {
        when(personaRepo.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> personaService.obtenerPersonaPorId(2L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Persona no encontrada");
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}