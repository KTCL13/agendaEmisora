package com.emisora.agenda.service;

import com.emisora.agenda.dto.PersonaDTO;
import com.emisora.agenda.dto.RolDTO;
import com.emisora.agenda.exceptions.ResourceNotFoundException;
import com.emisora.agenda.mapper.PersonaMapper;
import com.emisora.agenda.mapper.RolMapper;
import com.emisora.agenda.model.personas.Persona;
import com.emisora.agenda.model.personas.Rol;
import com.emisora.agenda.repository.PersonaRepository;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;


@Service
public class PersonaService {

    private final PersonaRepository personaRepo;

    private final PersonaMapper personaMapper;
    private final RolMapper rolMapper;

    public PersonaService(PersonaRepository personaRepo, PersonaMapper personaMapper, RolMapper rolMapper) {
        this.personaRepo = personaRepo;
        this.personaMapper = personaMapper;
        this.rolMapper = rolMapper;
    }


    @Transactional
    public PersonaDTO crearPersonaConRoles(PersonaDTO requestDTO) {
        
        Persona persona = personaMapper.dtoToPersona(requestDTO);
        
        if (persona.getRoles() == null) {
            persona.setRoles(new ArrayList<>());
        }
        if (requestDTO.getRoles() != null) {
            for (RolDTO rolDto : requestDTO.getRoles()) {
                Rol rolEntity = null;

                switch (rolDto.getTipoRol().toUpperCase()) {
                    case "ESTUDIANTE":
                        rolEntity = rolMapper.dtoToEstudianteRol(rolDto);
                        break;
                    case "PROFESOR":
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
        List<Persona> personas = personaRepo.findAll();
        return personaMapper.toDtoList(personas);
    }


       @Transactional
    public PersonaDTO actualizarPersona(Long personaId, PersonaDTO personaDTO) {
   
        Persona personaExistente = personaRepo.findById(personaId)
                .orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada con id: " + personaId));

        personaExistente.setNombres(personaDTO.getNombres());
        personaExistente.setApellidos(personaDTO.getApellidos());
        personaExistente.setTelefono(personaDTO.getTelefono());
        personaExistente.setCorreo(personaDTO.getCorreo());
        personaExistente.setNumeroId(personaDTO.getNumeroId());
        if (personaDTO.getTipoId() != null) {
            try {

                personaExistente.setTipoId(com.emisora.agenda.enums.TipoId.valueOf(personaDTO.getTipoId().toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.err.println("TipoId inválido proporcionado: " + personaDTO.getTipoId());
            }
        }
        if (personaExistente.getRoles() == null) {
            personaExistente.setRoles(new ArrayList<>());
        }
      
        List<Rol> rolesAntiguos = new ArrayList<>(personaExistente.getRoles());
        for(Rol rolAntiguo : rolesAntiguos){
            personaExistente.removeRol(rolAntiguo); 
        }

        if (personaDTO.getRoles() != null) {
            for (RolDTO rolDto : personaDTO.getRoles()) {
     
                Rol nuevoRolEntity = convertirRolDtoAEntidad(rolDto);
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
        personaRepo.delete(personaExistente);
    }

    private Rol convertirRolDtoAEntidad(RolDTO rolDto) {
        if (rolDto == null || rolDto.getTipoRol() == null) {
            return null;
        }
        switch (rolDto.getTipoRol().toUpperCase()) {
            case "ESTUDIANTE":
                return rolMapper.dtoToEstudianteRol(rolDto);
            case "PROFESOR":
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

}
