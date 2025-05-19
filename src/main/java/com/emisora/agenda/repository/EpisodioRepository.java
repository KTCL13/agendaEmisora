package com.emisora.agenda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emisora.agenda.model.Episodio;

@Repository
public interface EpisodioRepository extends JpaRepository<Episodio, Long>{
    
}
