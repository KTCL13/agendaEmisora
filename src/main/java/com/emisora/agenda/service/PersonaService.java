package com.emisora.agenda.service;

import com.emisora.agenda.dto.PersonaDTO;

import com.emisora.agenda.mapper.PersonaMapper;
import com.emisora.agenda.model.Persona;
import com.emisora.agenda.model.PersonaFactory;

import com.emisora.agenda.repository.PersonaRepository;

import jakarta.validation.Valid;

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

    public PersonaDTO getPersona(Long id) {
        Persona persona = personaRepo.findById(id).orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        return personMapper.toDto(persona);
    }

    public PersonaDTO actualizarPersona(Long id, PersonaDTO personaDTO) {   
        Persona persona = personaRepo.findById(id).orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        persona= personaFactory.crearDesdeDTO(personaDTO);
        Persona updatedPersona = personaRepo.save(persona);
        return personMapper.toDto(updatedPersona);
    }

    public void deletePersona(Long id) {
        Persona persona = personaRepo.findById(id).orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        personaRepo.delete(persona);
    }
}
