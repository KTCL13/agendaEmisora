package com.emisora.agenda.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.emisora.agenda.enums.EstadoPersona;
import com.emisora.agenda.model.User;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    
    @Query("SELECT u FROM users u WHERE" +
    " (LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
     "  LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
    "  LOWER(u.id) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<User> findByTerminoBusqueda(String searchTerm, Pageable pageable);

    Page<User> findAll(EstadoPersona estado, Pageable pageable);
}
