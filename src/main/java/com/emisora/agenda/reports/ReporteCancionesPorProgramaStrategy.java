package com.emisora.agenda.reports;

import com.emisora.agenda.dto.CancionReporteDto;
import com.emisora.agenda.model.Programa;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ReporteCancionesPorProgramaStrategy implements EstrategiaDeReporte<CancionReporteDto> {

    @Override
    public List<CancionReporteDto> generar(Map<String, Object> parametros) {
        Programa programa = (Programa) parametros.get("programa");

        // Generar listado de canciones únicas
        return programa.getEpisodios().stream()
                .flatMap(episodio -> episodio.getCanciones().stream())
                .map(c -> new CancionReporteDto(c.getTitulo(), c.getArtista()))
                .distinct()
                .toList();
    }
}
