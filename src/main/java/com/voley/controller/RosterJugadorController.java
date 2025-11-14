package com.voley.controller;

import com.voley.dto.RosterJugadorDTO;
import com.voley.service.RosterJugadorService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para RosterJugador
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@RestController
@RequestMapping("/api/roster")
public class RosterJugadorController {
    
    private static final Logger logger = LoggerFactory.getLogger(RosterJugadorController.class);
    
    private final RosterJugadorService rosterService;
    
    @Autowired
    public RosterJugadorController(RosterJugadorService rosterService) {
        this.rosterService = rosterService;
    }
    
    /**
     * POST /api/roster - Agregar jugador al roster
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> agregarJugador(@Valid @RequestBody RosterJugadorDTO dto) {
        try {
            logger.info("📝 Agregando jugador al roster: inscripción={}, usuario={}", 
                       dto.getIdInscripcion(), dto.getIdUsuario());
            
            RosterJugadorDTO roster = rosterService.agregarJugador(dto);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(crearRespuestaExitosa("Jugador agregado al roster exitosamente", roster));
            
        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Error de validación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            logger.error("❌ Error al agregar jugador: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * GET /api/roster/inscripciones/{idInscripcion} - Obtener jugadores de una inscripción
     */
    @GetMapping("/inscripciones/{idInscripcion}")
    public ResponseEntity<Map<String, Object>> obtenerJugadoresPorInscripcion(
            @PathVariable Long idInscripcion) {
        try {
            logger.info("📋 Obteniendo jugadores de inscripción: {}", idInscripcion);
            
            List<RosterJugadorDTO> jugadores = 
                rosterService.obtenerJugadoresPorInscripcion(idInscripcion);
            
            return ResponseEntity.ok(crearRespuestaExitosa(
                "Jugadores obtenidos exitosamente", 
                jugadores, 
                jugadores.size()));
            
        } catch (Exception e) {
            logger.error("❌ Error al obtener jugadores: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * GET /api/roster/usuarios/{idUsuario} - Obtener inscripciones de un jugador
     */
    @GetMapping("/usuarios/{idUsuario}")
    public ResponseEntity<Map<String, Object>> obtenerInscripcionesPorJugador(
            @PathVariable Long idUsuario) {
        try {
            logger.info("📋 Obteniendo inscripciones del jugador: {}", idUsuario);
            
            List<RosterJugadorDTO> inscripciones = 
                rosterService.obtenerInscripcionesPorJugador(idUsuario);
            
            return ResponseEntity.ok(crearRespuestaExitosa(
                "Inscripciones obtenidas exitosamente", 
                inscripciones, 
                inscripciones.size()));
            
        } catch (Exception e) {
            logger.error("❌ Error al obtener inscripciones: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * DELETE /api/roster/{id} - Eliminar jugador del roster
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarJugador(@PathVariable Long id) {
        try {
            logger.info("🗑️ Eliminando jugador del roster ID: {}", id);
            
            rosterService.eliminarJugador(id);
            
            return ResponseEntity.ok(crearRespuestaExitosa(
                "Jugador eliminado del roster exitosamente", 
                null));
            
        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            logger.error("❌ Error al eliminar jugador: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    // Métodos auxiliares
    private Map<String, Object> crearRespuestaExitosa(String mensaje, Object data) {
        return crearRespuestaExitosa(mensaje, data, null);
    }
    
    private Map<String, Object> crearRespuestaExitosa(String mensaje, Object data, Integer total) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", mensaje);
        response.put("timestamp", LocalDateTime.now());
        if (data != null) {
            response.put("data", data);
        }
        if (total != null) {
            response.put("total", total);
        }
        return response;
    }
    
    private Map<String, Object> crearRespuestaError(String mensaje) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", mensaje);
        response.put("timestamp", LocalDateTime.now());
        return response;
    }
}
