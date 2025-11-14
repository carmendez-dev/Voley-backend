package com.voley.controller;

import com.voley.dto.AccionJuegoDTO;
import com.voley.service.AccionJuegoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de acciones de juego
 */
@RestController
@RequestMapping("/api/acciones-juego")
public class AccionJuegoController {
    
    private static final Logger logger = LoggerFactory.getLogger(AccionJuegoController.class);
    
    @Autowired
    private AccionJuegoService accionJuegoService;
    
    /**
     * Obtiene todas las acciones de juego
     */
    @GetMapping
    public ResponseEntity<List<AccionJuegoDTO>> obtenerTodas() {
        logger.info("GET /api/acciones-juego - Obteniendo todas las acciones");
        List<AccionJuegoDTO> acciones = accionJuegoService.obtenerTodas();
        return ResponseEntity.ok(acciones);
    }
    
    /**
     * Obtiene una acción por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccionJuegoDTO> obtenerPorId(@PathVariable Long id) {
        logger.info("GET /api/acciones-juego/{} - Obteniendo acción", id);
        try {
            AccionJuegoDTO accion = accionJuegoService.obtenerPorId(id);
            return ResponseEntity.ok(accion);
        } catch (RuntimeException e) {
            logger.error("Error al obtener acción: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Obtiene todas las acciones de un set
     */
    @GetMapping("/set/{idSetPartido}")
    public ResponseEntity<List<AccionJuegoDTO>> obtenerPorSet(@PathVariable Long idSetPartido) {
        logger.info("GET /api/acciones-juego/set/{} - Obteniendo acciones del set", idSetPartido);
        List<AccionJuegoDTO> acciones = accionJuegoService.obtenerPorSetPartido(idSetPartido);
        return ResponseEntity.ok(acciones);
    }
    
    /**
     * Crea una nueva acción de juego
     */
    @PostMapping
    public ResponseEntity<AccionJuegoDTO> crear(@RequestBody AccionJuegoDTO dto) {
        logger.info("POST /api/acciones-juego - Creando nueva acción");
        try {
            AccionJuegoDTO creada = accionJuegoService.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (RuntimeException e) {
            logger.error("Error al crear acción: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Actualiza una acción de juego existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccionJuegoDTO> actualizar(@PathVariable Long id, @RequestBody AccionJuegoDTO dto) {
        logger.info("PUT /api/acciones-juego/{} - Actualizando acción", id);
        try {
            AccionJuegoDTO actualizada = accionJuegoService.actualizar(id, dto);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            logger.error("Error al actualizar acción: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Elimina una acción de juego
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("DELETE /api/acciones-juego/{} - Eliminando acción", id);
        try {
            accionJuegoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Error al eliminar acción: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
