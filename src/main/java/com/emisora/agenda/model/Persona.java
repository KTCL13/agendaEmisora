package com.emisora.agenda.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "personas")
@Inheritance(strategy = InheritanceType.JOINED) 
public abstract class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String correo;
    private String cedula;

}
