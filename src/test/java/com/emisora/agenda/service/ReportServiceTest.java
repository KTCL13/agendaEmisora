package com.emisora.agenda.service;

import com.emisora.agenda.dto.EpisodioReporteDto;
import com.emisora.agenda.dto.ReporteDTO;
import com.emisora.agenda.enums.TipoId;
import com.emisora.agenda.exceptions.ResourceNotFoundException;
import com.emisora.agenda.model.Cancion;
import com.emisora.agenda.model.Episodio;
import com.emisora.agenda.model.Programa;
import com.emisora.agenda.model.personas.Persona;
import com.emisora.agenda.reports.ExcelExporterCancionPorPrograma;
import com.emisora.agenda.reports.ExcelExporterEpisodioPorPersona;
import com.emisora.agenda.reports.ReporteEpisodiosPorPersona;
import com.emisora.agenda.repository.PersonaRepository;
import com.emisora.agenda.repository.ProgramaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ProgramaRepository programaRepository;

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private ReporteEpisodiosPorPersona reporteEpisodiosPorPersona;

    @Mock
    private ExcelExporterCancionPorPrograma cancionExporter;

    @Mock
    private ExcelExporterEpisodioPorPersona episodioExporter;

    @InjectMocks
    private ReportService reportService;

    private Programa programa;
    private Persona persona;

    @BeforeEach
    void setUp() {
        programa = new Programa();
        programa.setId(1L);
        programa.setTitulo("Mi Programa");
        programa.setEpisodios(new ArrayList<>());

        persona = new Persona();
        persona.setIdPersona(1L);
        persona.setNombresPersona("Carlos Pérez");
        persona.setApellidosPersona("Pérez");
        persona.setCorreo("carlos@example.com");
        persona.setTipoId(TipoId.CEDULA);
    }

    @Test
    void generarReporteCancionesPorPrograma_devuelveListadoCorrecto() {
        // Crear y configurar Programa
        Programa programa = new Programa();
        programa.setId(1L);
        programa.setTitulo("Mi Programa");
        programa.setEpisodios(new ArrayList<>());

        // Crear canciones como Set (correcto para el modelo)
        Set<Cancion> cancionesEpisodio = new HashSet<>();
        cancionesEpisodio.add(new Cancion(1L, "Vivir Mi Vida", "Marc Anthony"));
        cancionesEpisodio.add(new Cancion(2L, "Despacito", "Luis Fonsi"));

        // Simular Episodio con esas canciones
        Episodio episodio = mock(Episodio.class);
        when(episodio.getCanciones()).thenReturn(cancionesEpisodio); // Ahora sí es un Set

        // Agregar el episodio al programa
        programa.getEpisodios().add(episodio);

        // Simular repositorio
        when(programaRepository.findById(1L)).thenReturn(Optional.of(programa));

        // Ejecutar método del servicio
        ReporteDTO result = reportService.generarReporteCancionesPorPrograma(1L);

        assertNotNull(result);
        assertEquals("Canciones del Programa Mi Programa", result.getTitulo());

        // Hacer casting seguro del contenido a List<?>
        assertTrue(result.getContenido() instanceof List);
        List<?> contenido = (List<?>) result.getContenido();

        assertFalse(contenido.isEmpty());
        assertEquals(2, contenido.size());
    }

    // ❌ Test: generarReporteCancionesPorPrograma_lanzaExcepcion_siProgramaNoExiste
    @Test
    void generarReporteCancionesPorPrograma_lanzaResourceNotFoundException_siProgramaNoExiste() {
        when(programaRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            reportService.generarReporteCancionesPorPrograma(999L);
        });

        assertEquals("Programa no encontrado", exception.getMessage());
        verify(programaRepository).findById(999L);
    }

    @Test
    void generarReporteCancionesPorProgramaExcel_devuelveBytes_exitosamente() throws IOException {
        // Crear Programa con episodios simulados
        Programa programa = new Programa();
        programa.setId(1L);
        programa.setTitulo("Mi Programa");
        programa.setEpisodios(new ArrayList<>());

        // Crear Cancion para simular en Episodio
        Cancion cancion1 = new Cancion();
        cancion1.setId(1L);
        cancion1.setTitulo("Vivir Mi Vida");
        cancion1.setArtista("Marc Anthony");

        Cancion cancion2 = new Cancion();
        cancion2.setId(2L);
        cancion2.setTitulo("Despacito");
        cancion2.setArtista("Luis Fonsi");

        // Simular un Episodio con canciones
        Episodio episodio = mock(Episodio.class);
        Set<Cancion> canciones = new HashSet<>(Set.of(cancion1, cancion2));
        when(episodio.getCanciones()).thenReturn(canciones);

        // Agregar el episodio al programa
        programa.getEpisodios().add(episodio);

        // Simular repositorio
        when(programaRepository.findById(1L)).thenReturn(Optional.of(programa));

        // Simular exportador Excel
        when(cancionExporter.generarExcel(anyList())).thenReturn("mocked_excel_bytes".getBytes());

        // Llamar al método del servicio
        byte[] result = reportService.generarReporteCancionesPorProgramaExcel(1L);

        // Validar resultados
        assertNotNull(result);
        assertEquals("mocked_excel_bytes".getBytes().length, result.length);
        verify(cancionExporter).generarExcel(anyList());
    }

    // ❌ Test: generarReporteCancionesPorProgramaExcel_lanzaExcepcion_siProgramaNoExiste
    @Test
    void generarReporteCancionesPorProgramaExcel_lanzaResourceNotFoundException_siProgramaNoExiste() {
        when(programaRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            reportService.generarReporteCancionesPorProgramaExcel(999L);
        });

        assertEquals("Programa no encontrado", exception.getMessage());
        verify(programaRepository).findById(999L);
    }

    @Test
    void generarReporteEpisodiosPorPersona_devuelveListadoConRelaciones() {
        // 1. Simular repositorio
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));

        // 2. Datos esperados
        List<EpisodioReporteDto> episodiosEsperados = Arrays.asList(
                new EpisodioReporteDto("Episodio 1", "Primera emisión", "Radio Show", "30:00", "Locutor"),
                new EpisodioReporteDto("Episodio 2", "Entrevista especial", "Podcast", "45:00", "Invitado")
        );

        // 3. Simular estrategia
        when(reporteEpisodiosPorPersona.generar(anyMap())).thenReturn(episodiosEsperados);

        // 4. Ejecutar método del servicio
        ReporteDTO result = reportService.generarReporteEpisodiosPorPersona(1L);

        // 5. Validar resultados
        assertNotNull(result);
        assertEquals("Episodios de Carlos Pérez", result.getTitulo());

        // Hacer casting seguro del contenido a List<?>
        assertTrue(result.getContenido() instanceof List);
        List<?> contenido = (List<?>) result.getContenido();

        assertFalse(contenido.isEmpty());
        assertEquals(2, contenido.size());
        assertTrue(contenido.toString().contains("Episodio 1"));
        assertTrue(contenido.toString().contains("Episodio 2"));

        verify(personaRepository).findById(1L);
    }

    // ❌ Test: generarReporteEpisodiosPorPersona_lanzaExcepcion_siPersonaNoExiste
    @Test
    void generarReporteEpisodiosPorPersona_lanzaResourceNotFoundException_siPersonaNoExiste() {
        when(personaRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            reportService.generarReporteEpisodiosPorPersona(999L);
        });

        assertEquals("Persona no encontrada", exception.getMessage());
        verify(personaRepository).findById(999L);
    }

    // ✅ Test: generarReporteEpisodiosPorPersonaExcel - éxito
    @Test
    void generarReporteEpisodiosPorPersonaExcel_devuelveBytes_exitosamente() throws IOException {
        List<EpisodioReporteDto> episodios = Arrays.asList(
                new EpisodioReporteDto("Episodio 1", "Primera emisión", "Radio Show", "30:00", "Productor"),
                new EpisodioReporteDto("Episodio 2", "Segunda parte", "Podcast", "45:00", "Invitado")
        );

        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));
        when(reporteEpisodiosPorPersona.generar(anyMap())).thenReturn(episodios);
        when(episodioExporter.generarExcel(episodios)).thenReturn("mocked_excel_bytes".getBytes());

        byte[] result = reportService.generarReporteEpisodiosPorPersonaExcel(1L);

        assertNotNull(result);
        assertTrue(result.length > 0);
        verify(episodioExporter).generarExcel(episodios);
    }

    // ❌ Test: generarReporteEpisodiosPorPersonaExcel_lanzaExcepcion_siPersonaNoExiste
    @Test
    void generarReporteEpisodiosPorPersonaExcel_lanzaResourceNotFoundException_siPersonaNoExiste() {
        when(personaRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            reportService.generarReporteEpisodiosPorPersonaExcel(999L);
        });

        assertEquals("Persona no encontrada", exception.getMessage());
        verify(personaRepository).findById(999L);
    }
}