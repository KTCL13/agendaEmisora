package com.emisora.agenda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emisora.agenda.model.Cancion;

import java.util.Optional;

@Repository
public interface CancionRepository extends JpaRepository<Cancion, Long> {
    Optional<Cancion> findByTituloAndArtista(String titulo, String artista);
}
