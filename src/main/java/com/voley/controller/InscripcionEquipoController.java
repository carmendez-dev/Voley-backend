package com.voley.controller;

import com.voley.domain.InscripcionEquipo.EstadoInscripcion;
import com.voley.dto.ActualizarInscripcionDTO;
import com.voley.dto.InscripcionEquipoDTO;
import com.voley.service.InscripcionEquipoService;
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
 * Controlador REST para InscripcionEquipo
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionEquipoController {
    
    private static final Logger logger = LoggerFactory.getLogger(InscripcionEquipoController.class);
    
    private final InscripcionEquipoService inscripcionService;
    
    @Autowired
    public InscripcionEquipoController(InscripcionEquipoService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }
    
    /**
     * POST /api/inscripciones - Crear nueva inscripción
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearInscripcion(@Valid @RequestBody InscripcionEquipoDTO dto) {
        try {
            logger.info("📝 Creando inscripción: torneoCategoria={}, equipo={}", 
                       dto.getIdTorneoCategoria(), dto.getIdEquipo());
            
            InscripcionEquipoDTO inscripcion = inscripcionService.crearInscripcion(dto);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(crearRespuestaExitosa("Inscripción creada exitosamente", inscripcion));
            
        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Error de validación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            logger.error("❌ Error al crear inscripción: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * GET /api/inscripciones - Obtener todas las inscripciones
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodas(
            @RequestParam(required = false) String estado) {
        try {
            logger.info("📋 Obteniendo inscripciones - estado: {}", estado);
            
            List<InscripcionEquipoDTO> inscripciones;
            
            if (estado != null) {
                EstadoInscripcion estadoEnum = EstadoInscripcion.valueOf(estado);
                inscripciones = inscripcionService.obtenerPorEstado(estadoEnum);
            } else {
                inscripciones = inscripcionService.obtenerTodas();
            }
            
            return ResponseEntity.ok(crearRespuestaExitosa(
                "Inscripciones obtenidas exitosamente", 
                inscripciones, 
                inscripciones.size()));
            
        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Estado inválido: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(crearRespuestaError("Estado inválido. Use: inscrito, retirado, descalificado"));
        } catch (Exception e) {
            logger.error("❌ Error al obtener inscripciones: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * GET /api/inscripciones/{id} - Obtener inscripción por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable Long id) {
        try {
            logger.info("🔍 Obteniendo inscripción ID: {}", id);
            
            return inscripcionService.obtenerPorId(id)
                .map(inscripcion -> ResponseEntity.ok(
                    crearRespuestaExitosa("Inscripción encontrada", inscripcion)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Inscripción no encontrada con ID: " + id)));
            
        } catch (Exception e) {
            logger.error("❌ Error al obtener inscripción: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * GET /api/torneos/{idTorneo}/categorias/{idCategoria}/equipos
     * Obtener inscripciones por torneo y categoría
     */
    @GetMapping("/torneos/{idTorneo}/categorias/{idCategoria}/equipos")
    public ResponseEntity<Map<String, Object>> obtenerPorTorneoYCategoria(
            @PathVariable Long idTorneo,
            @PathVariable Long idCategoria) {
        try {
            logger.info("📋 Obteniendo inscripciones: torneo={}, categoria={}", 
                       idTorneo, idCategoria);
            
            List<InscripcionEquipoDTO> inscripciones = 
                inscripcionService.obtenerPorTorneoYCategoria(idTorneo, idCategoria);
            
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
     * GET /api/inscripciones/equipos/{idEquipo} - Obtener inscripciones por equipo
     */
    @GetMapping("/equipos/{idEquipo}")
    public ResponseEntity<Map<String, Object>> obtenerPorEquipo(@PathVariable Long idEquipo) {
        try {
            logger.info("📋 Obteniendo inscripciones del equipo: {}", idEquipo);
            
            List<InscripcionEquipoDTO> inscripciones = 
                inscripcionService.obtenerPorEquipo(idEquipo);
            
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
     * PUT /api/inscripciones/{id} - Actualizar inscripción (estado y observaciones)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarInscripcion(
            @PathVariable Long id,
            @RequestBody ActualizarInscripcionDTO dto) {
        try {
            logger.info("✏️ Actualizando inscripción ID: {} - estado: {}", id, dto.getEstado());
            
            InscripcionEquipoDTO actualizada = inscripcionService.cambiarEstado(
                id, 
                dto.getEstado(), 
                dto.getObservaciones()
            );
            
            return ResponseEntity.ok(crearRespuestaExitosa(
                "Inscripción actualizada exitosamente", 
                actualizada));
            
        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Error de validación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            logger.error("❌ Error al actualizar inscripción: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * PUT /api/inscripciones/{id}/estado - Cambiar estado de inscripción
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<Map<String, Object>> cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            String estadoStr = request.get("estado");
            String observaciones = request.get("observaciones");
            
            logger.info("🔄 Cambiando estado de inscripción {} a {}", id, estadoStr);
            
            EstadoInscripcion estado = EstadoInscripcion.valueOf(estadoStr);
            InscripcionEquipoDTO actualizada = 
                inscripcionService.cambiarEstado(id, estado, observaciones);
            
            return ResponseEntity.ok(crearRespuestaExitosa(
                "Estado actualizado exitosamente", 
                actualizada));
            
        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            logger.error("❌ Error al cambiar estado: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * DELETE /api/inscripciones/{id} - Eliminar inscripción
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarInscripcion(@PathVariable Long id) {
        try {
            logger.info("🗑️ Eliminando inscripción ID: {}", id);
            
            inscripcionService.eliminarInscripcion(id);
            
            return ResponseEntity.ok(crearRespuestaExitosa(
                "Inscripción eliminada exitosamente", 
                null));
            
        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            logger.error("❌ Error al eliminar inscripción: ", e);
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
