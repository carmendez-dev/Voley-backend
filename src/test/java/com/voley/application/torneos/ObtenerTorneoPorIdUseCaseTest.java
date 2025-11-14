package com.voley.application.torneos;

import com.voley.domain.EstadoTorneo;
import com.voley.domain.Torneo;
import com.voley.service.TorneoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para ObtenerTorneoPorIdUseCase")
class ObtenerTorneoPorIdUseCaseTest {

    @Mock
    private TorneoService torneoService;

    @InjectMocks
    private ObtenerTorneoPorIdUseCase obtenerTorneoPorIdUseCase;

    @Test
    @DisplayName("Debe obtener un torneo por ID exitosamente")
    void debeObtenerTorneoPorIdExitosamente() {
        // Arrange
        Long id = 1L;
        Torneo torneo = new Torneo();
        torneo.setId(id);
        torneo.setNombre("Torneo Test");
        torneo.setEstado(EstadoTorneo.Activo);
        when(torneoService.obtenerTorneoPorId(id)).thenReturn(Optional.of(torneo));

        // Act
        Optional<Torneo> resultado = obtenerTorneoPorIdUseCase.ejecutar(id);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getId());
        assertEquals("Torneo Test", resultado.get().getNombre());
        verify(torneoService, times(1)).obtenerTorneoPorId(id);
    }

    @Test
    @DisplayName("Debe retornar vacío cuando el torneo no existe")
    void debeRetornarVacioCuandoTorneoNoExiste() {
        // Arrange
        Long id = 999L;
        when(torneoService.obtenerTorneoPorId(id)).thenReturn(Optional.empty());

        // Act
        Optional<Torneo> resultado = obtenerTorneoPorIdUseCase.ejecutar(id);

        // Assert
        assertFalse(resultado.isPresent());
        verify(torneoService, times(1)).obtenerTorneoPorId(id);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el ID es nulo")
    void debeLanzarExcepcionCuandoIdEsNulo() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> obtenerTorneoPorIdUseCase.ejecutar(null)
        );
        
        assertEquals("El ID del torneo no puede ser nulo", exception.getMessage());
        verify(torneoService, never()).obtenerTorneoPorId(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el ID es menor o igual a cero")
    void debeLanzarExcepcionCuandoIdEsMenorOIgualACero() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> obtenerTorneoPorIdUseCase.ejecutar(0L)
        );
        
        assertEquals("El ID del torneo debe ser mayor a cero", exception.getMessage());
        verify(torneoService, never()).obtenerTorneoPorId(any());
    }

    @Test
    @DisplayName("Debe retornar torneo con información completa")
    void debeRetornarTorneoConInformacionCompleta() {
        // Arrange
        Long id = 1L;
        Torneo torneo = new Torneo();
        torneo.setId(id);
        torneo.setNombre("Torneo Test");
        torneo.setDescripcion("Descripción test");
        torneo.setEstado(EstadoTorneo.Activo);
        when(torneoService.obtenerTorneoPorId(id)).thenReturn(Optional.of(torneo));

        // Act
        Optional<Torneo> resultado = obtenerTorneoPorIdUseCase.ejecutar(id);

        // Assert
        assertTrue(resultado.isPresent());
        Torneo torneoResultado = resultado.get();
        assertNotNull(torneoResultado.getId());
        assertNotNull(torneoResultado.getNombre());
        assertNotNull(torneoResultado.getDescripcion());
        assertNotNull(torneoResultado.getEstado());
        verify(torneoService, times(1)).obtenerTorneoPorId(id);
    }
}
