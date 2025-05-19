package com.emisora.agenda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emisora.agenda.model.Cancion;

@Repository
public interface CancionRepository extends JpaRepository<Cancion, Long> {

}
