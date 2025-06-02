package com.emisora.agenda.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.emisora.agenda.model.User;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    
    @Query("SELECT u FROM User u WHERE" +
    " (LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
    "  LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) )")
    Page<User> findByTerminoBusqueda(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    Page<User> findAll(Pageable pageable);
}
