package com.emisora.agenda.service;

import com.emisora.agenda.dto.PersonaDTO;
import com.emisora.agenda.dto.RolDTO;
import com.emisora.agenda.mapper.PersonaMapper;
import com.emisora.agenda.mapper.RolMapper;
import com.emisora.agenda.model.personas.Persona;
import com.emisora.agenda.model.personas.Rol;
import com.emisora.agenda.repository.PersonaRepository;

import jakarta.transaction.Transactional;


import java.util.ArrayList;


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
}
