package com.voley.application.torneos;

import com.voley.domain.Torneo;
import com.voley.domain.EstadoTorneo;
import com.voley.service.TorneoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Caso de uso para obtener torneos por estado
 * Encapsula la lógica de negocio para filtrar torneos por estado
 */
@Component
public class ObtenerTorneosPorEstadoUseCase {
    
    private final TorneoService torneoService;
    
    @Autowired
    public ObtenerTorneosPorEstadoUseCase(TorneoService torneoService) {
        this.torneoService = torneoService;
    }
    
    /**
     * Ejecuta el caso de uso para obtener torneos por estado
     * 
     * @param estado El estado de los torneos a buscar
     * @return Lista de torneos con el estado especificado
     * @throws IllegalArgumentException Si el estado es nulo
     */
    public List<Torneo> ejecutar(EstadoTorneo estado) {
        validarEstado(estado);
        return torneoService.obtenerTorneosPorEstado(estado);
    }
    
    private void validarEstado(EstadoTorneo estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado del torneo no puede ser nulo");
        }
    }
}