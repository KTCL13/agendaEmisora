package com.emisora.agenda.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.emisora.agenda.model.Programa;

@Repository
public interface ProgramaRepository extends JpaRepository<Programa, Long> {

    @Query ("SELECT p FROM Programa p WHERE" +
           " (LOWER(p.titulo) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "  LOWER(p.codigo) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Programa> findByTerminoBusqueda(@Param("searchTerm") String searchTerm, Pageable pageable);}
