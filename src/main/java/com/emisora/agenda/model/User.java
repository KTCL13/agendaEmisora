package com.emisora.agenda.model;

import java.util.List;

import com.emisora.agenda.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

     @Column(nullable = false, unique = true)
    private String username;

     @Column(nullable = false)
    private String password;

     @Column(unique = true)
     private String email;

    private List<Role> roles;

    private boolean active = true;
}
