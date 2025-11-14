package com.voley.application.inscripciones;

import com.voley.domain.Equipo;
import com.voley.domain.InscripcionEquipo;
import com.voley.domain.TorneoCategoria;
import com.voley.repository.EquipoRepository;
import com.voley.repository.InscripcionEquipoRepository;
import com.voley.service.TorneoCategoriaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para crear una inscripción de equipo
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Component
public class CrearInscripcionUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(CrearInscripcionUseCase.class);
    
    private final InscripcionEquipoRepository inscripcionRepository;
    private final TorneoCategoriaService torneoCategoriaService;
    private final EquipoRepository equipoRepository;
    
    @Autowired
    public CrearInscripcionUseCase(InscripcionEquipoRepository inscripcionRepository,
                                  TorneoCategoriaService torneoCategoriaService,
                                  EquipoRepository equipoRepository) {
        this.inscripcionRepository = inscripcionRepository;
        this.torneoCategoriaService = torneoCategoriaService;
        this.equipoRepository = equipoRepository;
    }
    
    /**
     * Ejecuta el caso de uso para crear una inscripción
     * 
     * @param idTorneoCategoria ID de la categoría del torneo
     * @param idEquipo ID del equipo
     * @param observaciones Observaciones opcionales
     * @return La inscripción creada
     */
    public InscripcionEquipo ejecutar(Long idTorneoCategoria, Long idEquipo, String observaciones) {
        logger.info("Creando inscripción: torneoCategoria={}, equipo={}", idTorneoCategoria, idEquipo);
        
        // Validar parámetros
        if (idTorneoCategoria == null || idTorneoCategoria <= 0) {
            throw new IllegalArgumentException("El ID de torneo-categoría es inválido");
        }
        if (idEquipo == null || idEquipo <= 0) {
            throw new IllegalArgumentException("El ID del equipo es inválido");
        }
        
        // Verificar que no exista la inscripción
        if (inscripcionRepository.existeInscripcion(idTorneoCategoria, idEquipo)) {
            throw new IllegalArgumentException(
                "El equipo ya está inscrito en esta categoría del torneo");
        }
        
        // Obtener torneo-categoría
        TorneoCategoria torneoCategoria = torneoCategoriaService.obtenerTorneoCategoriaById(idTorneoCategoria)
            .orElseThrow(() -> new IllegalArgumentException(
                "No existe la categoría del torneo con ID: " + idTorneoCategoria));
        
        // Obtener equipo
        Equipo equipo = equipoRepository.buscarPorId(idEquipo)
            .orElseThrow(() -> new IllegalArgumentException(
                "No existe el equipo con ID: " + idEquipo));
        
        // Crear inscripción
        InscripcionEquipo inscripcion = new InscripcionEquipo(torneoCategoria, equipo);
        if (observaciones != null && !observaciones.trim().isEmpty()) {
            inscripcion.setObservaciones(observaciones);
        }
        
        InscripcionEquipo guardada = inscripcionRepository.guardar(inscripcion);
        logger.info("Inscripción creada exitosamente con ID: {}", guardada.getIdInscripcion());
        
        return guardada;
    }
}
