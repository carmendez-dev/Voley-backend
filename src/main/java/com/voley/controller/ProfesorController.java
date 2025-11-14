package com.voley.controller;

import com.voley.dto.CambiarPasswordDTO;
import com.voley.dto.CrearProfesorDTO;
import com.voley.dto.ProfesorDTO;
import com.voley.service.ProfesorService;
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
 * Controlador REST para Profesor
 */
@RestController
@RequestMapping("/api/profesores")
@CrossOrigin(origins = "*")
public class ProfesorController {
    
    private static final Logger logger = LoggerFactory.getLogger(ProfesorController.class);
    
    private final ProfesorService profesorService;
    
    @Autowired
    public ProfesorController(ProfesorService profesorService) {
        this.profesorService = profesorService;
    }
    
    /**
     * POST /api/profesores - Crear profesor
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody CrearProfesorDTO dto) {
        try {
            logger.info("📝 Creando profesor: {} {}", dto.getPrimerNombre(), dto.getPrimerApellido());
            
            ProfesorDTO creado = profesorService.crear(dto);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(crearRespuestaExitosa("Profesor creado exitosamente", creado));
            
        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Error de validación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            logger.error("❌ Error al crear profesor: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * GET /api/profesores - Obtener todos o filtrar por estado
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodos(
            @RequestParam(required = false) String estado) {
        try {
            logger.info("📋 Obteniendo profesores - estado: {}", estado);
            
            List<ProfesorDTO> profesores;
            
            if (estado != null && !estado.trim().isEmpty()) {
                profesores = profesorService.obtenerPorEstado(estado);
            } else {
                profesores = profesorService.obtenerTodos();
            }
            
            return ResponseEntity.ok(crearRespuestaExitosa(
                "Profesores obtenidos exitosamente", 
                profesores, 
                profesores.size()));
            
        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Estado inválido: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(crearRespuestaError("Estado inválido. Use: Activo o Inactivo"));
        } catch (Exception e) {
            logger.error("❌ Error al obtener profesores: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * GET /api/profesores/{id} - Obtener por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable Integer id) {
        try {
            logger.info("🔍 Obteniendo profesor ID: {}", id);
            
            return profesorService.obtenerPorId(id)
                .map(profesor -> ResponseEntity.ok(
                    crearRespuestaExitosa("Profesor encontrado", profesor)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Profesor no encontrado con ID: " + id)));
            
        } catch (Exception e) {
            logger.error("❌ Error al obtener profesor: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * GET /api/profesores/cedula/{cedula} - Obtener por cédula
     */
    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<Map<String, Object>> obtenerPorCedula(@PathVariable String cedula) {
        try {
            logger.info("🔍 Obteniendo profesor por cédula: {}", cedula);
            
            return profesorService.obtenerPorCedula(cedula)
                .map(profesor -> ResponseEntity.ok(
                    crearRespuestaExitosa("Profesor encontrado", profesor)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Profesor no encontrado con cédula: " + cedula)));
            
        } catch (Exception e) {
            logger.error("❌ Error al obtener profesor: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * PUT /api/profesores/{id} - Actualizar
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ProfesorDTO dto) {
        try {
            logger.info("✏️ Actualizando profesor ID: {}", id);
            
            ProfesorDTO actualizado = profesorService.actualizar(id, dto);
            
            return ResponseEntity.ok(crearRespuestaExitosa(
                "Profesor actualizado exitosamente", 
                actualizado));
            
        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Error de validación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            logger.error("❌ Error al actualizar profesor: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * PUT /api/profesores/cedula/{cedula}/password - Cambiar contraseña por cédula
     */
    @PutMapping("/cedula/{cedula}/password")
    public ResponseEntity<Map<String, Object>> cambiarPassword(
            @PathVariable String cedula,
            @Valid @RequestBody CambiarPasswordDTO dto) {
        try {
            logger.info("🔐 Cambiando contraseña para profesor con cédula: {}", cedula);
            
            profesorService.cambiarPassword(cedula, dto);
            
            return ResponseEntity.ok(crearRespuestaExitosa(
                "Contraseña actualizada exitosamente", 
                null));
            
        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            logger.error("❌ Error al cambiar contraseña: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * DELETE /api/profesores/{id} - Eliminar
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Integer id) {
        try {
            logger.info("🗑️ Eliminando profesor ID: {}", id);
            
            profesorService.eliminar(id);
            
            return ResponseEntity.ok(crearRespuestaExitosa(
                "Profesor eliminado exitosamente", 
                null));
            
        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            logger.error("❌ Error al eliminar profesor: ", e);
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
