package com.emisora.agenda.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emisora.agenda.model.Episodio;
import com.emisora.agenda.model.Programa;

@Repository
public interface EpisodioRepository extends JpaRepository<Episodio, Long>{

    Page<Episodio> findByPrograma(Programa programa, Pageable pageable);

    Page<Episodio> findByProgramaId(Long programa_id, Pageable pageable);
    
}
