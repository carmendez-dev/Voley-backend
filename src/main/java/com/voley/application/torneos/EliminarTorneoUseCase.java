package com.voley.application.torneos;

import com.voley.service.TorneoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para eliminar un torneo
 * Encapsula la lógica de negocio para la eliminación de torneos
 */
@Component
public class EliminarTorneoUseCase {
    
    private final TorneoService torneoService;
    
    @Autowired
    public EliminarTorneoUseCase(TorneoService torneoService) {
        this.torneoService = torneoService;
    }
    
    /**
     * Ejecuta el caso de uso para eliminar un torneo
     * 
     * @param id El ID del torneo a eliminar
     * @throws IllegalArgumentException Si el ID es inválido
     * @throws IllegalStateException Si el torneo no puede ser eliminado
     */
    public void ejecutar(Long id) {
        validarId(id);
        torneoService.eliminarTorneo(id);
    }
    
    private void validarId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del torneo no puede ser nulo");
        }
        
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del torneo debe ser mayor a cero");
        }
    }
}