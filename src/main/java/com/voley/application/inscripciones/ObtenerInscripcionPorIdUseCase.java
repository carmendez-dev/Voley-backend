package com.voley.application.inscripciones;

import com.voley.domain.InscripcionEquipo;
import com.voley.repository.InscripcionEquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Caso de uso para obtener una inscripción por ID
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Component
public class ObtenerInscripcionPorIdUseCase {
    
    private final InscripcionEquipoRepository inscripcionRepository;
    
    @Autowired
    public ObtenerInscripcionPorIdUseCase(InscripcionEquipoRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;
    }
    
    /**
     * Ejecuta el caso de uso
     * 
     * @param id ID de la inscripción
     * @return Optional con la inscripción si existe
     */
    public Optional<InscripcionEquipo> ejecutar(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID de la inscripción es inválido");
        }
        
        return inscripcionRepository.buscarPorId(id);
    }
}
