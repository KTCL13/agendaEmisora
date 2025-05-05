package com.emisora.agenda.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emisora.agenda.model.Programa;

@Repository
public interface ProgramaRepository extends JpaRepository<Programa, Long> {}
