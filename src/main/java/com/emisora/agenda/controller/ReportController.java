package com.emisora.agenda.controller;

import com.emisora.agenda.dto.ReporteDTO;
import com.emisora.agenda.service.ReportService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;


    @GetMapping("/canciones/por-programa/{programaId}")
    public ReporteDTO obtenerReporteCancionesPorPrograma(@PathVariable Long programaId) {
        return reportService.generarReporteCancionesPorPrograma(programaId);
    }

    @GetMapping("/episodios/por-persona/{personaId}")
    public ReporteDTO obtenerReporteEpisodiosPorPersona(@PathVariable Long personaId) {
        System.out.println("Obteniendo reporte de episodios por persona con ID: " + personaId);
        return reportService.generarReporteEpisodiosPorPersona(personaId);
    }

    @GetMapping("/canciones/por-programa/excel/{programaId}")
    public ResponseEntity<byte[]> downloadCancionesPorPrograma(@PathVariable Long programaId) throws IOException {
        System.out.println("Descargando reporte de canciones por programa con ID: " + programaId);
        byte[] excelBytes = reportService.generarReporteCancionesPorProgramaExcel(programaId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "reporte_canciones_programa_" + programaId + ".xlsx");

        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/episodios/por-persona/excel/{personaId}")
    public ResponseEntity<byte[]> downloadEpisodiosPorPersona(@PathVariable Long personaId) {
        byte[] excelBytes = reportService.generarReporteEpisodiosPorPersonaExcel(personaId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "reporte_episodios_persona_" + personaId + ".xlsx");

        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }
}
