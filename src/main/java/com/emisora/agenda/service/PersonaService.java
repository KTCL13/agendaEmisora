package com.emisora.agenda.service;

import com.emisora.agenda.dto.PersonaDTO;
import com.emisora.agenda.dto.RolInstitucionalDTO;
import com.emisora.agenda.enums.EstadoPersona;
import com.emisora.agenda.exceptions.ResourceNotFoundException;
import com.emisora.agenda.mapper.PersonaMapper;
import com.emisora.agenda.mapper.RolInstitucionalMapper;
import com.emisora.agenda.model.personas.Persona;
import com.emisora.agenda.model.personas.RolInstitucional;
import com.emisora.agenda.repository.PersonaRepository;

import jakarta.transaction.Transactional;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PersonaService {

    private final PersonaRepository personaRepo;

    private final PersonaMapper personaMapper;
    private final RolInstitucionalMapper rolMapper;

    public PersonaService(PersonaRepository personaRepo, PersonaMapper personaMapper, RolInstitucionalMapper rolMapper) {
        this.personaRepo = personaRepo;
        this.personaMapper = personaMapper;
        this.rolMapper = rolMapper;
    }


    @Transactional
    public PersonaDTO crearPersonaConRoles(PersonaDTO requestDTO) {
        
        Persona persona = personaMapper.dtoToPersona(requestDTO);
        persona.setFechaCreacionPersona(LocalDateTime.now());
        persona.setEstado(com.emisora.agenda.enums.EstadoPersona.ACTIVO);
        
        if (persona.getRolesInstitucionales() == null) {
            persona.setRolesInstitucionales(new ArrayList<>());
        }
        if (requestDTO.getRolesInstitucionales() != null) {
            for (RolInstitucionalDTO rolDto : requestDTO.getRolesInstitucionales()) {
                RolInstitucional rolEntity = null;

                switch (rolDto.getTipoRol().toUpperCase()) {
                    case "ESTUDIANTE":
                        rolEntity = rolMapper.dtoToEstudianteRol(rolDto);
                        break;
                    case "DOCENTE":
                        rolEntity = rolMapper.dtoToProfesorRol(rolDto);
                        break;
                    case "INVITADO":
                        rolEntity = rolMapper.dtoToInvitadoRol(rolDto);
                        break;
                    case "FUNCIONARIO":
                        rolEntity = rolMapper.dtoToFuncionarioRol(rolDto);
                        break;
                    default:
                        throw new IllegalArgumentException("Tipo de rol desconocido: " + rolDto.getTipoRol());
                }
                if (rolEntity != null) {
                    persona.addRol(rolEntity); // El método addRol establece la relación bidireccional
                }
            }
        }

        Persona personaGuardada = personaRepo.save(persona);
    
        return personaMapper.toDto(personaGuardada);
    }


    public List<PersonaDTO> obtenerTodasLasPersonas() {
        List<Persona> personas = personaRepo.findByEstado(EstadoPersona.ACTIVO);
        return personaMapper.toDtoList(personas);
    }


       @Transactional
    public PersonaDTO actualizarPersona(Long personaId, PersonaDTO personaDTO) {
   
        Persona personaExistente = personaRepo.findById(personaId)
                .orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada con id: " + personaId));

        personaExistente.setNombresPersona(personaDTO.getNombresPersona());
        personaExistente.setApellidosPersona(personaDTO.getApellidosPersona());
        personaExistente.setTelefonoPersona(personaDTO.getTelefonoPersona());
        personaExistente.setCorreo(personaDTO.getCorreo());
        personaExistente.setNumeroId(personaDTO.getNumeroId());
        personaExistente.setFechaModificacionPersona(LocalDateTime.now());
        if (personaDTO.getTipoId() != null) {
            try {

                personaExistente.setTipoId(com.emisora.agenda.enums.TipoId.valueOf(personaDTO.getTipoId().toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.err.println("TipoId inválido proporcionado: " + personaDTO.getTipoId());
            }
        }
        if (personaExistente.getRolesInstitucionales() == null) {
            personaExistente.setRolesInstitucionales(new ArrayList<>());
        }
      
        List<RolInstitucional> rolesAntiguos = new ArrayList<>(personaExistente.getRolesInstitucionales());
        for(RolInstitucional rolAntiguo : rolesAntiguos){
            personaExistente.removeRol(rolAntiguo); 
        }

        if (personaDTO.getRolesInstitucionales() != null) {
            for (RolInstitucionalDTO rolDto : personaDTO.getRolesInstitucionales()) {
     
                RolInstitucional nuevoRolEntity = convertirRolDtoAEntidad(rolDto);
                if (nuevoRolEntity != null) {
                    personaExistente.addRol(nuevoRolEntity);
                }
            }
        }

        Persona personaActualizada = personaRepo.save(personaExistente);
        return personaMapper.toDto(personaActualizada);
    }



    
    public void eliminarPersona(Long id) {
        Persona personaExistente = personaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con id: " + id));
        personaExistente.setEstado(com.emisora.agenda.enums.EstadoPersona.INACTIVO);
        personaRepo.save(personaExistente);
    }

    private RolInstitucional convertirRolDtoAEntidad(RolInstitucionalDTO rolDto) {
        if (rolDto == null || rolDto.getTipoRol() == null) {
            return null;
        }
        switch (rolDto.getTipoRol().toUpperCase()) {
            case "ESTUDIANTE":
                return rolMapper.dtoToEstudianteRol(rolDto);
            case "DOCENTE":
                return rolMapper.dtoToProfesorRol(rolDto);
            case "INVITADO":
                return rolMapper.dtoToInvitadoRol(rolDto);
            case "FUNCIONARIO":
                return rolMapper.dtoToFuncionarioRol(rolDto);
            default:
                System.err.println("Tipo de rol desconocido durante la conversión de DTO a Entidad: " + rolDto.getTipoRol());
                throw new IllegalArgumentException("Tipo de rol desconocido: " + rolDto.getTipoRol());
        }
    }


    public PersonaDTO obtenerPersonaPorId(Long id) {
        Persona persona = personaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada con id: " + id));
        return personaMapper.toDto(persona);
    }


    public Page<PersonaDTO> getAllActivePersons(int page, int size, String ordenarPor, String direccionOrden, String searchTerm) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direccionOrden), ordenarPor));
        Page<Persona> personasPage;

    if (searchTerm != null && !searchTerm.trim().isEmpty()) {
        personasPage = personaRepo.findByEstadoAndTerminoBusqueda(EstadoPersona.ACTIVO, searchTerm, pageable);
    } else {
        personasPage = personaRepo.findByEstado(EstadoPersona.ACTIVO, pageable); 
    }
        return personasPage.map(personaMapper::toDto);
    }

}
