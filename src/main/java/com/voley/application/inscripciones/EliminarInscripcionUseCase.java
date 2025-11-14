package com.voley.application.inscripciones;

import com.voley.repository.InscripcionEquipoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para eliminar una inscripción
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Component
public class EliminarInscripcionUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(EliminarInscripcionUseCase.class);
    
    private final InscripcionEquipoRepository inscripcionRepository;
    
    @Autowired
    public EliminarInscripcionUseCase(InscripcionEquipoRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;
    }
    
    /**
     * Ejecuta el caso de uso
     * 
     * @param id ID de la inscripción a eliminar
     */
    public void ejecutar(Long id) {
        logger.info("Eliminando inscripción ID: {}", id);
        
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID de la inscripción es inválido");
        }
        
        if (!inscripcionRepository.existe(id)) {
            throw new IllegalArgumentException("No existe la inscripción con ID: " + id);
        }
        
        inscripcionRepository.eliminar(id);
        logger.info("Inscripción eliminada exitosamente: {}", id);
    }
}
