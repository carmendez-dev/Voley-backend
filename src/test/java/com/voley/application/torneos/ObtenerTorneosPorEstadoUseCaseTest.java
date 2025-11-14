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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para ObtenerTorneosPorEstadoUseCase")
class ObtenerTorneosPorEstadoUseCaseTest {

    @Mock
    private TorneoService torneoService;

    @InjectMocks
    private ObtenerTorneosPorEstadoUseCase obtenerTorneosPorEstadoUseCase;

    @Test
    @DisplayName("Debe obtener torneos activos exitosamente")
    void debeObtenerTorneosActivosExitosamente() {
        // Arrange
        Torneo torneo1 = new Torneo();
        torneo1.setId(1L);
        torneo1.setNombre("Torneo Activo 1");
        torneo1.setEstado(EstadoTorneo.Activo);
        
        Torneo torneo2 = new Torneo();
        torneo2.setId(2L);
        torneo2.setNombre("Torneo Activo 2");
        torneo2.setEstado(EstadoTorneo.Activo);
        
        List<Torneo> torneos = Arrays.asList(torneo1, torneo2);
        when(torneoService.obtenerTorneosPorEstado(EstadoTorneo.Activo)).thenReturn(torneos);

        // Act
        List<Torneo> resultado = obtenerTorneosPorEstadoUseCase.ejecutar(EstadoTorneo.Activo);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        resultado.forEach(t -> assertEquals(EstadoTorneo.Activo, t.getEstado()));
        verify(torneoService, times(1)).obtenerTorneosPorEstado(EstadoTorneo.Activo);
    }

    @Test
    @DisplayName("Debe obtener torneos pendientes exitosamente")
    void debeObtenerTorneosPendientesExitosamente() {
        // Arrange
        Torneo torneo = new Torneo();
        torneo.setId(1L);
        torneo.setNombre("Torneo Pendiente");
        torneo.setEstado(EstadoTorneo.Pendiente);
        
        when(torneoService.obtenerTorneosPorEstado(EstadoTorneo.Pendiente)).thenReturn(List.of(torneo));

        // Act
        List<Torneo> resultado = obtenerTorneosPorEstadoUseCase.ejecutar(EstadoTorneo.Pendiente);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(EstadoTorneo.Pendiente, resultado.get(0).getEstado());
        verify(torneoService, times(1)).obtenerTorneosPorEstado(EstadoTorneo.Pendiente);
    }

    @Test
    @DisplayName("Debe obtener torneos finalizados exitosamente")
    void debeObtenerTorneosFinalizadosExitosamente() {
        // Arrange
        Torneo torneo = new Torneo();
        torneo.setId(1L);
        torneo.setNombre("Torneo Finalizado");
        torneo.setEstado(EstadoTorneo.Finalizado);
        
        when(torneoService.obtenerTorneosPorEstado(EstadoTorneo.Finalizado)).thenReturn(List.of(torneo));

        // Act
        List<Torneo> resultado = obtenerTorneosPorEstadoUseCase.ejecutar(EstadoTorneo.Finalizado);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(EstadoTorneo.Finalizado, resultado.get(0).getEstado());
        verify(torneoService, times(1)).obtenerTorneosPorEstado(EstadoTorneo.Finalizado);
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay torneos con ese estado")
    void debeRetornarListaVaciaCuandoNoHayTorneosConEseEstado() {
        // Arrange
        when(torneoService.obtenerTorneosPorEstado(EstadoTorneo.Finalizado)).thenReturn(List.of());

        // Act
        List<Torneo> resultado = obtenerTorneosPorEstadoUseCase.ejecutar(EstadoTorneo.Finalizado);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(torneoService, times(1)).obtenerTorneosPorEstado(EstadoTorneo.Finalizado);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el estado es nulo")
    void debeLanzarExcepcionCuandoEstadoEsNulo() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> obtenerTorneosPorEstadoUseCase.ejecutar(null)
        );
        
        assertEquals("El estado del torneo no puede ser nulo", exception.getMessage());
        verify(torneoService, never()).obtenerTorneosPorEstado(any());
    }
}
