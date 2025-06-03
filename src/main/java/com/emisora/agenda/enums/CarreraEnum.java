package com.emisora.agenda.enums;

public enum CarreraEnum {

    INGENIERIA_SISTEMAS("Ingeniería de Sistemas"),
    INGENIERIA_ELECTRICA("Ingeniería Eléctrica"),
    INGENIERIA_ELECTRONICA("Ingeniería Electrónica"),
    INGENIERIA_CIVIL("Ingeniería Civil"),
    INGENIERIA_MECANICA("Ingeniería Mecánica"),
    INGENIERIA_QUIMICA("Ingeniería Química"),
    INGENIERIA_BIOMEDICA("Ingeniería Biomédica"),
    INGENIERIA_DE_ALIMENTOS("Ingeniería de Alimentos"),
    INGENIERIA_DE_TRANSPORTE("Ingeniería de Transporte"),
    INGENIERIA_DE_MINAS("Ingeniería de Minas");

    private final String descripcion;

    CarreraEnum(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    String toUpperCase() {
        return this.name().toUpperCase();
    }
}
