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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para BuscarTorneosPorNombreUseCase")
class BuscarTorneosPorNombreUseCaseTest {

    @Mock
    private TorneoService torneoService;

    @InjectMocks
    private BuscarTorneosPorNombreUseCase buscarTorneosPorNombreUseCase;

    @Test
    @DisplayName("Debe buscar torneos por nombre exitosamente")
    void debeBuscarTorneosPorNombreExitosamente() {
        // Arrange
        String nombre = "Verano";
        Torneo torneo1 = new Torneo();
        torneo1.setId(1L);
        torneo1.setNombre("Torneo Verano 2024");
        
        Torneo torneo2 = new Torneo();
        torneo2.setId(2L);
        torneo2.setNombre("Torneo Verano 2025");
        
        List<Torneo> torneos = Arrays.asList(torneo1, torneo2);
        when(torneoService.buscarTorneosPorNombre(nombre)).thenReturn(torneos);

        // Act
        List<Torneo> resultado = buscarTorneosPorNombreUseCase.ejecutar(nombre);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(torneoService, times(1)).buscarTorneosPorNombre(nombre);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el nombre es nulo")
    void debeLanzarExcepcionCuandoNombreEsNulo() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> buscarTorneosPorNombreUseCase.ejecutar(null)
        );
        
        assertEquals("El nombre de búsqueda no puede estar vacío", exception.getMessage());
        verify(torneoService, never()).buscarTorneosPorNombre(anyString());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el nombre está vacío")
    void debeLanzarExcepcionCuandoNombreEstaVacio() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> buscarTorneosPorNombreUseCase.ejecutar("   ")
        );
        
        assertEquals("El nombre de búsqueda no puede estar vacío", exception.getMessage());
        verify(torneoService, never()).buscarTorneosPorNombre(anyString());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el nombre tiene menos de 2 caracteres")
    void debeLanzarExcepcionCuandoNombreTieneMenosDe2Caracteres() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> buscarTorneosPorNombreUseCase.ejecutar("A")
        );
        
        assertEquals("El nombre de búsqueda debe tener al menos 2 caracteres", exception.getMessage());
        verify(torneoService, never()).buscarTorneosPorNombre(anyString());
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay coincidencias")
    void debeRetornarListaVaciaCuandoNoHayCoincidencias() {
        // Arrange
        String nombre = "NoExiste";
        when(torneoService.buscarTorneosPorNombre(nombre)).thenReturn(List.of());

        // Act
        List<Torneo> resultado = buscarTorneosPorNombreUseCase.ejecutar(nombre);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(torneoService, times(1)).buscarTorneosPorNombre(nombre);
    }
}
