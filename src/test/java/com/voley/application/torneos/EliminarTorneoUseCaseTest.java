package com.voley.application.torneos;

import com.voley.service.TorneoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para EliminarTorneoUseCase")
class EliminarTorneoUseCaseTest {

    @Mock
    private TorneoService torneoService;

    @InjectMocks
    private EliminarTorneoUseCase eliminarTorneoUseCase;

    @Test
    @DisplayName("Debe eliminar un torneo exitosamente")
    void debeEliminarTorneoExitosamente() {
        // Arrange
        Long id = 1L;
        doNothing().when(torneoService).eliminarTorneo(id);

        // Act & Assert
        assertDoesNotThrow(() -> eliminarTorneoUseCase.ejecutar(id));
        verify(torneoService, times(1)).eliminarTorneo(id);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el ID es nulo")
    void debeLanzarExcepcionCuandoIdEsNulo() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> eliminarTorneoUseCase.ejecutar(null)
        );
        
        assertEquals("El ID del torneo no puede ser nulo", exception.getMessage());
        verify(torneoService, never()).eliminarTorneo(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el ID es menor o igual a cero")
    void debeLanzarExcepcionCuandoIdEsMenorOIgualACero() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> eliminarTorneoUseCase.ejecutar(0L)
        );
        
        assertEquals("El ID del torneo debe ser mayor a cero", exception.getMessage());
        verify(torneoService, never()).eliminarTorneo(any());
    }

    @Test
    @DisplayName("Debe propagar excepción del servicio cuando el torneo no existe")
    void debePropagarExcepcionDelServicioCuandoTorneoNoExiste() {
        // Arrange
        Long id = 999L;
        doThrow(new IllegalArgumentException("Torneo no encontrado"))
            .when(torneoService).eliminarTorneo(id);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> eliminarTorneoUseCase.ejecutar(id)
        );
        
        assertEquals("Torneo no encontrado", exception.getMessage());
        verify(torneoService, times(1)).eliminarTorneo(id);
    }
}
