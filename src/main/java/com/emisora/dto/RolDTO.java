package com.emisora.dto;

public class RolDTO {
    private String tipo;

    public RolDTO(String tipo) {
        this.tipo = tipo;
    }

    public RolDTO() {}

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
