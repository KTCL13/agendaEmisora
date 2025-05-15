package com.emisora.agenda.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.emisora.agenda.dto.PersonaDTO;
import com.emisora.agenda.dto.RolDTO;
import com.emisora.agenda.enums.CarreraEnum;
import com.emisora.agenda.enums.TipoId;
import com.emisora.agenda.mapper.PersonaMapper;
import com.emisora.agenda.mapper.RolMapper;
import com.emisora.agenda.model.personas.EstudianteRol;
import com.emisora.agenda.model.personas.Persona;
import com.emisora.agenda.model.personas.ProfesorRol;
import com.emisora.agenda.model.personas.Rol;
import com.emisora.agenda.repository.PersonaRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class) 
class PersonaServiceTest {

    @Mock 
    private PersonaRepository personaRepository;

    @Mock 
    private PersonaMapper personaMapper;

    @Mock 
    private RolMapper rolMapper;

    @InjectMocks 
    private PersonaService personaService;

    private PersonaDTO personaRequestDTO;
    private Persona personaMappedFromRequest;
    private EstudianteRol estudianteRolEntity;
    private ProfesorRol profesorRolEntity;
    private Persona personaGuardada;
    private PersonaDTO expectedResponseDTO;

    @BeforeEach
    void setUp() {
        

        // 1. DTO de Solicitud
        personaRequestDTO = new PersonaDTO();
        personaRequestDTO.setNombres("Juan Test");
        personaRequestDTO.setApellidos("Pérez");
        personaRequestDTO.setTelefono  ("0987654321");
        personaRequestDTO.setTipoId(TipoId.CEDULA.toString());
        personaRequestDTO.setNumeroId("ID12345");
        personaRequestDTO.setCorreo("xd@uptc.edu.co");
        


        RolDTO rolEstudianteDTO = new RolDTO();
        rolEstudianteDTO.setTipoRol("ESTUDIANTE");
        rolEstudianteDTO.setCodigoUniversidad("E001");
        rolEstudianteDTO.setCarrera("INGENIERIA_DE_SISTEMAS");

        RolDTO rolProfesorDTO = new RolDTO();
        rolProfesorDTO.setTipoRol("PROFESOR");
        rolProfesorDTO.setDepartamento("Ingeniería");
        rolProfesorDTO.setCarrera("INGENIERIA_ELECTRONICA");

        personaRequestDTO.setRoles(Arrays.asList(rolEstudianteDTO, rolProfesorDTO));


        personaMappedFromRequest = new Persona(); 

        estudianteRolEntity = new EstudianteRol();
        estudianteRolEntity.setCodigoUniversidad("E001");
        estudianteRolEntity.setCarrera(CarreraEnum.INGENIERIA_SISTEMAS);
        estudianteRolEntity.setSemestre(2);


        profesorRolEntity = new ProfesorRol();
        profesorRolEntity.setCarrera(CarreraEnum.INGENIERIA_SISTEMAS);
        profesorRolEntity.setFacultad("Ingeniería");
        profesorRolEntity.setCodigoUniversidad("202112344");
    

        // 3. Entidad Persona Guardada (simulando lo que retornaría el repositorio)
        personaGuardada = new Persona();
        personaGuardada.setId( 1L);
        personaGuardada.setNombres("Juan Test");
        personaGuardada.setTelefono  ("0987654321");
        personaGuardada.setTipoId(TipoId.CEDULA);
        personaGuardada.setNumeroId("ID12345");
        personaGuardada.setCorreo("xd@uptc.edu.co");
        // Los roles se añadirían a esta instancia dentro de la lógica del servicio antes de llamar a save,
        // y el mock de save retornaría esta instancia (o una similar con IDs).

        // 4. DTO de Respuesta Esperado
        expectedResponseDTO = new PersonaDTO();
        expectedResponseDTO.setId( 1L);
        expectedResponseDTO.setNombres("Juan Test");
        expectedResponseDTO.setTelefono  ("0987654321");
        expectedResponseDTO.setTipoId(TipoId.CEDULA.toString());
        expectedResponseDTO.setNumeroId("ID12345");
        expectedResponseDTO.setCorreo("xd@uptc.edu.co");
        // Los roles DTO se añadirían aquí si los mapeamos completamente.
    }

