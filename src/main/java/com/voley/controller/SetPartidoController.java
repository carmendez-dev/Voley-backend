package com.voley.controller;

import com.voley.dto.SetPartidoDTO;
import com.voley.dto.SetPartidoUpdateDTO;
import com.voley.service.SetPartidoService;
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
 * Controlador REST para SetPartido
 * Gestiona los endpoints del CRUD de sets de partido
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@RestController
@RequestMapping("/api/sets")
public class SetPartidoController {
    
    private static final Logger logger = LoggerFactory.getLogger(SetPartidoController.class);
    
    private final SetPartidoService setPartidoService;
    
    @Autowired
    public SetPartidoController(SetPartidoService setPartidoService) {
        this.setPartidoService = setPartidoService;
    }
    
    /**
     * Crear un nuevo set
     * POST /api/sets
     */
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody SetPartidoDTO dto) {
        try {
            logger.info("Creando nuevo set para partido ID: {}", dto.getIdPartido());
            SetPartidoDTO creado = setPartidoService.crear(dto);
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Set creado exitosamente");
            response.put("set", creado);
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            logger.error("Error de validación al crear set: {}", e.getMessage());
            return ResponseEntity.badRequest().body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            logger.error("Error inesperado al crear set", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error al crear el set"));
        }
    }
    
    /**
     * Obtener todos los sets
     * GET /api/sets
     */
    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        try {
            logger.info("Obteniendo todos los sets");
            List<SetPartidoDTO> sets = setPartidoService.obtenerTodos();
            
            Map<String, Object> response = new HashMap<>();
            response.put("sets", sets);
            response.put("total", sets.size());
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error al obtener sets", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error al obtener los sets"));
        }
    }
    
    /**
     * Obtener un set por ID
     * GET /api/sets/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            logger.info("Obteniendo set con ID: {}", id);
            return setPartidoService.obtenerPorId(id)
                .map(set -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("set", set);
                    response.put("timestamp", LocalDateTime.now());
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("No se encontró el set con ID: " + id)));
        } catch (Exception e) {
            logger.error("Error al obtener set por ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error al obtener el set"));
        }
    }
    
    /**
     * Obtener todos los sets de un partido
     * GET /api/sets/partido/{idPartido}
     */
    @GetMapping("/partido/{idPartido}")
    public ResponseEntity<?> obtenerPorPartido(@PathVariable Long idPartido) {
        try {
            logger.info("Obteniendo sets del partido ID: {}", idPartido);
            List<SetPartidoDTO> sets = setPartidoService.obtenerPorPartido(idPartido);
            
            Map<String, Object> response = new HashMap<>();
            response.put("sets", sets);
            response.put("total", sets.size());
            response.put("idPartido", idPartido);
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error al obtener sets del partido: {}", idPartido, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error al obtener los sets del partido"));
        }
    }
    
    /**
     * Actualizar un set
     * PUT /api/sets/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody SetPartidoUpdateDTO dto) {
        try {
            logger.info("Actualizando set con ID: {}", id);
            SetPartidoDTO actualizado = setPartidoService.actualizar(id, dto);
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Set actualizado exitosamente");
            response.put("set", actualizado);
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("Error de validación al actualizar set: {}", e.getMessage());
            return ResponseEntity.badRequest().body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar set", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error al actualizar el set"));
        }
    }
    
    /**
     * Eliminar un set
     * DELETE /api/sets/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            logger.info("Eliminando set con ID: {}", id);
            setPartidoService.eliminar(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Set eliminado exitosamente");
            response.put("id", id);
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("Error al eliminar set: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            logger.error("Error inesperado al eliminar set", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error al eliminar el set"));
        }
    }
    
    /**
     * Método auxiliar para crear respuestas de error
     */
    private Map<String, Object> crearRespuestaError(String mensaje) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", mensaje);
        response.put("timestamp", LocalDateTime.now());
        return response;
    }
}
