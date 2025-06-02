package com.emisora.agenda.controller;

import com.emisora.agenda.dto.ReporteDTO;
import com.emisora.agenda.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/canciones/por-programa/{programaId}")
    public ReporteDTO obtenerReporteCancionesPorPrograma(@PathVariable Long programaId) {
        return reportService.generarReporteCancionesPorPrograma(programaId);
    }
}
