package com.emisora.agenda.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.emisora.agenda.enums.EstadoPersona;
import com.emisora.agenda.model.personas.Persona;


public interface PersonaRepository extends JpaRepository<Persona, Long> {
    
    List<Persona> findByEstado(EstadoPersona estado);

    Page<Persona> findByEstado(EstadoPersona estado, Pageable pageable);

    @Query("SELECT p FROM Persona p WHERE" +
    " (LOWER(p.nombresPersona) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
     "  LOWER(p.apellidosPersona) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
    "  LOWER(p.numeroId) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Persona> findByEstadoAndTerminoBusqueda( EstadoPersona estadoPersona, @Param("searchTerm") String searchTerm, Pageable pageable);
    
}