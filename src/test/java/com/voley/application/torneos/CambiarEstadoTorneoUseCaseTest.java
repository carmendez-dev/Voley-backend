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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para CambiarEstadoTorneoUseCase")
class CambiarEstadoTorneoUseCaseTest {

    @Mock
    private TorneoService torneoService;

    @InjectMocks
    private CambiarEstadoTorneoUseCase cambiarEstadoTorneoUseCase;

    @Test
    @DisplayName("Debe cambiar el estado del torneo exitosamente")
    void debeCambiarEstadoExitosamente() {
        // Arrange
        Long id = 1L;
        EstadoTorneo nuevoEstado = EstadoTorneo.Activo;
        Torneo torneoActualizado = new Torneo();
        torneoActualizado.setId(id);
        torneoActualizado.setEstado(nuevoEstado);
        when(torneoService.cambiarEstadoTorneo(eq(id), eq(nuevoEstado))).thenReturn(torneoActualizado);

        // Act
        Torneo resultado = cambiarEstadoTorneoUseCase.ejecutar(id, nuevoEstado);

        // Assert
        assertNotNull(resultado);
        assertEquals(nuevoEstado, resultado.getEstado());
        verify(torneoService, times(1)).cambiarEstadoTorneo(eq(id), eq(nuevoEstado));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el ID es nulo")
    void debeLanzarExcepcionCuandoIdEsNulo() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> cambiarEstadoTorneoUseCase.ejecutar(null, EstadoTorneo.Activo)
        );
        
        assertEquals("El ID del torneo no puede ser nulo", exception.getMessage());
        verify(torneoService, never()).cambiarEstadoTorneo(any(), any());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el ID es menor o igual a cero")
    void debeLanzarExcepcionCuandoIdEsMenorOIgualACero() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> cambiarEstadoTorneoUseCase.ejecutar(0L, EstadoTorneo.Activo)
        );
        
        assertEquals("El ID del torneo debe ser mayor a cero", exception.getMessage());
        verify(torneoService, never()).cambiarEstadoTorneo(any(), any());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el nuevo estado es nulo")
    void debeLanzarExcepcionCuandoNuevoEstadoEsNulo() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> cambiarEstadoTorneoUseCase.ejecutar(1L, null)
        );
        
        assertEquals("El nuevo estado no puede ser nulo", exception.getMessage());
        verify(torneoService, never()).cambiarEstadoTorneo(any(), any());
    }

    @Test
    @DisplayName("Debe cambiar de Pendiente a Activo")
    void debeCambiarDePendienteAActivo() {
        // Arrange
        Long id = 1L;
        EstadoTorneo nuevoEstado = EstadoTorneo.Activo;
        Torneo torneoActualizado = new Torneo();
        torneoActualizado.setId(id);
        torneoActualizado.setEstado(nuevoEstado);
        when(torneoService.cambiarEstadoTorneo(eq(id), eq(nuevoEstado))).thenReturn(torneoActualizado);

        // Act
        Torneo resultado = cambiarEstadoTorneoUseCase.ejecutar(id, nuevoEstado);

        // Assert
        assertNotNull(resultado);
        assertEquals(EstadoTorneo.Activo, resultado.getEstado());
        verify(torneoService, times(1)).cambiarEstadoTorneo(eq(id), eq(nuevoEstado));
    }

    @Test
    @DisplayName("Debe cambiar de Activo a Finalizado")
    void debeCambiarDeActivoAFinalizado() {
        // Arrange
        Long id = 1L;
        EstadoTorneo nuevoEstado = EstadoTorneo.Finalizado;
        Torneo torneoActualizado = new Torneo();
        torneoActualizado.setId(id);
        torneoActualizado.setEstado(nuevoEstado);
        when(torneoService.cambiarEstadoTorneo(eq(id), eq(nuevoEstado))).thenReturn(torneoActualizado);

        // Act
        Torneo resultado = cambiarEstadoTorneoUseCase.ejecutar(id, nuevoEstado);

        // Assert
        assertNotNull(resultado);
        assertEquals(EstadoTorneo.Finalizado, resultado.getEstado());
        verify(torneoService, times(1)).cambiarEstadoTorneo(eq(id), eq(nuevoEstado));
    }
}
