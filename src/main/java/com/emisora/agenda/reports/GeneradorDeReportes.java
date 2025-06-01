package com.emisora.agenda.reports;

import com.emisora.agenda.dto.ReporteDTO;

import java.util.Map;

public class GeneradorDeReportes {

    private EstrategiaDeReporte estrategiaActual;

    public GeneradorDeReportes(EstrategiaDeReporte estrategiaInicial) {
        this.estrategiaActual = estrategiaInicial;
    }

    public void setEstrategia(EstrategiaDeReporte nuevaEstrategia) {
        this.estrategiaActual = nuevaEstrategia;
    }

    public ReporteDTO generarReporte(Map<String, Object> parametros) {
        return (ReporteDTO) estrategiaActual.generar(parametros);
    }
}
