package com.emisora.agenda.controller;

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

    @GetMapping("/canciones/por-programa/excel")
    public ResponseEntity<byte[]> downloadExcel(@RequestParam Long programaId) throws IOException {
        byte[] excelBytes = reportService.generarReporteCancionesPorProgramaExcel(programaId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "reporte_canciones_programa_" + programaId + ".xlsx");

        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/episodios/por-persona/excel")
    public ResponseEntity<byte[]> downloadEpisodiosPorPersona(@RequestParam Long personaId) {
        byte[] excelBytes = reportService.generarReporteEpisodiosPorPersonaExcel(personaId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "reporte_episodios_persona_" + personaId + ".xlsx");

        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }
}
