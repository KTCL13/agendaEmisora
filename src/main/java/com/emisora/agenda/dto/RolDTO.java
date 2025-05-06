package com.emisora.agenda.dto;

import jakarta.validation.constraints.NotBlank;

public class RolDTO {
    @NotBlank(message = "El tipo de rol es obligatorio")
    private String tipo;

    public RolDTO() {}

    public RolDTO(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
