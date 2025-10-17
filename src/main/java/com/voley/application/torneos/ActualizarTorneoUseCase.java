package com.voley.application.torneos;

import com.voley.domain.Torneo;
import com.voley.service.TorneoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Caso de uso para actualizar un torneo existente
 * Encapsula la lógica de negocio para la actualización de torneos
 */
@Component
public class ActualizarTorneoUseCase {
    
    private final TorneoService torneoService;
    
    @Autowired
    public ActualizarTorneoUseCase(TorneoService torneoService) {
        this.torneoService = torneoService;
    }
    
    /**
     * Ejecuta el caso de uso para actualizar un torneo
     * 
     * @param id El ID del torneo a actualizar
     * @param torneoActualizado Los datos actualizados del torneo
     * @return El torneo actualizado
     * @throws IllegalArgumentException Si los datos son inválidos
     */
    public Torneo ejecutar(Long id, Torneo torneoActualizado) {
        validarId(id);
        validarDatosActualizacion(torneoActualizado);
        asignarValoresActualizacion(torneoActualizado);
        return torneoService.actualizarTorneo(id, torneoActualizado);
    }
    
    private void validarId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del torneo no puede ser nulo");
        }
        
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del torneo debe ser mayor a cero");
        }
    }
    
    private void validarDatosActualizacion(Torneo torneo) {
        if (torneo == null) {
            throw new IllegalArgumentException("Los datos del torneo no pueden ser nulos");
        }
        
        if (torneo.getNombre() != null) {
            if (torneo.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del torneo no puede estar vacío");
            }
            
            if (torneo.getNombre().length() > 100) {
                throw new IllegalArgumentException("El nombre del torneo no puede exceder 100 caracteres");
            }
        }
        
        if (torneo.getFechaInicio() != null && torneo.getFechaFin() != null) {
            if (torneo.getFechaInicio().isAfter(torneo.getFechaFin())) {
                throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
            }
        }
    }
    
    private void asignarValoresActualizacion(Torneo torneo) {
        // No necesitamos asignar fechas de auditoría ya que no las tenemos en la estructura simplificada
    }
}