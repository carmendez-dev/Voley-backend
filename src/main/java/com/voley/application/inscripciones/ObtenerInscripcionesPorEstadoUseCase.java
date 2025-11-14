package com.voley.application.inscripciones;

import com.voley.domain.InscripcionEquipo;
import com.voley.domain.InscripcionEquipo.EstadoInscripcion;
import com.voley.repository.InscripcionEquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Caso de uso para obtener inscripciones por estado
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Component
public class ObtenerInscripcionesPorEstadoUseCase {
    
    private final InscripcionEquipoRepository inscripcionRepository;
    
    @Autowired
    public ObtenerInscripcionesPorEstadoUseCase(InscripcionEquipoRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;
    }
    
    /**
     * Ejecuta el caso de uso
     * 
     * @param estado Estado de la inscripción
     * @return Lista de inscripciones con ese estado
     */
    public List<InscripcionEquipo> ejecutar(EstadoInscripcion estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }
        
        return inscripcionRepository.buscarPorEstado(estado);
    }
}
