package com.emisora.agenda.model.personas;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("INVITADO")
public class InvitadoRol extends RolInstitucional {

    
}