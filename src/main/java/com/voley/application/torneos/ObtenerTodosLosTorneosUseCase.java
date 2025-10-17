package com.voley.application.torneos;

import com.voley.domain.Torneo;
import com.voley.service.TorneoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Caso de uso para obtener todos los torneos
 * Encapsula la lógica de negocio para la consulta de todos los torneos
 */
@Component
public class ObtenerTodosLosTorneosUseCase {
    
    private final TorneoService torneoService;
    
    @Autowired
    public ObtenerTodosLosTorneosUseCase(TorneoService torneoService) {
        this.torneoService = torneoService;
    }
    
    /**
     * Ejecuta el caso de uso para obtener todos los torneos
     * 
     * @return Lista de todos los torneos ordenados por fecha de inicio
     */
    public List<Torneo> ejecutar() {
        return torneoService.obtenerTodosLosTorneos();
    }
}