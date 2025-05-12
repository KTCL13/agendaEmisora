package com.emisora.agenda.model;

import jakarta.persistence.Entity;

import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "invitados")
public class Invitado extends Persona {

    private String ocupación;

}