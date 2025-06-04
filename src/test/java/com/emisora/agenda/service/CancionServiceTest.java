package com.emisora.agenda.service;

import com.emisora.agenda.dto.CancionDTO;
import com.emisora.agenda.exceptions.AppException;
import com.emisora.agenda.exceptions.ResourceNotFoundException;
import com.emisora.agenda.mapper.CancionMapper;
import com.emisora.agenda.model.Cancion;
import com.emisora.agenda.repository.CancionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancionServiceTest {

    @Mock
    private CancionRepository cancionRepository;

    @Mock
    private CancionMapper cancionMapper;

    @InjectMocks
    private CancionService cancionService;

    private Cancion canción1;
    private Cancion canción2;
    private CancionDTO canciónDto1;
    private CancionDTO canciónDto2;

    @BeforeEach
    void setUp() {
        canción1 = new Cancion();
        canción1.setId(1L);
        canción1.setTitulo("Vivir Mi Vida");
        canción1.setArtista("Marc Anthony");

        canción2 = new Cancion();
        canción2.setId(2L);
        canción2.setTitulo("Despacito");
        canción2.setArtista("Luis Fonsi");

        canciónDto1 = new CancionDTO();
        canciónDto1.setId(1L);
        canciónDto1.setTitulo("Vivir Mi Vida");
        canciónDto1.setArtista("Marc Anthony");

        canciónDto2 = new CancionDTO();
        canciónDto2.setId(2L);
        canciónDto2.setTitulo("Despacito");
        canciónDto2.setArtista("Luis Fonsi");
    }

    @Test
    void crearCancion_Exitosa() {
        when(cancionRepository.findByTituloAndArtista("Vivir Mi Vida", "Marc Anthony")).thenReturn(Optional.empty());

        when(cancionRepository.save(any(Cancion.class))).thenReturn(canción1);

        CancionDTO result = cancionService.crearCancion(canciónDto1);

        assertNotNull(result);
        assertEquals("Vivir Mi Vida", result.getTitulo());

        verify(cancionRepository).findByTituloAndArtista("Vivir Mi Vida", "Marc Anthony");
        verify(cancionRepository).save(any(Cancion.class));
    }

    @Test
    void crearCancion_LanzaAppExceptionSiYaExiste() {
        when(cancionRepository.findByTituloAndArtista("Vivir Mi Vida", "Marc Anthony"))
                .thenReturn(Optional.of(canción1));

        AppException exception = assertThrows(AppException.class, () -> {
            cancionService.crearCancion(canciónDto1);
        });

        assertEquals("Ya existe una canción con ese título y artista", exception.getMessage());
        verify(cancionRepository, never()).save(any());
    }

    @Test
    void obtenerTodasLasCanciones_DevuelveListaNoVacia() {
        List<Cancion> canciones = Arrays.asList(canción1, canción2);
        when(cancionRepository.findAll()).thenReturn(canciones);

        // Simular que cada canción se mapea individualmente
        when(cancionMapper.toDto(canción1)).thenReturn(canciónDto1);
        when(cancionMapper.toDto(canción2)).thenReturn(canciónDto2);

        List<CancionDTO> resultado = cancionService.obtenerTodasLasCanciones();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(cancionRepository).findAll();
    }

    @Test
    void actualizarCancion_ActualizaCorrectamente() {
        // 1. Simular que existe una canción
        when(cancionRepository.findById(1L)).thenReturn(Optional.of(canción1));

        // 2. Configurar mapeo entidad → DTO
        when(cancionMapper.toDto(any(Cancion.class))).thenAnswer(invocation -> {
            Cancion cancion = invocation.getArgument(0);
            return new CancionDTO(
                    cancion.getId(),
                    cancion.getTitulo(),
                    cancion.getArtista()
            );
        });

        // 3. Simular guardado con datos actualizados
        when(cancionRepository.save(any(Cancion.class))).thenAnswer(invocation -> {
            Cancion cancion = invocation.getArgument(0);
            cancion.setId(1L); // Simulamos ID asignado tras guardar
            cancion.setTitulo("Vivir Mi Vida - Actualizado");
            cancion.setArtista("Marc Anthony");
            return cancion;
        });

        // 4. Datos del DTO actualizado
        CancionDTO dtoActualizado = new CancionDTO();
        dtoActualizado.setId(1L);
        dtoActualizado.setTitulo("Vivir Mi Vida - Actualizado");
        dtoActualizado.setArtista("Marc Anthony");

        // 5. Llamar al método del servicio
        CancionDTO result = cancionService.actualizarCancion(1L, dtoActualizado);

        // 6. Validar resultados
        assertNotNull(result);
        assertEquals("Vivir Mi Vida - Actualizado", result.getTitulo());
        assertEquals("Marc Anthony", result.getArtista());

        // 7. Verificar interacciones
        verify(cancionRepository).findById(1L);
        verify(cancionRepository).save(any(Cancion.class));
    }

    @Test
    void actualizarCancion_LanzaResourceNotFoundException() {
        when(cancionRepository.findById(999L)).thenReturn(Optional.empty());

        CancionDTO dto = new CancionDTO();
        dto.setId(999L);
        dto.setTitulo("Canción Inexistente");
        dto.setArtista("Artista Desconocido");

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            cancionService.actualizarCancion(999L, dto);
        });

        assertEquals("Canción no encontrada con ID: 999", exception.getMessage());
        verify(cancionRepository, never()).save(any());
    }

    @Test
    void eliminarCancion_Exito() {
        when(cancionRepository.existsById(1L)).thenReturn(true);
        cancionService.eliminarCancion(1L);
        verify(cancionRepository).deleteById(1L);
    }

    @Test
    void eliminarCancion_LanzaResourceNotFoundException() {
        when(cancionRepository.existsById(999L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            cancionService.eliminarCancion(999L);
        });

        assertEquals("Canción no encontrada con ID: 999", exception.getMessage());
        verify(cancionRepository, never()).deleteById(999L);
    }

    @Test
    void buscarCanciones_DevuelveResultados() {
        Pageable pageable = PageRequest.of(0, 10);
        when(cancionRepository.findByTituloOrArtistaContainingIgnoreCase("Vivir", pageable))
                .thenReturn(new PageImpl<>(Collections.singletonList(canción1)));

        List<CancionDTO> resultados = cancionService.buscarCanciones("Vivir", 10);

        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        verify(cancionRepository).findByTituloOrArtistaContainingIgnoreCase("Vivir", pageable);
    }

    @Test
    void obtenerCancionesPorIds_DevuelveListaFiltrada() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(cancionRepository.findAllById(ids)).thenReturn(Arrays.asList(canción1, canción2));

        List<CancionDTO> resultado = cancionService.obtenerCancionesPorIds(ids);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(cancionRepository).findAllById(ids);
    }

    @Test
    void obtenerCancionesPorIds_devuelveListaVaciaSiIdsEsNuloOEstaVacio() {
        List<CancionDTO> resultado = cancionService.obtenerCancionesPorIds(null);
        assertTrue(resultado.isEmpty());
        verify(cancionRepository, never()).findAllById(anyList());
    }

    @Test
    void validarCancion_NoPermiteDuplicados() {
        when(cancionRepository.findByTituloAndArtista("Despacito", "Luis Fonsi")).thenReturn(Optional.of(canción2));

        AppException exception = assertThrows(AppException.class, () -> {
            cancionService.crearCancion(canciónDto2);
        });

        assertEquals("Ya existe una canción con ese título y artista", exception.getMessage());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }
}
