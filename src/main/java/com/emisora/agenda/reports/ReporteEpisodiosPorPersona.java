package com.emisora.agenda.reports;

import com.emisora.agenda.dto.EpisodioReporteDto;
import com.emisora.agenda.model.Episodio;
import com.emisora.agenda.model.personas.Persona;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ReporteEpisodiosPorPersona implements EstrategiaDeReporte<EpisodioReporteDto> {

    @Override
    public List<EpisodioReporteDto> generar(Map<String, Object> parametros) {
        Persona persona = (Persona) parametros.get("persona");

        Set<EpisodioReporteDto> reporte = new HashSet<>();

        // Como Productor
        if (persona.getProductos() != null) {
            for (Episodio e : persona.getProductos()) {
                reporte.add(convertirADto(e, "Productor"));
            }
        }

        // Como Locutor
        if (persona.getLocuciones() != null) {
            for (Episodio e : persona.getLocuciones()) {
                reporte.add(convertirADto(e, "Locutor"));
            }
        }

        // Como Invitado
        if (persona.getEpisodiosDondeEsInvitado() != null) {
            for (Episodio e : persona.getEpisodiosDondeEsInvitado()) {
                reporte.add(convertirADto(e, "Invitado"));
            }
        }

        return reporte.stream().toList();
    }

    private EpisodioReporteDto convertirADto(Episodio episodio, String tipoRelacion) {
        return new EpisodioReporteDto(
                episodio.getNombre(),
                episodio.getDescripcion(),
                episodio.getPrograma().getTitulo(),
                episodio.getDuracion(),
                tipoRelacion
        );
    }
}
