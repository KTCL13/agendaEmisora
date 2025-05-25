package com.emisora.agenda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emisora.agenda.enums.EstadoPersona;
import com.emisora.agenda.model.personas.Persona;


public interface PersonaRepository extends JpaRepository<Persona, Long> {
    
    List<Persona> findByEstado(EstadoPersona estado);
    
}