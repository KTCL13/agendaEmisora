package com.emisora.agenda.service;

import com.emisora.agenda.dto.ReporteDTO;
import com.emisora.agenda.model.Programa;
import com.emisora.agenda.reports.ReporteCancionesPorPrograma;
import com.emisora.agenda.repository.ProgramaRepository;
import com.emisora.agenda.reports.EstrategiaDeReporte;
import com.emisora.agenda.reports.GeneradorDeReportes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private ProgramaRepository programaRepository;

    public ReporteDTO generarReporteCancionesPorPrograma(Long programaId) {
        Programa programa = programaRepository.findById(programaId)
                .orElseThrow(() -> new RuntimeException("Programa no encontrado"));

        EstrategiaDeReporte estrategia = new ReporteCancionesPorPrograma();
        GeneradorDeReportes generador = new GeneradorDeReportes(estrategia);

        Map<String, Object> parametros = Map.of("programa", programa);
        return generador.generarReporte(parametros);
    }
}
