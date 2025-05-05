package com.emisora.agenda.repository;

import com.emisora.agenda.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findByUsername(String username);
}
