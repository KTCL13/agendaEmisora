package com.emisora.agenda.service;

import com.emisora.agenda.dto.CancionDTO;
import com.emisora.agenda.dto.CancionReporteDto;
import com.emisora.agenda.dto.EpisodioReporteDto;
import com.emisora.agenda.dto.ReporteDTO;
import com.emisora.agenda.exceptions.ResourceNotFoundException;
import com.emisora.agenda.model.Programa;
import com.emisora.agenda.model.personas.Persona;
import com.emisora.agenda.reports.ExcelExporterEpisodioPorPersona;
import com.emisora.agenda.reports.ExcelExporterCancionPorPrograma;
import com.emisora.agenda.reports.ReporteEpisodiosPorPersona;
import com.emisora.agenda.repository.PersonaRepository;
import com.emisora.agenda.repository.ProgramaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ProgramaRepository programaRepository;
    private final PersonaRepository personaRepository;
    private final ExcelExporterCancionPorPrograma cancionExporter;
    private final ExcelExporterEpisodioPorPersona episodioExporter;
    private final ReporteEpisodiosPorPersona reporteEpisodiosPorPersona;

    public byte[] generarReporteCancionesPorProgramaExcel(Long programaId) {
        Programa programa = programaRepository.findById(programaId)
                .orElseThrow(() -> new ResourceNotFoundException("Programa no encontrado"));

        List<CancionReporteDto> canciones = programa.getEpisodios().stream()
                .flatMap(episodio -> episodio.getCanciones().stream())
                .map(c -> new CancionReporteDto(c.getTitulo(), c.getArtista()))
                .distinct()
                .toList();

        try {
            return cancionExporter.generarExcel(canciones);
        } catch (IOException e) {
            throw new RuntimeException("Error al generar el archivo Excel", e);
        }
    }

    public byte[] generarReporteEpisodiosPorPersonaExcel(Long personaId) {
        Persona persona = personaRepository.findById(personaId)
                .orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada"));

        Map<String, Object> params = new HashMap<>();
        params.put("persona", persona);

        List<EpisodioReporteDto> episodios = reporteEpisodiosPorPersona.generar(params);

        try {
            return episodioExporter.generarExcel(episodios);
        } catch (IOException e) {
            throw new RuntimeException("Error al generar el archivo Excel", e);
        }
    }

    public ReporteDTO generarReporteCancionesPorPrograma(Long programaId) {
        Programa programa = programaRepository.findById(programaId)
                .orElseThrow(() -> new ResourceNotFoundException("Programa no encontrado"));

        List<CancionDTO> canciones = programa.getEpisodios().stream()
                .flatMap(episodio -> episodio.getCanciones().stream())
                .map(c -> new CancionDTO(c.getId(),c.getTitulo(), c.getArtista()))
                .distinct()
                .toList();

        return new ReporteDTO("Canciones del Programa " + programa.getTitulo(), canciones);
    }

    public ReporteDTO generarReporteEpisodiosPorPersona(Long personaId) {
        Persona persona = personaRepository.findById(personaId)
                .orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada"));
        Map<String, Object> params = new HashMap<>();
        params.put("persona", persona);
        List<EpisodioReporteDto> episodios = reporteEpisodiosPorPersona.generar(params);
        return new ReporteDTO("Episodios de " + persona.getNombresPersona(), episodios);
    }

}
