package com.voley.controller;

import com.voley.application.torneos.*;
import com.voley.domain.Categoria;
import com.voley.domain.Torneo;
import com.voley.domain.EstadoTorneo;
import com.voley.dto.CategoriaBasicaDTO;
import com.voley.service.TorneoService;
import com.voley.service.TorneoCategoriaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador REST para la gestión de torneos usando casos de uso (Clean Architecture)
 * Maneja todas las operaciones CRUD relacionadas con torneos
 */
@RestController
@RequestMapping("/api/torneos")
public class TorneoUseCaseController {
    
    private static final Logger logger = LoggerFactory.getLogger(TorneoUseCaseController.class);
    
    private final CrearTorneoUseCase crearTorneoUseCase;
    private final ObtenerTodosLosTorneosUseCase obtenerTodosLosTorneosUseCase;
    private final ObtenerTorneoPorIdUseCase obtenerTorneoPorIdUseCase;
    private final ActualizarTorneoUseCase actualizarTorneoUseCase;
    private final EliminarTorneoUseCase eliminarTorneoUseCase;
    private final ObtenerTorneosPorEstadoUseCase obtenerTorneosPorEstadoUseCase;
    private final CambiarEstadoTorneoUseCase cambiarEstadoTorneoUseCase;
    private final BuscarTorneosPorNombreUseCase buscarTorneosPorNombreUseCase;
    private final TorneoService torneoService; // Para operaciones adicionales
    private final TorneoCategoriaService torneoCategoriaService; // Para relaciones
    
    @Autowired
    public TorneoUseCaseController(
            CrearTorneoUseCase crearTorneoUseCase,
            ObtenerTodosLosTorneosUseCase obtenerTodosLosTorneosUseCase,
            ObtenerTorneoPorIdUseCase obtenerTorneoPorIdUseCase,
            ActualizarTorneoUseCase actualizarTorneoUseCase,
            EliminarTorneoUseCase eliminarTorneoUseCase,
            ObtenerTorneosPorEstadoUseCase obtenerTorneosPorEstadoUseCase,
            CambiarEstadoTorneoUseCase cambiarEstadoTorneoUseCase,
            BuscarTorneosPorNombreUseCase buscarTorneosPorNombreUseCase,
            TorneoService torneoService,
            TorneoCategoriaService torneoCategoriaService) {
        this.crearTorneoUseCase = crearTorneoUseCase;
        this.obtenerTodosLosTorneosUseCase = obtenerTodosLosTorneosUseCase;
        this.obtenerTorneoPorIdUseCase = obtenerTorneoPorIdUseCase;
        this.actualizarTorneoUseCase = actualizarTorneoUseCase;
        this.eliminarTorneoUseCase = eliminarTorneoUseCase;
        this.obtenerTorneosPorEstadoUseCase = obtenerTorneosPorEstadoUseCase;
        this.cambiarEstadoTorneoUseCase = cambiarEstadoTorneoUseCase;
        this.buscarTorneosPorNombreUseCase = buscarTorneosPorNombreUseCase;
        this.torneoService = torneoService;
        this.torneoCategoriaService = torneoCategoriaService;
    }
    
    /**
     * GET /api/torneos - Lista todos los torneos
     */
    @GetMapping
    public ResponseEntity<?> obtenerTodosLosTorneos(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String nombre) {
        try {
            logger.info("Obteniendo torneos - estado: {}, nombre: {}", estado, nombre);
            
            List<Torneo> torneos;
            
            if (estado != null && !estado.trim().isEmpty()) {
                // Filtrar por estado
                EstadoTorneo estadoTorneo = convertirStringAEstado(estado);
                torneos = obtenerTorneosPorEstadoUseCase.ejecutar(estadoTorneo);
            } else if (nombre != null && !nombre.trim().isEmpty()) {
                // Buscar por nombre
                torneos = buscarTorneosPorNombreUseCase.ejecutar(nombre);
            } else {
                // Obtener todos
                torneos = obtenerTodosLosTorneosUseCase.ejecutar();
            }
            
            logger.info("Se encontraron {} torneos", torneos.size());
            return ResponseEntity.ok(torneos);
            
        } catch (IllegalArgumentException e) {
            logger.error("Error en parámetros de búsqueda: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Parámetros inválidos",
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            logger.error("Error interno al obtener torneos", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Error interno del servidor",
                "message", "No se pudieron obtener los torneos"
            ));
        }
    }
    