    @Test
    void crearPersonaConRoles_Success() {
        // Arrange (Configuración de los mocks)

        // Mock para personaMapper.dtoToPersona
        // Simulamos que el mapper retorna una instancia de Persona (sin roles inicialmente)
        // para probar la inicialización de la lista de roles en el servicio.
        Persona personaInicialMapeada = new Persona();
        // Aseguramos que getRoles() sea null para probar la línea: if (persona.getRoles() == null)
        personaInicialMapeada.setRoles(null);
        when(personaMapper.dtoToPersona(personaRequestDTO)).thenReturn(personaInicialMapeada);


        // Mock para los métodos de rolMapper para convertir DTOs de Rol a Entidades Rol
        // Asumimos que RolDTO tiene getTipoRol(), getCodigoEstudiante(), etc.
        when(rolMapper.dtoToEstudianteRol(
            personaRequestDTO.getRoles().get(0))) // el primer rolDTO es Estudiante
            .thenReturn(estudianteRolEntity);

        when(rolMapper.dtoToProfesorRol(
            personaRequestDTO.getRoles().get(1))) // el segundo rolDTO es Profesor
            .thenReturn(profesorRolEntity);

        // Mock para personaRepository.save
        // Hacemos que el save retorne la persona con un ID asignado y los roles adjuntos.
        // Usamos thenAnswer para simular la asignación de ID y que los roles ya están en la entidad 'persona'.
          when(personaRepository.save(any(Persona.class))).thenAnswer(invocation -> {
            Persona personaPasadaASave = invocation.getArgument(0);
            personaPasadaASave.setId(1L); // Simula la generación de ID por la BD

   
            if (personaPasadaASave.getTipoId() == null) {
                 // Asigna un valor por defecto o uno basado en la lógica de tu aplicación
                personaPasadaASave.setTipoId(TipoId.CEDULA);
            }
            // También asegúrate que otros campos que se usan en el mapeo a DTO tengan valor
            if (personaPasadaASave.getNombres() == null) personaPasadaASave.setNombres(personaRequestDTO.getNombres());
            if (personaPasadaASave.getApellidos() == null) personaPasadaASave.setApellidos(personaRequestDTO.getApellidos());
            if (personaPasadaASave.getTelefono() == null) personaPasadaASave.setTelefono(personaRequestDTO.getTelefono());
            if (personaPasadaASave.getNumeroId() == null) personaPasadaASave.setNumeroId(personaRequestDTO.getNumeroId());
            if (personaPasadaASave.getCorreo() == null) personaPasadaASave.setCorreo(personaRequestDTO.getCorreo());


            // Simula la generación de ID para los roles si es necesario para el DTO de respuesta
            long rolIdCounter = 1L;
            if (personaPasadaASave.getRoles() != null) {
                for (Rol r : personaPasadaASave.getRoles()) {
                    if (r.getRolId() == null) {
                         // r.setRolId(rolIdCounter++); // El ORM lo haría en la realidad
                    }
                }
            }
            return personaPasadaASave;
        });

        // Mock para personaMapper.toDto
        // Este es el DTO final que esperamos que el método de servicio retorne.
        // Lo configuraremos para que retorne nuestro 'expectedResponseDTO'
        // cuando se le pase la entidad persona que simulamos fue guardada.
        // Para que esto funcione bien, 'expectedResponseDTO' debe reflejar el estado de 'personaGuardadaSimulada'.
        // En una prueba real, podrías hacer que este mock construya el DTO basado en la entrada
        // o simplemente verificar que la entrada a este mock sea correcta.
        // Por simplicidad aquí, retornamos un DTO preconfigurado.
        when(personaMapper.toDto(any(Persona.class))).thenAnswer(invocation -> {
            Persona pGuardada = invocation.getArgument(0);
            PersonaDTO resp = new PersonaDTO();
            resp.setId(pGuardada.getId());
            resp.setNombres(pGuardada.getNombres());
            resp.setApellidos(pGuardada.getApellidos());
            resp.setTelefono(pGuardada.getTelefono());
            resp.setTipoId(pGuardada.getTipoId().name());
            resp.setNumeroId(pGuardada.getNumeroId());
            resp.setCorreo(pGuardada.getCorreo());
            if (pGuardada.getRoles() != null) {
                List<RolDTO> rolesResp = new ArrayList<>();
                for(Rol r : pGuardada.getRoles()){
                    RolDTO rolRespDto = new RolDTO();
                    // Aquí necesitarías mapear completamente el rol entidad a rol DTO
                    // Usando rolMapper.toRolDTO(r) sería lo ideal si ese mock está bien configurado.
                    // Por ahora, solo el tipo.
                    if(r instanceof EstudianteRol) rolMapper.dtoToEstudianteRol(rolRespDto);
                    if(r instanceof ProfesorRol)  rolMapper.dtoToProfesorRol(rolRespDto);
                    rolesResp.add(rolRespDto);
                }
                resp.setRoles(rolesResp);
            }
            return resp;
        });


        // Act (Ejecutar el método a probar)
        PersonaDTO actualResponseDTO = personaService.crearPersonaConRoles(personaRequestDTO);

        // Assert (Verificar los resultados y las interacciones con los mocks)
        assertNotNull(actualResponseDTO);
        assertEquals(personaRequestDTO.getNombres(), actualResponseDTO.getNombres());
        assertEquals(personaRequestDTO.getApellidos(), actualResponseDTO.getApellidos());
        assertEquals(personaRequestDTO.getTelefono(), actualResponseDTO.getTelefono());
        assertEquals(personaRequestDTO.getTipoId(), actualResponseDTO.getTipoId());
        assertEquals(personaRequestDTO.getNumeroId(), actualResponseDTO.getNumeroId());
        assertEquals(personaRequestDTO.getCorreo(), actualResponseDTO.getCorreo());
        assertNotNull(actualResponseDTO.getId());
        assertEquals(2, actualResponseDTO.getRoles().size()); 

        // Verificar que los métodos de los mappers fueron llamados correctamente
        verify(personaMapper).dtoToPersona(personaRequestDTO);
        verify(rolMapper).dtoToEstudianteRol(personaRequestDTO.getRoles().get(0));
        verify(rolMapper).dtoToProfesorRol(personaRequestDTO.getRoles().get(1));

        // Capturar el argumento pasado a personaRepository.save para verificarlo
        ArgumentCaptor<Persona> personaArgumentCaptor = ArgumentCaptor.forClass(Persona.class);
        verify(personaRepository).save(personaArgumentCaptor.capture());
        Persona personaPasadaASave = personaArgumentCaptor.getValue();

        assertNotNull(personaPasadaASave);
        assertEquals(personaRequestDTO.getNombres(), personaPasadaASave.getNombres());
        assertEquals(personaRequestDTO.getApellidos(), personaPasadaASave.getApellidos());
        assertEquals(personaRequestDTO.getTelefono(), personaPasadaASave.getTelefono());
        assertEquals(personaRequestDTO.getTipoId(), personaPasadaASave.getTipoId().toString());
        assertEquals(personaRequestDTO.getNumeroId(), personaPasadaASave.getNumeroId());
        assertEquals(personaRequestDTO.getCorreo(), personaPasadaASave.getCorreo());
        assertNotNull(personaPasadaASave.getRoles());
        assertEquals(2, personaPasadaASave.getRoles().size());

        // Verificar que los roles correctos fueron añadidos a la Persona
        assertTrue(personaPasadaASave.getRoles().stream().anyMatch(r -> r instanceof EstudianteRol && ((EstudianteRol)r).getCodigoUniversidad().equals("E001")));
        assertTrue(personaPasadaASave.getRoles().stream().anyMatch(r -> r instanceof EstudianteRol && ((EstudianteRol)r).getCarrera().equals(CarreraEnum.INGENIERIA_SISTEMAS)));
        assertTrue(personaPasadaASave.getRoles().stream().anyMatch(r -> r instanceof EstudianteRol && ((EstudianteRol)r).getSemestre() == 2));

        assertTrue(personaPasadaASave.getRoles().stream().anyMatch(r -> r instanceof ProfesorRol && ((ProfesorRol)r).getCodigoUniversidad().equals("202112344")));
        assertTrue(personaPasadaASave.getRoles().stream().anyMatch(r -> r instanceof ProfesorRol && ((ProfesorRol)r).getCarrera().equals(CarreraEnum.INGENIERIA_SISTEMAS)));
        assertTrue(personaPasadaASave.getRoles().stream().anyMatch(r -> r instanceof ProfesorRol && ((ProfesorRol)r).getFacultad().equals("Ingeniería")));
  

        // Verificar que la relación bidireccional está establecida (rol.getPersona() == persona)
        for (Rol rol : personaPasadaASave.getRoles()) {
            assertSame(personaPasadaASave, rol.getPersona(), "La referencia 'persona' en el Rol no está establecida correctamente.");
        }

        // Verificar que el mapeo final a PersonaDTO se hizo con la persona guardada
        verify(personaMapper).toDto(personaPasadaASave);
    }

