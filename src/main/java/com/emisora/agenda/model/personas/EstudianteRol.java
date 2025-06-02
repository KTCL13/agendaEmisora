package com.emisora.agenda.model.personas;

import com.emisora.agenda.enums.CarreraEnum;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("ESTUDIANTE")
@Data
@EqualsAndHashCode(callSuper = true)
public class EstudianteRol extends RolInstitucional {
    
    @Enumerated(EnumType.STRING) 
    private CarreraEnum carrera;
    private String codigoEstudiante;

}
