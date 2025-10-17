package com.voley.controller;

import com.voley.application.categorias.*;
import com.voley.domain.Categoria;
import com.voley.domain.GeneroCategoria;
import com.voley.dto.CategoriaDTO;
import com.voley.service.CategoriaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador REST para la gestión de categorías usando casos de uso (Clean Architecture)
 * Maneja todas las operaciones CRUD relacionadas con categorías
 */
@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(originPatterns = {"http://localhost:*", "http://127.0.0.1:*"}, 
             allowedHeaders = {"Origin", "Content-Type", "Accept", "Authorization", 
                              "Access-Control-Request-Method", "Access-Control-Request-Headers"},
             allowCredentials = "true")
public class CategoriaUseCaseController {
    
    private static final Logger logger = LoggerFactory.getLogger(CategoriaUseCaseController.class);
    
    // Casos de uso
    private final CrearCategoriaUseCase crearCategoriaUseCase;
    private final ObtenerTodasLasCategoriasUseCase obtenerTodasLasCategoriasUseCase;
    private final ObtenerCategoriaPorIdUseCase obtenerCategoriaPorIdUseCase;
    private final ActualizarCategoriaUseCase actualizarCategoriaUseCase;
    private final EliminarCategoriaUseCase eliminarCategoriaUseCase;
    private final BuscarCategoriasPorGeneroUseCase buscarCategoriasPorGeneroUseCase;
    private final BuscarCategoriasPorNombreUseCase buscarCategoriasPorNombreUseCase;
    private final CategoriaService categoriaService;
    
    @Autowired
    public CategoriaUseCaseController(
            CrearCategoriaUseCase crearCategoriaUseCase,
            ObtenerTodasLasCategoriasUseCase obtenerTodasLasCategoriasUseCase,
            ObtenerCategoriaPorIdUseCase obtenerCategoriaPorIdUseCase,
            ActualizarCategoriaUseCase actualizarCategoriaUseCase,
            EliminarCategoriaUseCase eliminarCategoriaUseCase,
            BuscarCategoriasPorGeneroUseCase buscarCategoriasPorGeneroUseCase,
            BuscarCategoriasPorNombreUseCase buscarCategoriasPorNombreUseCase,
            CategoriaService categoriaService) {
        this.crearCategoriaUseCase = crearCategoriaUseCase;
        this.obtenerTodasLasCategoriasUseCase = obtenerTodasLasCategoriasUseCase;
        this.obtenerCategoriaPorIdUseCase = obtenerCategoriaPorIdUseCase;
        this.actualizarCategoriaUseCase = actualizarCategoriaUseCase;
        this.eliminarCategoriaUseCase = eliminarCategoriaUseCase;
        this.buscarCategoriasPorGeneroUseCase = buscarCategoriasPorGeneroUseCase;
        this.buscarCategoriasPorNombreUseCase = buscarCategoriasPorNombreUseCase;
        this.categoriaService = categoriaService;
    }
    
