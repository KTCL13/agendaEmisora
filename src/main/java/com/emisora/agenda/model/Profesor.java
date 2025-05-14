package com.emisora.agenda.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name = "profesores")
public class Profesor extends Persona {

    private String facultad;
    @Enumerated(EnumType.STRING) 
    private CarreraEnum carrera;
    private String codigoUniversidad;

}
