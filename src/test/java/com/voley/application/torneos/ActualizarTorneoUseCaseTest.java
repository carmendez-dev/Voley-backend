package com.voley.application.torneos;

import com.voley.domain.EstadoTorneo;
import com.voley.domain.Torneo;
import com.voley.service.TorneoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para ActualizarTorneoUseCase")
class ActualizarTorneoUseCaseTest {

    @Mock
    private TorneoService torneoService;

    @InjectMocks
    private ActualizarTorneoUseCase actualizarTorneoUseCase;

    private Torneo torneoActualizado;

    @BeforeEach
    void setUp() {
        torneoActualizado = new Torneo();
        torneoActualizado.setNombre("Torneo Actualizado");
        torneoActualizado.setDescripcion("Nueva descripción");
        torneoActualizado.setFechaInicio(LocalDate.of(2025, 7, 1));
        torneoActualizado.setFechaFin(LocalDate.of(2025, 7, 31));
    }

    @Test
    @DisplayName("Debe actualizar un torneo exitosamente")
    void debeActualizarTorneoExitosamente() {
        // Arrange
        Long id = 1L;
        Torneo torneoGuardado = new Torneo();
        torneoGuardado.setId(id);
        torneoGuardado.setNombre(torneoActualizado.getNombre());
        when(torneoService.actualizarTorneo(eq(id), any(Torneo.class))).thenReturn(torneoGuardado);

        // Act
        Torneo resultado = actualizarTorneoUseCase.ejecutar(id, torneoActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(torneoService, times(1)).actualizarTorneo(eq(id), any(Torneo.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el ID es nulo")
    void debeLanzarExcepcionCuandoIdEsNulo() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> actualizarTorneoUseCase.ejecutar(null, torneoActualizado)
        );
        
        assertEquals("El ID del torneo no puede ser nulo", exception.getMessage());
        verify(torneoService, never()).actualizarTorneo(any(), any());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el ID es menor o igual a cero")
    void debeLanzarExcepcionCuandoIdEsMenorOIgualACero() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> actualizarTorneoUseCase.ejecutar(0L, torneoActualizado)
        );
        
        assertEquals("El ID del torneo debe ser mayor a cero", exception.getMessage());
        verify(torneoService, never()).actualizarTorneo(any(), any());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando los datos son nulos")
    void debeLanzarExcepcionCuandoDatosSonNulos() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> actualizarTorneoUseCase.ejecutar(1L, null)
        );
        
        assertEquals("Los datos del torneo no pueden ser nulos", exception.getMessage());
        verify(torneoService, never()).actualizarTorneo(any(), any());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el nombre está vacío")
    void debeLanzarExcepcionCuandoNombreEstaVacio() {
        // Arrange
        torneoActualizado.setNombre("   ");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> actualizarTorneoUseCase.ejecutar(1L, torneoActualizado)
        );
        
        assertEquals("El nombre del torneo no puede estar vacío", exception.getMessage());
        verify(torneoService, never()).actualizarTorneo(any(), any());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando fecha inicio es posterior a fecha fin")
    void debeLanzarExcepcionCuandoFechaInicioEsPosteriorAFechaFin() {
        // Arrange
        torneoActualizado.setFechaInicio(LocalDate.of(2025, 12, 31));
        torneoActualizado.setFechaFin(LocalDate.of(2025, 12, 1));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> actualizarTorneoUseCase.ejecutar(1L, torneoActualizado)
        );
        
        assertEquals("La fecha de inicio no puede ser posterior a la fecha de fin", exception.getMessage());
        verify(torneoService, never()).actualizarTorneo(any(), any());
    }
}
