package com.emisora.agenda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramaDTO {


    private Long id;
    private String codigo;
    private String categoria;
    private String titulo;
    private String descripcion;
    private String fechaCreacion;

}