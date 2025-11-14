package com.voley.application.inscripciones;

import com.voley.domain.InscripcionEquipo;
import com.voley.repository.InscripcionEquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Caso de uso para obtener inscripciones por torneo y categoría
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Component
public class ObtenerInscripcionesPorTorneoYCategoriaUseCase {
    
    private final InscripcionEquipoRepository inscripcionRepository;
    
    @Autowired
    public ObtenerInscripcionesPorTorneoYCategoriaUseCase(InscripcionEquipoRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;
    }
    
    /**
     * Ejecuta el caso de uso
     * 
     * @param idTorneo ID del torneo
     * @param idCategoria ID de la categoría
     * @return Lista de inscripciones
     */
    public List<InscripcionEquipo> ejecutar(Long idTorneo, Long idCategoria) {
        if (idTorneo == null || idTorneo <= 0) {
            throw new IllegalArgumentException("El ID del torneo es inválido");
        }
        if (idCategoria == null || idCategoria <= 0) {
            throw new IllegalArgumentException("El ID de la categoría es inválido");
        }
        
        return inscripcionRepository.buscarPorTorneoYCategoria(idTorneo, idCategoria);
    }
}