    /**
     * GET /api/torneos/{id} - Obtiene un torneo por su ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerTorneoPorId(@PathVariable Long id) {
        try {
            logger.info("Obteniendo torneo con ID: {}", id);
            
            Optional<Torneo> torneo = obtenerTorneoPorIdUseCase.ejecutar(id);
            
            if (torneo.isPresent()) {
                logger.info("Torneo encontrado: {}", torneo.get().getNombre());
                return ResponseEntity.ok(torneo.get());
            } else {
                logger.warn("Torneo no encontrado con ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            
        } catch (IllegalArgumentException e) {
            logger.error("ID inválido: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "error", "ID inválido",
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            logger.error("Error interno al obtener torneo con ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Error interno del servidor",
                "message", "No se pudo obtener el torneo"
            ));
        }
    }
    
    /**
     * POST /api/torneos - Crea un nuevo torneo
     */
    @PostMapping
    public ResponseEntity<?> crearTorneo(@RequestBody Torneo torneo) {
        try {
            logger.info("Creando nuevo torneo: {}", torneo.getNombre());
            
            Torneo torneoCreado = crearTorneoUseCase.ejecutar(torneo);
            
            logger.info("Torneo creado exitosamente con ID: {}", torneoCreado.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(torneoCreado);
            
        } catch (IllegalArgumentException e) {
            logger.error("Error en datos del torneo: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Datos inválidos",
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            logger.error("Error interno al crear torneo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Error interno del servidor",
                "message", "No se pudo crear el torneo"
            ));
        }
    }
    
    /**
     * PUT /api/torneos/{id} - Actualiza un torneo existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarTorneo(@PathVariable Long id, @RequestBody Torneo torneo) {
        try {
            logger.info("Actualizando torneo con ID: {}", id);
            
            Torneo torneoActualizado = actualizarTorneoUseCase.ejecutar(id, torneo);
            
            logger.info("Torneo actualizado exitosamente: {}", torneoActualizado.getId());
            return ResponseEntity.ok(torneoActualizado);
            
        } catch (IllegalArgumentException e) {
            logger.error("Error en datos del torneo: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Datos inválidos",
                "message", e.getMessage()
            ));
        } catch (IllegalStateException e) {
            logger.error("Estado inválido para actualización: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "error", "Estado inválido",
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            logger.error("Error interno al actualizar torneo con ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Error interno del servidor",
                "message", "No se pudo actualizar el torneo"
            ));
        }
    }
    
    /**
     * DELETE /api/torneos/{id} - Elimina un torneo
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarTorneo(@PathVariable Long id) {
        try {
            logger.info("Eliminando torneo con ID: {}", id);
            
            eliminarTorneoUseCase.ejecutar(id);
            
            logger.info("Torneo eliminado exitosamente: {}", id);
            return ResponseEntity.noContent().build();
            
        } catch (IllegalArgumentException e) {
            logger.error("ID inválido: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "error", "ID inválido",
                "message", e.getMessage()
            ));
        } catch (IllegalStateException e) {
            logger.error("Estado inválido para eliminación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "error", "Estado inválido",
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            logger.error("Error interno al eliminar torneo con ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Error interno del servidor",
                "message", "No se pudo eliminar el torneo"
            ));
        }
    }
    
    /**
     * PUT /api/torneos/{id}/estado - Cambia el estado de un torneo
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstadoTorneo(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            String nuevoEstadoStr = body.get("estado");
            if (nuevoEstadoStr == null || nuevoEstadoStr.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Estado requerido",
                    "message", "El campo 'estado' es obligatorio"
                ));
            }
            
            EstadoTorneo nuevoEstado = EstadoTorneo.valueOf(nuevoEstadoStr);
            
            logger.info("Cambiando estado del torneo {} a {}", id, nuevoEstado);
            
            Torneo torneoActualizado = cambiarEstadoTorneoUseCase.ejecutar(id, nuevoEstado);
            
            logger.info("Estado del torneo {} cambiado exitosamente a {}", id, nuevoEstado);
            return ResponseEntity.ok(torneoActualizado);
            
        } catch (IllegalArgumentException e) {
            logger.error("Error en parámetros: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Parámetros inválidos",
                "message", e.getMessage()
            ));
        } catch (IllegalStateException e) {
            logger.error("Transición de estado inválida: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "error", "Transición inválida",
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            logger.error("Error interno al cambiar estado del torneo: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Error interno del servidor",
                "message", "No se pudo cambiar el estado del torneo"
            ));
        }
    }
    
    /**
     * GET /api/torneos/activos - Obtiene torneos activos
     */
    @GetMapping("/activos")
    public ResponseEntity<?> obtenerTorneosActivos() {
        try {
            logger.info("Obteniendo torneos activos");
            
            List<Torneo> torneosActivos = torneoService.obtenerTorneosActivos();
            
            logger.info("Se encontraron {} torneos activos", torneosActivos.size());
            return ResponseEntity.ok(torneosActivos);
            
        } catch (Exception e) {
            logger.error("Error interno al obtener torneos activos", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Error interno del servidor",
                "message", "No se pudieron obtener los torneos activos"
            ));
        }
    }
    
