package com.emisora.agenda.model;

import jakarta.persistence.Entity;

import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "estudiantes")
public class Estudiante extends Persona {
 
    private CarreraEnum carrera;
    private String codigoUniversidad;
    private int semestre;

}
