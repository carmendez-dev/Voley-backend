package com.voley.application.inscripciones;

import com.voley.domain.InscripcionEquipo;
import com.voley.domain.InscripcionEquipo.EstadoInscripcion;
import com.voley.repository.InscripcionEquipoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para cambiar el estado de una inscripción
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Component
public class CambiarEstadoInscripcionUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(CambiarEstadoInscripcionUseCase.class);
    
    private final InscripcionEquipoRepository inscripcionRepository;
    
    @Autowired
    public CambiarEstadoInscripcionUseCase(InscripcionEquipoRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;
    }
    
    /**
     * Ejecuta el caso de uso
     * 
     * @param id ID de la inscripción
     * @param nuevoEstado Nuevo estado
     * @param observaciones Observaciones del cambio
     * @return La inscripción actualizada
     */
    public InscripcionEquipo ejecutar(Long id, EstadoInscripcion nuevoEstado, String observaciones) {
        logger.info("Cambiando estado de inscripción {} a {}", id, nuevoEstado);
        
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID de la inscripción es inválido");
        }
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado es obligatorio");
        }
        
        InscripcionEquipo inscripcion = inscripcionRepository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "No existe la inscripción con ID: " + id));
        
        inscripcion.setEstado(nuevoEstado);
        
        if (observaciones != null && !observaciones.trim().isEmpty()) {
            inscripcion.setObservaciones(observaciones);
        }
        
        InscripcionEquipo actualizada = inscripcionRepository.guardar(inscripcion);
        logger.info("Estado cambiado exitosamente a: {}", nuevoEstado);
        
        return actualizada;
    }
}
