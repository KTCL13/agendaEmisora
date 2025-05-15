package com.emisora.agenda.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emisora.agenda.model.personas.Persona;


public interface PersonaRepository extends JpaRepository<Persona, Long> {}