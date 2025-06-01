package com.emisora.agenda.reports;

import com.emisora.agenda.dto.EpisodioReporteDto;

import java.util.List;
import java.util.Map;

public interface EstrategiaDeReporte<T> {
    List<T> generar(Map<String, Object> parametros);
}
