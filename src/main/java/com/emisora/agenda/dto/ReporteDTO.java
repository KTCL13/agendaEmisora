package com.emisora.agenda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReporteDTO {
    private String titulo;
    private Object contenido;
}
