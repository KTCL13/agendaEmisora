package com.emisora.agenda.repository;

import com.emisora.agenda.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PersonaRepository extends JpaRepository<Persona, Long> {}