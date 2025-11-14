package com.voley.controller;

import com.voley.dto.*;
import com.voley.service.EstadisticasService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para estadísticas del sistema
 */
@RestController
@RequestMapping("/api/estadisticas")
public class EstadisticasController {
    
    private static final Logger logger = LoggerFactory.getLogger(EstadisticasController.class);
    
    @Autowired
    private EstadisticasService estadisticasService;
    
    /**
     * Obtiene estadísticas generales para el dashboard
     */
    @GetMapping("/generales")
    public ResponseEntity<EstadisticasGeneralesDTO> obtenerEstadisticasGenerales() {
        logger.info("GET /api/estadisticas/generales - Obteniendo estadísticas generales");
        try {
            EstadisticasGeneralesDTO stats = estadisticasService.obtenerEstadisticasGenerales();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Error al obtener estadísticas generales: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Obtiene estadísticas completas de un partido
     */
    @GetMapping("/partido/{idPartido}")
    public ResponseEntity<EstadisticasPartidoDTO> obtenerEstadisticasPartido(@PathVariable Long idPartido) {
        logger.info("GET /api/estadisticas/partido/{} - Obteniendo estadísticas del partido", idPartido);
        try {
            EstadisticasPartidoDTO stats = estadisticasService.obtenerEstadisticasPartido(idPartido);
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            logger.error("Error al obtener estadísticas del partido: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error inesperado: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Obtiene estadísticas de un jugador en un partido específico
     */
    @GetMapping("/partido/{idPartido}/jugador/{idRoster}")
    public ResponseEntity<EstadisticasJugadorDTO> obtenerEstadisticasJugador(
            @PathVariable Long idPartido,
            @PathVariable Long idRoster) {
        logger.info("GET /api/estadisticas/partido/{}/jugador/{} - Obteniendo estadísticas del jugador", 
                    idPartido, idRoster);
        try {
            EstadisticasJugadorDTO stats = estadisticasService.obtenerEstadisticasJugador(idPartido, idRoster);
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            logger.error("Error al obtener estadísticas del jugador: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error inesperado: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
