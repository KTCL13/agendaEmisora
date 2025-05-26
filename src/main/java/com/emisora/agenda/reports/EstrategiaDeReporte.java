package com.emisora.agenda.reports;

import com.emisora.agenda.dto.ReporteDTO;

import java.util.Map;

public interface EstrategiaDeReporte {
    ReporteDTO generar(Map<String, Object> parametros);
}
