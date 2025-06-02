package com.emisora.agenda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpisodioReporteDto {
    private String nombre;
    private String descripcion;
    private String programa;
    private String duracion;
    private String tipoRelacion; // "Locutor", "Productor", "Invitado"
}
