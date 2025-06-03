package com.emisora.agenda.model.personas;

import com.emisora.agenda.enums.CarreraEnum;
import com.emisora.agenda.enums.FacultadEnum;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@DiscriminatorValue("DOCENTE")
@EqualsAndHashCode(callSuper = true)
public class ProfesorRol extends RolInstitucional {

    private FacultadEnum facultad;
    @Enumerated(EnumType.STRING) 
    private CarreraEnum carrera;

}
