package com.emisora.agenda.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpisodioDTO {

    private Long id;

    @NotBlank(message = "El nombre del episodio no puede estar vacío.")
    @Size(max = 255, message = "El nombre del episodio no puede exceder los 255 caracteres.")
    private String nombre;

    private String descripcion;

    @NotEmpty(message = "Debe proporcionar al menos una fecha de emisión.")
    private List<LocalDate> fechasEmitidas;

    @NotBlank(message = "La duración no puede estar vacía.")
    private String duracion; // e.g., "01:30:00"

    @NotNull(message = "El ID del productor es obligatorio.")
    private Long productorId;
    private String productorNombre; // Opcional, para mostrar en respuestas

    @NotNull(message = "El ID del locutor es obligatorio.")
    private Long locutorId;
    private String locutorNombre; // Opcional

    @NotNull(message = "El ID del programa es obligatorio.")
    private Long programaId;
    private String programaNombre; // Opcional

    private Set<Long> invitadosIds;
    private Set<Long> cancionIds;
    private Set<Long> referenciaIds;

    
    private Set<String> nombresInvitados;
    private Set<String> titulosCanciones;
    private Set<String> descripcionesReferencias;
}