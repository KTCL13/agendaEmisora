package com.emisora.agenda.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.emisora.agenda.model.Cancion;

import java.util.Optional;

@Repository
public interface CancionRepository extends JpaRepository<Cancion, Long> {
    Optional<Cancion> findByTituloAndArtista(String titulo, String artista);

    @Query("SELECT c FROM Cancion c WHERE LOWER(c.titulo) LIKE LOWER(concat('%', :term, '%')) OR LOWER(c.artista) LIKE LOWER(concat('%', :term, '%'))")
    Page<Cancion> findByTituloOrArtistaContainingIgnoreCase(@Param("term") String term, Pageable pageable);
}
