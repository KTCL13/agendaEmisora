package com.emisora.agenda.dto;

import com.emisora.agenda.enums.CarreraEnum;
import com.emisora.agenda.enums.FacultadEnum;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RolInstitucionalDTO {
    
    private Long rolInstitucionalId;
    
    @NotBlank
    private String tipoRol;

    
    private CarreraEnum carrera;
    private String dependencia;
    private String codigoEstudiante;
    private FacultadEnum facultad;

}
