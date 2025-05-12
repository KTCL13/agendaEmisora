package com.emisora.agenda.service;

import com.emisora.agenda.dto.PersonaDTO;

import com.emisora.agenda.mapper.PersonaMapper;
import com.emisora.agenda.model.Persona;
import com.emisora.agenda.model.PersonaFactory;

import com.emisora.agenda.repository.PersonaRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;


@Service
public class PersonaService {

    private final PersonaRepository personaRepo;
    private final PersonaFactory personaFactory;
    private final PersonaMapper personMapper;

    public PersonaService(PersonaRepository personaRepo, PersonaFactory personaFactory, PersonaMapper personMapper) {
        this.personaRepo = personaRepo;
        this.personaFactory = personaFactory;
        this.personMapper = personMapper;
    }

    public PersonaDTO crearPersona(PersonaDTO dto) {
        Persona persona = personaRepo.save(personaFactory.crearDesdeDTO(dto));
        return personMapper.toDto(persona);
    }

    public List<PersonaDTO> getAllPersonas() {
        List<Persona> personas = personaRepo.findAll();
        
        return personas.stream()
                .map(personMapper::toDto).collect(Collectors.toList());
    }
}
