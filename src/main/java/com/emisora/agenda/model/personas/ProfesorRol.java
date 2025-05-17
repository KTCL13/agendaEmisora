package com.emisora.agenda.model.personas;

import com.emisora.agenda.enums.CarreraEnum;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@DiscriminatorValue("PROFESOR")
@EqualsAndHashCode(callSuper = true)
public class ProfesorRol extends Rol {

    private String facultad;
    @Enumerated(EnumType.STRING) 
    private CarreraEnum carrera;

}
