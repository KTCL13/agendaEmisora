package com.emisora.agenda.service;

import com.emisora.agenda.dto.CancionReporteDto;
import com.emisora.agenda.exceptions.ResourceNotFoundException;
import com.emisora.agenda.model.Programa;
import com.emisora.agenda.reports.ExcelExporterCancionPorPrograma;
import com.emisora.agenda.repository.CancionRepository;
import com.emisora.agenda.repository.ProgramaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final CancionRepository cancionRepository;
    private final ProgramaRepository programaRepository;
    private final ExcelExporterCancionPorPrograma excelExporter;

    public byte[] generarReporteCancionesPorProgramaExcel(Long programaId) {
        Programa programa = programaRepository.findById(programaId)
                .orElseThrow(() -> new ResourceNotFoundException("Programa no encontrado"));

        List<CancionReporteDto> canciones = programa.getEpisodios().stream()
                .flatMap(episodio -> episodio.getCanciones().stream())
                .map(c -> new CancionReporteDto(c.getTitulo(), c.getArtista()))
                .distinct()
                .toList();

        try {
            return excelExporter.generarExcel(canciones);
        } catch (IOException e) {
            throw new RuntimeException("Error al generar el archivo Excel", e);
        }
    }
}
