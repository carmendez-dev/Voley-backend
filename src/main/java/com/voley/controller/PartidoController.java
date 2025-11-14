package com.voley.controller;

import com.voley.domain.Partido.ResultadoPartido;
import com.voley.dto.PartidoDTO;
import com.voley.dto.PartidoUpdateDTO;
import com.voley.service.PartidoService;
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
 * Controlador REST para Partido
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@RestController
@RequestMapping("/api/partidos")
public class PartidoController {
    
    private static final Logger logger = LoggerFactory.getLogger(PartidoController.class);
    
    private final PartidoService partidoService;
    
    @Autowired
    public PartidoController(PartidoService partidoService) {
        this.partidoService = partidoService;
    }
    
    /**
     * POST /api/partidos - Crear partido
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody PartidoDTO dto) {
        try {
            logger.info("📝 Creando partido");
            
            PartidoDTO creado = partidoService.crear(dto);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(crearRespuestaExitosa("Partido creado exitosamente", creado));
            
        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Error de validación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            logger.error("❌ Error al crear partido: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * GET /api/partidos - Obtener todos o filtrar
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodos(
            @RequestParam(required = false) String resultado) {
        try {
            logger.info("📋 Obteniendo partidos - resultado: {}", resultado);
            
            List<PartidoDTO> partidos;
            
            if (resultado != null) {
                if ("pendientes".equalsIgnoreCase(resultado)) {
                    partidos = partidoService.obtenerPartidosPendientes();
                } else {
                    ResultadoPartido resultadoEnum = ResultadoPartido.valueOf(resultado);
                    partidos = partidoService.obtenerPorResultado(resultadoEnum);
                }
            } else {
                partidos = partidoService.obtenerTodos();
            }
            
            return ResponseEntity.ok(crearRespuestaExitosa(
                "Partidos obtenidos exitosamente", 
                partidos, 
                partidos.size()));
            
        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Resultado inválido: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(crearRespuestaError("Resultado inválido. Use: Pendiente, Ganado, Perdido, Walkover, WalkoverContra o pendientes"));
        } catch (Exception e) {
            logger.error("❌ Error al obtener partidos: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * GET /api/partidos/{id} - Obtener por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable Long id) {
        try {
            logger.info("🔍 Obteniendo partido ID: {}", id);
            
            return partidoService.obtenerPorId(id)
                .map(partido -> ResponseEntity.ok(
                    crearRespuestaExitosa("Partido encontrado", partido)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Partido no encontrado con ID: " + id)));
            
        } catch (Exception e) {
            logger.error("❌ Error al obtener partido: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * PUT /api/partidos/{id} - Actualizar
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PartidoUpdateDTO dto) {
        try {
            logger.info("✏️ Actualizando partido ID: {}", id);
            
            PartidoDTO actualizado = partidoService.actualizar(id, dto);
            
            return ResponseEntity.ok(crearRespuestaExitosa(
                "Partido actualizado exitosamente", 
                actualizado));
            
        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Error de validación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            logger.error("❌ Error al actualizar partido: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * PUT /api/partidos/{id}/resultado - Cambiar resultado
     */
    @PutMapping("/{id}/resultado")
    public ResponseEntity<Map<String, Object>> cambiarResultado(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            String resultadoStr = request.get("resultado");
            logger.info("🔄 Cambiando resultado del partido {} a {}", id, resultadoStr);
            
            ResultadoPartido resultado = ResultadoPartido.valueOf(resultadoStr);
            PartidoDTO actualizado = partidoService.cambiarResultado(id, resultado);
            
            return ResponseEntity.ok(crearRespuestaExitosa(
                "Resultado actualizado exitosamente", 
                actualizado));
            
        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            logger.error("❌ Error al cambiar resultado: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * DELETE /api/partidos/{id} - Eliminar
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        try {
            logger.info("🗑️ Eliminando partido ID: {}", id);
            
            partidoService.eliminar(id);
            
            return ResponseEntity.ok(crearRespuestaExitosa(
                "Partido eliminado exitosamente", 
                null));
            
        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(crearRespuestaError(e.getMessage()));
        } catch (Exception e) {
            logger.error("❌ Error al eliminar partido: ", e);
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
