package com.voley.controller;

import com.voley.domain.Equipo;
import com.voley.domain.CategoriaEquipo;
import com.voley.service.EquipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para operaciones con Equipos
 * 
 * Expone endpoints para el CRUD de equipos y la gestión de
 * relaciones categoría-equipo según los requerimientos:
 * 
 * - CRUD completo de Equipo (/equipos)
 * - POST /categorias/{id}/equipos/{idEquipo} → asignar equipo
 * - GET /categorias/{id}/equipos → listar equipos de esa categoría
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(originPatterns = {"http://localhost:*", "http://127.0.0.1:*"}, 
             allowedHeaders = {"Origin", "Content-Type", "Accept", "Authorization", 
                              "Access-Control-Request-Method", "Access-Control-Request-Headers"},
             allowCredentials = "true")
public class EquipoUseCaseController {
    
    private final EquipoService equipoService;
    
    @Autowired
    public EquipoUseCaseController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }
    
    // ===== ENDPOINTS CRUD PARA EQUIPOS =====
    
    /**
     * GET /api/equipos - Obtener todos los equipos
     */
    @GetMapping("/equipos")
    public ResponseEntity<List<Equipo>> obtenerTodosEquipos() {
        try {
            List<Equipo> equipos = equipoService.obtenerTodosEquipos();
            return ResponseEntity.ok(equipos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * GET /api/equipos/{id} - Obtener equipo por ID
     */
    @GetMapping("/equipos/{id}")
    public ResponseEntity<Equipo> obtenerEquipoPorId(@PathVariable Long id) {
        try {
            Equipo equipo = equipoService.obtenerEquipoPorId(id);
            return ResponseEntity.ok(equipo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * POST /api/equipos - Crear nuevo equipo
     */
    @PostMapping("/equipos")
    public ResponseEntity<Equipo> crearEquipo(@RequestBody EquipoRequest request) {
        try {
            Equipo equipo = equipoService.crearEquipo(request.getNombre(), request.getDescripcion());
            return ResponseEntity.status(HttpStatus.CREATED).body(equipo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * PUT /api/equipos/{id} - Actualizar equipo existente
     */
    @PutMapping("/equipos/{id}")
    public ResponseEntity<Equipo> actualizarEquipo(@PathVariable Long id, @RequestBody EquipoRequest request) {
        try {
            Equipo equipo = equipoService.actualizarEquipo(id, request.getNombre(), request.getDescripcion());
            return ResponseEntity.ok(equipo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * DELETE /api/equipos/{id} - Eliminar equipo
     */
    @DeleteMapping("/equipos/{id}")
    public ResponseEntity<Void> eliminarEquipo(@PathVariable Long id) {
        try {
            equipoService.eliminarEquipo(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * GET /api/equipos/buscar?nombre={nombre} - Buscar equipos por nombre
     */
    @GetMapping("/equipos/buscar")
    public ResponseEntity<List<Equipo>> buscarEquiposPorNombre(@RequestParam String nombre) {
        try {
            List<Equipo> equipos = equipoService.buscarEquiposPorNombre(nombre);
            return ResponseEntity.ok(equipos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // ===== ENDPOINTS PARA RELACIONES CATEGORIA-EQUIPO =====
    
    /**
     * POST /api/categorias/{id}/equipos/{idEquipo} - Asignar equipo a categoría
     */
    @PostMapping("/categorias/{id}/equipos/{idEquipo}")
    public ResponseEntity<Map<String, String>> asignarEquipoACategoria(@PathVariable Long id, @PathVariable Long idEquipo) {
        try {
            equipoService.asignarEquipoACategoria(id, idEquipo);
            return ResponseEntity.ok(Map.of("message", "Equipo asignado exitosamente a la categoría"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }
    
    /**
     * GET /api/categorias/{id}/equipos - Obtener equipos de una categoría
     */
    @GetMapping("/categorias/{id}/equipos")
    public ResponseEntity<List<EquipoBasicoDTO>> obtenerEquiposPorCategoria(@PathVariable Long id) {
        try {
            List<CategoriaEquipo> relaciones = equipoService.obtenerEquiposPorCategoria(id);
            
            // Convertir a DTO básico con información del equipo y categoría
            List<EquipoBasicoDTO> equiposDTO = relaciones.stream()
                    .map(rel -> new EquipoBasicoDTO(
                            rel.getEquipo().getIdEquipo(),
                            rel.getEquipo().getNombre(),
                            rel.getEquipo().getDescripcion(),
                            rel.getCategoria().getIdCategoria(),
                            rel.getCategoria().getNombre()
                    ))
                    .toList();
            
            return ResponseEntity.ok(equiposDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * DELETE /api/categorias/{id}/equipos/{idEquipo} - Desasignar equipo de categoría
     */
    @DeleteMapping("/categorias/{id}/equipos/{idEquipo}")
    public ResponseEntity<Map<String, String>> desasignarEquipoDeCategoria(@PathVariable Long id, @PathVariable Long idEquipo) {
        try {
            equipoService.desasignarEquipoDeCategoria(id, idEquipo);
            return ResponseEntity.ok(Map.of("message", "Equipo desasignado exitosamente de la categoría"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound()
                    .build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }
    
    // ===== CLASES INTERNAS PARA REQUEST/RESPONSE =====
    
    /**
     * Clase para recibir datos en peticiones POST/PUT
     */
    public static class EquipoRequest {
        private String nombre;
        private String descripcion;
        
        // Constructores
        public EquipoRequest() {}
        
        public EquipoRequest(String nombre, String descripcion) {
            this.nombre = nombre;
            this.descripcion = descripcion;
        }
        
        // Getters y Setters
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    }
    
    /**
     * DTO para respuesta básica de equipos con información de categoría
     */
    public static class EquipoBasicoDTO {
        private Long idEquipo;
        private String nombre;
        private String descripcion;
        private Long idCategoria;
        private String nombreCategoria;
        
        public EquipoBasicoDTO(Long idEquipo, String nombre, String descripcion, Long idCategoria, String nombreCategoria) {
            this.idEquipo = idEquipo;
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.idCategoria = idCategoria;
            this.nombreCategoria = nombreCategoria;
        }
        
        // Getters
        public Long getIdEquipo() { return idEquipo; }
        public String getNombre() { return nombre; }
        public String getDescripcion() { return descripcion; }
        public Long getIdCategoria() { return idCategoria; }
        public String getNombreCategoria() { return nombreCategoria; }
    }
}