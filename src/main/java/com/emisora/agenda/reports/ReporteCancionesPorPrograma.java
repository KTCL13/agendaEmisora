package com.emisora.agenda.reports;

import com.emisora.agenda.dto.ReporteDTO;
import com.emisora.agenda.model.Cancion;
import com.emisora.agenda.model.Programa;

import java.util.List;
import java.util.Map;

public class ReporteCancionesPorPrograma implements EstrategiaDeReporte {

    @Override
    public ReporteDTO generar(Map<String, Object> parametros) {
        Programa programa = (Programa) parametros.get("programa");

        // Extraer todas las canciones de los episodios del programa
        List<Cancion> canciones = programa.getEpisodios().stream()
                .flatMap(episodio -> episodio.getCanciones().stream())
                .distinct()
                .toList();
        return new ReporteDTO("Canciones del Programa", canciones);
    }
}
