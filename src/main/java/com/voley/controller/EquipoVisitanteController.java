package com.voley.controller;

import com.voley.dto.EquipoVisitanteDTO;
import com.voley.service.EquipoVisitanteService;
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
 * Controlador REST para EquipoVisitante
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@RestController
@RequestMapping("/api/equipos-visitantes")
public class EquipoVisitanteController {
    
    private static final Logger logger = LoggerFactory.getLogger(EquipoVisitanteController.class);
    
    private final EquipoVisitanteService equipoVisitanteService;
    
    @Autowired
    public EquipoVisitanteController(EquipoVisitanteService equipoVisitanteService) {
        this.equipoVisitanteService = equipoVisitanteService;
    }
    
    /**
     * POST /api/equipos-visitantes - Crear equipo visitante
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody EquipoVisitanteDTO dto) {
        try {
            logger.info("📝 Creando equipo visitante: {}", dto.getNombre());
            
            EquipoVisitanteDTO creado = equipoVisitanteService.crear(dto);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(crearRespuestaExitosa("Equipo visitante creado exitosamente", creado));
            
        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Error de validación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            logger.error("❌ Error al crear equipo visitante: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * GET /api/equipos-visitantes - Obtener todos o buscar por nombre
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodos(
            @RequestParam(required = false) String nombre) {
        try {
            logger.info("📋 Obteniendo equipos visitantes - nombre: {}", nombre);
            
            List<EquipoVisitanteDTO> equipos;
            
            if (nombre != null && !nombre.trim().isEmpty()) {
                equipos = equipoVisitanteService.buscarPorNombre(nombre);
            } else {
                equipos = equipoVisitanteService.obtenerTodos();
            }
            
            return ResponseEntity.ok(crearRespuestaExitosa(
                "Equipos visitantes obtenidos exitosamente", 
                equipos, 
                equipos.size()));
            
        } catch (Exception e) {
            logger.error("❌ Error al obtener equipos visitantes: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * GET /api/equipos-visitantes/{id} - Obtener por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable Long id) {
        try {
            logger.info("🔍 Obteniendo equipo visitante ID: {}", id);
            
            return equipoVisitanteService.obtenerPorId(id)
                .map(equipo -> ResponseEntity.ok(
                    crearRespuestaExitosa("Equipo visitante encontrado", equipo)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Equipo visitante no encontrado con ID: " + id)));
            
        } catch (Exception e) {
            logger.error("❌ Error al obtener equipo visitante: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * PUT /api/equipos-visitantes/{id} - Actualizar
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EquipoVisitanteDTO dto) {
        try {
            logger.info("✏️ Actualizando equipo visitante ID: {}", id);
            
            EquipoVisitanteDTO actualizado = equipoVisitanteService.actualizar(id, dto);
            
            return ResponseEntity.ok(crearRespuestaExitosa(
                "Equipo visitante actualizado exitosamente", 
                actualizado));
            
        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Error de validación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            logger.error("❌ Error al actualizar equipo visitante: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * DELETE /api/equipos-visitantes/{id} - Eliminar
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        try {
            logger.info("🗑️ Eliminando equipo visitante ID: {}", id);
            
            equipoVisitanteService.eliminar(id);
            
            return ResponseEntity.ok(crearRespuestaExitosa(
                "Equipo visitante eliminado exitosamente", 
                null));
            
        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            logger.error("❌ Error al eliminar equipo visitante: ", e);
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