    /**
     * GET /api/torneos/disponibles - Obtiene torneos disponibles para inscripción (Pendientes)
     */
    @GetMapping("/disponibles")
    public ResponseEntity<?> obtenerTorneosDisponibles() {
        try {
            logger.info("Obteniendo torneos disponibles para inscripción");
            
            List<Torneo> torneosDisponibles = torneoService.obtenerTorneosDisponiblesParaInscripcion();
            
            logger.info("Se encontraron {} torneos disponibles", torneosDisponibles.size());
            return ResponseEntity.ok(torneosDisponibles);
            
        } catch (Exception e) {
            logger.error("Error interno al obtener torneos disponibles", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Error interno del servidor",
                "message", "No se pudieron obtener los torneos disponibles"
            ));
        }
    }
    
    /**
     * GET /api/torneos/estadisticas - Obtiene estadísticas de torneos
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<?> obtenerEstadisticasTorneos() {
        try {
            logger.info("Obteniendo estadísticas de torneos");
            
            TorneoService.TorneoEstadisticas estadisticas = torneoService.obtenerEstadisticas();
            
            logger.info("Estadísticas obtenidas: {} torneos totales", estadisticas.getTotal());
            return ResponseEntity.ok(estadisticas);
            
        } catch (Exception e) {
            logger.error("Error interno al obtener estadísticas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Error interno del servidor",
                "message", "No se pudieron obtener las estadísticas"
            ));
        }
    }
    
    // ========== ENDPOINTS PARA RELACIONES TORNEO-CATEGORÍA ==========
    
    /**
     * POST /api/torneos/{id}/categorias/{idCategoria} - Asociar categoría a torneo
     */
    @PostMapping("/{id}/categorias/{idCategoria}")
    public ResponseEntity<?> asociarCategoriaATorneo(@PathVariable Long id, @PathVariable Long idCategoria) {
        try {
            logger.info("Asociando categoría {} al torneo {}", idCategoria, id);
            
            torneoCategoriaService.asociarCategoriaATorneo(id, idCategoria);
            
            logger.info("Categoría {} asociada exitosamente al torneo {}", idCategoria, id);
            return ResponseEntity.ok(Map.of(
                "mensaje", "Categoría asociada exitosamente al torneo",
                "torneoId", id,
                "categoriaId", idCategoria
            ));
            
        } catch (IllegalArgumentException e) {
            logger.error("Error al asociar categoría al torneo: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(
                "No se puede asociar", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error interno al asociar categoría al torneo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
                "Error interno del servidor", "No se pudo asociar la categoría al torneo"));
        }
    }
    
    /**
     * DELETE /api/torneos/{id}/categorias/{idCategoria} - Desasociar categoría de torneo
     */
    @DeleteMapping("/{id}/categorias/{idCategoria}")
    public ResponseEntity<?> desasociarCategoriaDelTorneo(@PathVariable Long id, @PathVariable Long idCategoria) {
        try {
            logger.info("Desasociando categoría {} del torneo {}", idCategoria, id);
            
            torneoCategoriaService.desasociarCategoriaDelTorneo(id, idCategoria);
            
            logger.info("Categoría {} desasociada exitosamente del torneo {}", idCategoria, id);
            return ResponseEntity.ok(Map.of(
                "mensaje", "Categoría desasociada exitosamente del torneo",
                "torneoId", id,
                "categoriaId", idCategoria
            ));
            
        } catch (IllegalArgumentException e) {
            logger.error("Error al desasociar categoría del torneo: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(
                "No se puede desasociar", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error interno al desasociar categoría del torneo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
                "Error interno del servidor", "No se pudo desasociar la categoría del torneo"));
        }
    }
    
    /**
     * GET /api/torneos/{id}/categorias - Listar categorías del torneo (ID y nombre)
     */
    @GetMapping("/{id}/categorias")
    public ResponseEntity<?> obtenerCategoriasPorTorneo(@PathVariable Long id) {
        try {
            logger.info("Obteniendo categorías del torneo {}", id);
            
            List<CategoriaBasicaDTO> categorias = torneoCategoriaService.obtenerCategoriasBasicasPorTorneo(id);
            
            logger.info("Se encontraron {} categorías para el torneo {}", categorias.size(), id);
            return ResponseEntity.ok(categorias);
            
        } catch (IllegalArgumentException e) {
            logger.error("Error al obtener categorías del torneo: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(
                "Torneo no encontrado", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error interno al obtener categorías del torneo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
                "Error interno del servidor", "No se pudieron obtener las categorías del torneo"));
        }
    }
    
    /**
     * GET /api/torneos/{id}/categorias/count - Contar categorías del torneo
     */
    @GetMapping("/{id}/categorias/count")
    public ResponseEntity<?> contarCategoriasPorTorneo(@PathVariable Long id) {
        try {
            logger.info("Contando categorías del torneo {}", id);
            
            long count = torneoCategoriaService.contarCategoriasPorTorneo(id);
            
            return ResponseEntity.ok(Map.of(
                "torneoId", id,
                "cantidadCategorias", count
            ));
            
        } catch (Exception e) {
            logger.error("Error interno al contar categorías del torneo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
                "Error interno del servidor", "No se pudo contar las categorías del torneo"));
        }
    }
    
    /**
     * Convierte un string a EstadoTorneo manejando diferentes formatos
     * Acepta: "Pendiente", "PENDIENTE", "pendiente", etc.
     */
    private EstadoTorneo convertirStringAEstado(String estadoStr) {
        if (estadoStr == null || estadoStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Estado no puede estar vacío");
        }
        
        // Normalizar el string: primera letra mayúscula, resto minúscula
        String estadoNormalizado = estadoStr.trim().toLowerCase();
        estadoNormalizado = Character.toUpperCase(estadoNormalizado.charAt(0)) + 
                           (estadoNormalizado.length() > 1 ? estadoNormalizado.substring(1) : "");
        
        try {
            return EstadoTorneo.valueOf(estadoNormalizado);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                String.format("Estado inválido: %s. Estados válidos: %s", 
                    estadoStr, 
                    Arrays.toString(EstadoTorneo.values())));
        }
    }
    
    /**
     * Clase interna para respuestas de error
     */
    public static class ErrorResponse {
        private String error;
        private String message;
        private long timestamp;
        
        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }
        
        // Getters
        public String getError() { return error; }
        public String getMessage() { return message; }
        public long getTimestamp() { return timestamp; }
    }
}