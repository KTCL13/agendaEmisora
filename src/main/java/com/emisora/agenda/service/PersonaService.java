package com.emisora.agenda.service;

import com.emisora.agenda.dto.PersonaDTO;
import com.emisora.agenda.dto.RolDTO;
import com.emisora.agenda.model.Persona;
import com.emisora.agenda.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    public PersonaDTO crearPersona(PersonaDTO personaDTO) {
        Persona persona = convertirAPersona(personaDTO);
        Persona nuevaPersona = personaRepository.save(persona);
        return convertirADTO(nuevaPersona);
    }

    public List<PersonaDTO> obtenerTodasLasPersonas() {
        return personaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private Persona convertirAPersona(PersonaDTO dto) {
        Persona persona = new Persona();
        persona.setNombre(dto.getNombre());
        persona.setCorreo(dto.getCorreo());

        List<String> roles = dto.getRoles().stream()
                .map(RolDTO::getTipo)
                .collect(Collectors.toList());

        persona.setRoles(roles);
        return persona;
    }

    private PersonaDTO convertirADTO(Persona persona) {
        List<RolDTO> rolDTOs = persona.getRoles().stream()
                .map(r -> new RolDTO(r))
                .collect(Collectors.toList());

        return new PersonaDTO(persona.getNombre(), persona.getCorreo(), rolDTOs);
    }
}
