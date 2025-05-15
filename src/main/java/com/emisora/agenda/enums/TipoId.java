package com.emisora.agenda.enums;

public enum TipoId {

    CEDULA("Cédula"),
    PASAPORTE("Pasaporte"),
    TARJETA_IDENTIDAD("Tarjeta de Identidad"),
    CEDULA_EXTRANJERA("Cédula Extranjera");

    private final String descripcion;

    TipoId(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    String toUpperCase() {
        return this.name().toUpperCase();
    }
}
