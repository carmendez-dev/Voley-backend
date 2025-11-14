package com.voley.application.inscripciones;

import com.voley.domain.InscripcionEquipo;
import com.voley.repository.InscripcionEquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Caso de uso para obtener inscripciones por equipo
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Component
public class ObtenerInscripcionesPorEquipoUseCase {
    
    private final InscripcionEquipoRepository inscripcionRepository;
    
    @Autowired
    public ObtenerInscripcionesPorEquipoUseCase(InscripcionEquipoRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;
    }
    
    /**
     * Ejecuta el caso de uso
     * 
     * @param idEquipo ID del equipo
     * @return Lista de inscripciones del equipo
     */
    public List<InscripcionEquipo> ejecutar(Long idEquipo) {
        if (idEquipo == null || idEquipo <= 0) {
            throw new IllegalArgumentException("El ID del equipo es inválido");
        }
        
        return inscripcionRepository.buscarPorEquipo(idEquipo);
    }
}
