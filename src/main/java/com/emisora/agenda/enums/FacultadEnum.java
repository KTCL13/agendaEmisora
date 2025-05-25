package com.emisora.agenda.enums;

public enum FacultadEnum {

    FACULTAD_DE_INGENIERIA("FACULTAD DE INGENIERIA"),
    FACULTAD_DE_CIENCIAS_SOCIALES("FACULTAD DE CIENCIAS SOCIALES"),
    FACULTAD_DE_ARTES("FACULTAD DE ARTES"),
    FACULTAD_DE_CIENCIAS_ECONOMICAS("FACULTAD DE CIENCIAS ECONOMICAS"),
    FACULTAD_DE_CIENCIAS_NATURALES("FACULTAD DE CIENCIAS NATURALES"),
    FACULTAD_DE_DERECHO("FACULTAD DE DERECHO"),
    FACULTAD_DE_MEDICINA("FACULTAD DE MEDICINA");

    private final String nombre;

    FacultadEnum(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