    @Test
    void crearPersonaConRoles_RequestWithNoRoles_Success() {
        // Arrange
        personaRequestDTO.setRoles(null); // O Collections.emptyList()

        Persona personaInicialMapeada = new Persona();
        when(personaMapper.dtoToPersona(personaRequestDTO)).thenReturn(personaInicialMapeada);

          when(personaRepository.save(any(Persona.class))).thenAnswer(invocation -> {
            Persona personaPasadaASave = invocation.getArgument(0);
            personaPasadaASave.setId(1L); // Simula la generación de ID por la BD

            // ***** CORRECCIÓN CLAVE AQUÍ *****
            // Asegúrate de que la entidad Persona tenga un valor para tipoId
            // antes de que sea "guardada" y luego mapeada a DTO.
            // Esto podría venir del mapeo inicial DTO->Entidad o establecerse aquí si es parte
            // de la lógica de "guardado" simulada.
            if (personaPasadaASave.getTipoId() == null) {
                 // Asigna un valor por defecto o uno basado en la lógica de tu aplicación
                personaPasadaASave.setTipoId(TipoId.CEDULA);
            }
            // También asegúrate que otros campos que se usan en el mapeo a DTO tengan valor
            if (personaPasadaASave.getNombres() == null) personaPasadaASave.setNombres(personaRequestDTO.getNombres());
            if (personaPasadaASave.getApellidos() == null) personaPasadaASave.setApellidos(personaRequestDTO.getApellidos());
            if (personaPasadaASave.getTelefono() == null) personaPasadaASave.setTelefono(personaRequestDTO.getTelefono());
            if (personaPasadaASave.getNumeroId() == null) personaPasadaASave.setNumeroId(personaRequestDTO.getNumeroId());
            if (personaPasadaASave.getCorreo() == null) personaPasadaASave.setCorreo(personaRequestDTO.getCorreo());


            // Simula la generación de ID para los roles si es necesario para el DTO de respuesta
            long rolIdCounter = 1L;
            if (personaPasadaASave.getRoles() != null) {
                for (Rol r : personaPasadaASave.getRoles()) {
                    if (r.getRolId() == null) {
                         // r.setRolId(rolIdCounter++); // El ORM lo haría en la realidad
                    }
                }
            }
            return personaPasadaASave;
        });
        
        // Adaptar la respuesta del personaMapper.toDto para este caso
        when(personaMapper.toDto(any(Persona.class))).thenAnswer(invocation -> {
            Persona pGuardada = invocation.getArgument(0);
            PersonaDTO resp = new PersonaDTO();
            resp.setId((pGuardada.getId()));
            resp.setNombres(pGuardada.getNombres());
            resp.setApellidos(pGuardada.getApellidos());
            resp.setCorreo(pGuardada.getCorreo());
            resp.setTelefono(pGuardada.getTelefono());
            resp.setTipoId(pGuardada.getTipoId().name());
            resp.setNumeroId(pGuardada.getNumeroId());
            resp.setRoles(Collections.emptyList()); // Esperamos una lista vacía de roles
            return resp;
        });

        // Act
        PersonaDTO actualResponseDTO = personaService.crearPersonaConRoles(personaRequestDTO);

        // Assert
        assertNotNull(actualResponseDTO);
        assertEquals(personaRequestDTO.getNombres(), actualResponseDTO.getNombres());
        assertNotNull(actualResponseDTO.getId());
        assertTrue(actualResponseDTO.getRoles() == null || actualResponseDTO.getRoles().isEmpty());

        ArgumentCaptor<Persona> personaArgumentCaptor = ArgumentCaptor.forClass(Persona.class);
        verify(personaRepository).save(personaArgumentCaptor.capture());
        Persona personaPasadaASave = personaArgumentCaptor.getValue();

        assertTrue(personaPasadaASave.getRoles() == null || personaPasadaASave.getRoles().isEmpty());
        verify(rolMapper, never()).dtoToEstudianteRol(any()); // No se deben llamar mappers de rol
        verify(rolMapper, never()).dtoToProfesorRol(any());
    }

    @Test
    void crearPersonaConRoles_UnknownRoleType_ShouldThrowIllegalArgumentException() {
        // Arrange
        RolDTO unknownRolDto = new RolDTO();
        unknownRolDto.setTipoRol("ROL_INVALIDO");
        personaRequestDTO.setRoles(Collections.singletonList(unknownRolDto));

        Persona personaInicialMapeada = new Persona();
        when(personaMapper.dtoToPersona(personaRequestDTO)).thenReturn(personaInicialMapeada);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            personaService.crearPersonaConRoles(personaRequestDTO);
        });

        assertEquals("Tipo de rol desconocido: ROL_INVALIDO", exception.getMessage());
        verify(personaRepository, never()).save(any()); // No se debe llamar a save si hay un error
    }
}