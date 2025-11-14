package com.voley.application.torneos;

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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para ObtenerTodosLosTorneosUseCase")
class ObtenerTodosLosTorneosUseCaseTest {

    @Mock
    private TorneoService torneoService;

    @InjectMocks
    private ObtenerTodosLosTorneosUseCase obtenerTodosLosTorneosUseCase;

    @Test
    @DisplayName("Debe obtener todos los torneos exitosamente")
    void debeObtenerTodosLosTorneosExitosamente() {
        // Arrange
        Torneo torneo1 = new Torneo();
        torneo1.setId(1L);
        torneo1.setNombre("Torneo 1");
        
        Torneo torneo2 = new Torneo();
        torneo2.setId(2L);
        torneo2.setNombre("Torneo 2");
        
        Torneo torneo3 = new Torneo();
        torneo3.setId(3L);
        torneo3.setNombre("Torneo 3");
        
        List<Torneo> torneos = Arrays.asList(torneo1, torneo2, torneo3);
        when(torneoService.obtenerTodosLosTorneos()).thenReturn(torneos);

        // Act
        List<Torneo> resultado = obtenerTodosLosTorneosUseCase.ejecutar();

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        verify(torneoService, times(1)).obtenerTodosLosTorneos();
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay torneos")
    void debeRetornarListaVaciaCuandoNoHayTorneos() {
        // Arrange
        when(torneoService.obtenerTodosLosTorneos()).thenReturn(List.of());

        // Act
        List<Torneo> resultado = obtenerTodosLosTorneosUseCase.ejecutar();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(torneoService, times(1)).obtenerTodosLosTorneos();
    }

    @Test
    @DisplayName("Debe retornar torneos con información completa")
    void debeRetornarTorneosConInformacionCompleta() {
        // Arrange
        Torneo torneo = new Torneo();
        torneo.setId(1L);
        torneo.setNombre("Torneo Test");
        torneo.setDescripcion("Descripción test");
        
        when(torneoService.obtenerTodosLosTorneos()).thenReturn(List.of(torneo));

        // Act
        List<Torneo> resultado = obtenerTodosLosTorneosUseCase.ejecutar();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        Torneo torneoResultado = resultado.get(0);
        assertNotNull(torneoResultado.getId());
        assertNotNull(torneoResultado.getNombre());
        assertNotNull(torneoResultado.getDescripcion());
        verify(torneoService, times(1)).obtenerTodosLosTorneos();
    }
}
