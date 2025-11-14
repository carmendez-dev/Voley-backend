package com.voley.application.inscripciones;

import com.voley.domain.InscripcionEquipo;
import com.voley.domain.InscripcionEquipo.EstadoInscripcion;
import com.voley.repository.InscripcionEquipoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para actualizar una inscripción
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Component
public class ActualizarInscripcionUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(ActualizarInscripcionUseCase.class);
    
    private final InscripcionEquipoRepository inscripcionRepository;
    
    @Autowired
    public ActualizarInscripcionUseCase(InscripcionEquipoRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;
    }
    
    /**
     * Ejecuta el caso de uso
     * 
     * @param id ID de la inscripción
     * @param estado Nuevo estado (opcional)
     * @param observaciones Nuevas observaciones (opcional)
     * @return La inscripción actualizada
     */
    public InscripcionEquipo ejecutar(Long id, EstadoInscripcion estado, String observaciones) {
        logger.info("Actualizando inscripción ID: {}", id);
        
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID de la inscripción es inválido");
        }
        
        InscripcionEquipo inscripcion = inscripcionRepository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "No existe la inscripción con ID: " + id));
        
        // Actualizar estado si se proporciona
        if (estado != null) {
            inscripcion.setEstado(estado);
        }
        
        // Actualizar observaciones si se proporcionan
        if (observaciones != null) {
            inscripcion.setObservaciones(observaciones);
        }
        
        InscripcionEquipo actualizada = inscripcionRepository.guardar(inscripcion);
        logger.info("Inscripción actualizada exitosamente: {}", id);
        
        return actualizada;
    }
}
