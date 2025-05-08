package com.emisora.agenda.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emisora.agenda.model.User;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

}