    /**
     * GET /api/categorias - Lista todas las categorías
     */
    @GetMapping
    public ResponseEntity<?> obtenerTodasLasCategorias(
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String nombre) {
        try {
            logger.info("Obteniendo categorías - genero: {}, nombre: {}", genero, nombre);
            
            List<Categoria> categorias;
            
            if (genero != null && !genero.trim().isEmpty()) {
                // Filtrar por género
                GeneroCategoria generoCategoria = convertirStringAGenero(genero);
                categorias = buscarCategoriasPorGeneroUseCase.ejecutar(generoCategoria);
            } else if (nombre != null && !nombre.trim().isEmpty()) {
                // Buscar por nombre
                categorias = buscarCategoriasPorNombreUseCase.ejecutar(nombre);
            } else {
                // Obtener todas
                categorias = obtenerTodasLasCategoriasUseCase.ejecutar();
            }
            
            logger.info("Se encontraron {} categorías", categorias.size());
            return ResponseEntity.ok(categorias);
            
        } catch (IllegalArgumentException e) {
            logger.error("Error en parámetros de búsqueda: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(
                "Parámetros inválidos", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error interno al obtener categorías", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
                "Error interno del servidor", "No se pudieron obtener las categorías"));
        }
    }
    
    /**
     * GET /api/categorias/{id} - Obtiene una categoría por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCategoriaPorId(@PathVariable Long id) {
        try {
            logger.info("Obteniendo categoría con ID: {}", id);
            
            Optional<Categoria> categoria = obtenerCategoriaPorIdUseCase.ejecutar(id);
            
            if (categoria.isPresent()) {
                logger.info("Categoría encontrada: {}", categoria.get().getNombre());
                return ResponseEntity.ok(categoria.get());
            } else {
                logger.warn("Categoría no encontrada con ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(
                    "Categoría no encontrada", "No existe categoría con ID: " + id));
            }
            
        } catch (Exception e) {
            logger.error("Error interno al obtener categoría por ID: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
                "Error interno del servidor", "No se pudo obtener la categoría"));
        }
    }
    
    /**
     * POST /api/categorias - Crea una nueva categoría
     */
    @PostMapping
    public ResponseEntity<?> crearCategoria(@Valid @RequestBody CategoriaDTO categoriaDTO) {
        try {
            logger.info("Creando nueva categoría: {}", categoriaDTO.getNombre());
            
            // Convertir DTO a entidad
            Categoria categoria = new Categoria(categoriaDTO.getNombre(), categoriaDTO.getGenero());
            
            // Crear categoría
            Categoria categoriaCreada = crearCategoriaUseCase.ejecutar(categoria);
            
            logger.info("Categoría creada exitosamente con ID: {}", categoriaCreada.getIdCategoria());
            return ResponseEntity.status(HttpStatus.CREATED).body(categoriaCreada);
            
        } catch (IllegalArgumentException e) {
            logger.error("Error de validación al crear categoría: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(
                "Datos inválidos", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error interno al crear categoría", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
                "Error interno del servidor", "No se pudo crear la categoría"));
        }
    }
    
    /**
     * PUT /api/categorias/{id} - Actualiza una categoría existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCategoria(@PathVariable Long id, @Valid @RequestBody CategoriaDTO categoriaDTO) {
        try {
            logger.info("Actualizando categoría con ID: {}", id);
            
            // Convertir DTO a entidad
            Categoria categoria = new Categoria(categoriaDTO.getNombre(), categoriaDTO.getGenero());
            
            // Actualizar categoría
            Categoria categoriaActualizada = actualizarCategoriaUseCase.ejecutar(id, categoria);
            
            logger.info("Categoría actualizada exitosamente: {}", id);
            return ResponseEntity.ok(categoriaActualizada);
            
        } catch (IllegalArgumentException e) {
            logger.error("Error de validación al actualizar categoría: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(
                "Datos inválidos", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error interno al actualizar categoría", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
                "Error interno del servidor", "No se pudo actualizar la categoría"));
        }
    }
    
    /**
     * DELETE /api/categorias/{id} - Elimina una categoría
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Long id) {
        try {
            logger.info("Eliminando categoría con ID: {}", id);
            
            eliminarCategoriaUseCase.ejecutar(id);
            
            logger.info("Categoría eliminada exitosamente: {}", id);
            return ResponseEntity.ok(Map.of(
                "mensaje", "Categoría eliminada exitosamente",
                "id", id
            ));
            
        } catch (IllegalArgumentException e) {
            logger.error("Error al eliminar categoría: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(
                "No se puede eliminar", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error interno al eliminar categoría", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
                "Error interno del servidor", "No se pudo eliminar la categoría"));
        }
    }
    
    /**
     * GET /api/categorias/estadisticas - Obtiene estadísticas de categorías
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<?> obtenerEstadisticas() {
        try {
            logger.info("Obteniendo estadísticas de categorías");
            
            CategoriaService.CategoriaEstadisticas estadisticas = categoriaService.obtenerEstadisticas();
            
            return ResponseEntity.ok(Map.of(
                "total", estadisticas.getTotal(),
                "masculinas", estadisticas.getMasculinas(),
                "femeninas", estadisticas.getFemeninas(),
                "mixtas", estadisticas.getMixtas()
            ));
            
        } catch (Exception e) {
            logger.error("Error interno al obtener estadísticas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
                "Error interno del servidor", "No se pudieron obtener las estadísticas"));
        }
    }
    
    /**
     * Convierte un string a GeneroCategoria manejando diferentes formatos
     * Acepta: "Masculino", "MASCULINO", "masculino", etc.
     */
    private GeneroCategoria convertirStringAGenero(String generoStr) {
        if (generoStr == null || generoStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Género no puede estar vacío");
        }
        
        // Normalizar el string: primera letra mayúscula, resto minúscula
        String generoNormalizado = generoStr.trim().toLowerCase();
        generoNormalizado = Character.toUpperCase(generoNormalizado.charAt(0)) + 
                           (generoNormalizado.length() > 1 ? generoNormalizado.substring(1) : "");
        
        try {
            return GeneroCategoria.valueOf(generoNormalizado);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                String.format("Género inválido: %s. Géneros válidos: Masculino, Femenino, Mixto", 
                    generoStr));
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