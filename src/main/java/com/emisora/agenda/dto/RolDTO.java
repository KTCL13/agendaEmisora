package com.emisora.agenda.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RolDTO {
    
    private Long rolId;
    
    @NotBlank
    private String tipoRol;

    
    private String carrera;
    private String dependencia;
    private String codigoEstudiante;
    private String ocupacion;
    private String facultad;

}
