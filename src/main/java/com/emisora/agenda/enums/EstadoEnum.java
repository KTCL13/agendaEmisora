package com.emisora.agenda.enums;

public enum EstadoEnum {
    ACTIVO("ACTIVO"),
    INACTIVO("INACTIVO");

    private String estado;

    EstadoEnum(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }
}
