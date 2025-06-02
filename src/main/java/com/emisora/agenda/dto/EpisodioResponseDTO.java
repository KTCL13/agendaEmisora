package com.emisora.agenda.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpisodioResponseDTO {
    
    private Long id;

    private String nombre;

    private String descripcion;

    private List<LocalDate> fechasEmitidas;

    private String duracion; 

}
