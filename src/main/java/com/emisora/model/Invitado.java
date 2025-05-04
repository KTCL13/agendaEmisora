package com.emisora.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Invitado implements Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ocupación;

    public Invitado() {}

    public Invitado(String ocupación) {
        this.ocupación = ocupación;
    }

    @Override
    public String getDescripcion() {
        return "Facultad: " + ocupación;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getOcupación() {
        return ocupación;
    }
    public void setOcupación(String ocupación) {
        this.ocupación = ocupación;
    }

}