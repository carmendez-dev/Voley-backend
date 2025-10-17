package com.voley.application.torneos;

import com.voley.domain.Torneo;
import com.voley.domain.EstadoTorneo;
import com.voley.service.TorneoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para cambiar el estado de un torneo
 * Encapsula la lógica de negocio para las transiciones de estado
 */
@Component
public class CambiarEstadoTorneoUseCase {
    
    private final TorneoService torneoService;
    
    @Autowired
    public CambiarEstadoTorneoUseCase(TorneoService torneoService) {
        this.torneoService = torneoService;
    }
    
    /**
     * Ejecuta el caso de uso para cambiar el estado de un torneo
     * 
     * @param id El ID del torneo
     * @param nuevoEstado El nuevo estado del torneo
     * @return El torneo con el estado actualizado
     * @throws IllegalArgumentException Si los parámetros son inválidos
     */
    public Torneo ejecutar(Long id, EstadoTorneo nuevoEstado) {
        validarParametros(id, nuevoEstado);
        return torneoService.cambiarEstadoTorneo(id, nuevoEstado);
    }
    
    private void validarParametros(Long id, EstadoTorneo nuevoEstado) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del torneo no puede ser nulo");
        }
        
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del torneo debe ser mayor a cero");
        }
        
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado no puede ser nulo");
        }
    }
}