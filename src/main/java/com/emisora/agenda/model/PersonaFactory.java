package com.emisora.agenda.model;

import com.emisora.agenda.dto.PersonaDTO;

public interface PersonaFactory {
    Persona crearDesdeDTO(PersonaDTO dto);
}