package com.voley.application.torneos;

import com.voley.domain.Torneo;
import com.voley.service.TorneoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Caso de uso para crear un nuevo torneo
 * Encapsula la lógica de negocio para la creación de torneos
 */
@Component
public class CrearTorneoUseCase {
    
    private final TorneoService torneoService;
    
    @Autowired
    public CrearTorneoUseCase(TorneoService torneoService) {
        this.torneoService = torneoService;
    }
    
    /**
     * Ejecuta el caso de uso para crear un torneo
     * 
     * @param torneo El torneo a crear
     * @return El torneo creado
     * @throws IllegalArgumentException Si los datos del torneo son inválidos
     */
    public Torneo ejecutar(Torneo torneo) {
        validarTorneo(torneo);
        asignarValoresAutomaticos(torneo);
        return torneoService.crearTorneo(torneo);
    }
    
    private void validarTorneo(Torneo torneo) {
        if (torneo == null) {
            throw new IllegalArgumentException("El torneo no puede ser nulo");
        }
        
        if (torneo.getNombre() == null || torneo.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del torneo es obligatorio");
        }
        
        if (torneo.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre del torneo no puede exceder 100 caracteres");
        }
        
        if (torneo.getFechaInicio() != null && torneo.getFechaFin() != null) {
            if (torneo.getFechaInicio().isAfter(torneo.getFechaFin())) {
                throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
            }
        }
    }
    
    private void asignarValoresAutomaticos(Torneo torneo) {
        // Los valores por defecto ya están en la entidad
    }
}