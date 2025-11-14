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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para CrearTorneoUseCase")
class CrearTorneoUseCaseTest {

    @Mock
    private TorneoService torneoService;

    @InjectMocks
    private CrearTorneoUseCase crearTorneoUseCase;

    private Torneo torneoValido;

    @BeforeEach
    void setUp() {
        torneoValido = new Torneo();
        torneoValido.setNombre("Copa Primavera 2025");
        torneoValido.setDescripcion("Torneo de voleibol");
        torneoValido.setFechaInicio(LocalDate.of(2025, 11, 1));
        torneoValido.setFechaFin(LocalDate.of(2025, 11, 30));
        torneoValido.setEstado(EstadoTorneo.Pendiente);
    }

    @Test
    @DisplayName("Debe crear un torneo válido exitosamente")
    void debeCrearTorneoValidoExitosamente() {
        // Arrange
        Torneo torneoGuardado = new Torneo();
        torneoGuardado.setId(1L);
        torneoGuardado.setNombre(torneoValido.getNombre());
        when(torneoService.crearTorneo(any(Torneo.class))).thenReturn(torneoGuardado);

        // Act
        Torneo resultado = crearTorneoUseCase.ejecutar(torneoValido);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(torneoService, times(1)).crearTorneo(any(Torneo.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el torneo es nulo")
    void debeLanzarExcepcionCuandoTorneoEsNulo() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> crearTorneoUseCase.ejecutar(null)
        );
        
        assertEquals("El torneo no puede ser nulo", exception.getMessage());
        verify(torneoService, never()).crearTorneo(any(Torneo.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el nombre es nulo")
    void debeLanzarExcepcionCuandoNombreEsNulo() {
        // Arrange
        torneoValido.setNombre(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> crearTorneoUseCase.ejecutar(torneoValido)
        );
        
        assertEquals("El nombre del torneo es obligatorio", exception.getMessage());
        verify(torneoService, never()).crearTorneo(any(Torneo.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el nombre está vacío")
    void debeLanzarExcepcionCuandoNombreEstaVacio() {
        // Arrange
        torneoValido.setNombre("   ");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> crearTorneoUseCase.ejecutar(torneoValido)
        );
        
        assertEquals("El nombre del torneo es obligatorio", exception.getMessage());
        verify(torneoService, never()).crearTorneo(any(Torneo.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el nombre excede 100 caracteres")
    void debeLanzarExcepcionCuandoNombreExcedeLimite() {
        // Arrange
        torneoValido.setNombre("A".repeat(101));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> crearTorneoUseCase.ejecutar(torneoValido)
        );
        
        assertEquals("El nombre del torneo no puede exceder 100 caracteres", exception.getMessage());
        verify(torneoService, never()).crearTorneo(any(Torneo.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando fecha inicio es posterior a fecha fin")
    void debeLanzarExcepcionCuandoFechaInicioEsPosteriorAFechaFin() {
        // Arrange
        torneoValido.setFechaInicio(LocalDate.of(2025, 12, 31));
        torneoValido.setFechaFin(LocalDate.of(2025, 12, 1));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> crearTorneoUseCase.ejecutar(torneoValido)
        );
        
        assertEquals("La fecha de inicio no puede ser posterior a la fecha de fin", exception.getMessage());
        verify(torneoService, never()).crearTorneo(any(Torneo.class));
    }
}
