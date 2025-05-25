package com.emisora.agenda.dto;

import java.time.LocalDate;
import java.util.List;

import com.emisora.agenda.enums.EstadoPersona;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonaDTO {  

    private Long id;

    @NotBlank
    private String nombres;

    @NotBlank
    private String apellidos;

    @NotBlank
    private String telefono;

    @NotBlank
    private String correo;

    @NotBlank
    private String numeroId;

    @NotBlank
    private String tipoId; 

    @Valid
    private List<RolInstitucionalDTO> roles;


    private EstadoPersona estado;

    private LocalDate fechaCreacion;

    private LocalDate fechaModificacion;

}