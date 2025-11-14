package com.voley.application.inscripciones;

import com.voley.domain.InscripcionEquipo;
import com.voley.repository.InscripcionEquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Caso de uso para obtener todas las inscripciones
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Component
public class ObtenerTodasInscripcionesUseCase {
    
    private final InscripcionEquipoRepository inscripcionRepository;
    
    @Autowired
    public ObtenerTodasInscripcionesUseCase(InscripcionEquipoRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;
    }
    
    /**
     * Ejecuta el caso de uso
     * 
     * @return Lista de todas las inscripciones
     */
    public List<InscripcionEquipo> ejecutar() {
        return inscripcionRepository.buscarTodas();
    }
}
