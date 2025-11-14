package com.voley.controller;

import com.voley.domain.Usuario;
import com.voley.dto.UsuarioRequestDTO;
import com.voley.dto.UsuarioResponseDTO;
import com.voley.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 🎯 Controlador CRUD para Administrar Jugadores (tabla usuarios)
 * 
 * Endpoints principales:
 * - GET /usuarios - Obtener todos los usuarios
 * - POST /usuarios - Crear nuevo usuario
 * - PUT /usuarios/{id} - Actualizar usuario
 * - DELETE /usuarios/{id} - Eliminar usuario
 * 
 * Validaciones implementadas:
 * - Email y cédula únicos
 * - Estado activo/inactivo
 * - Validación de campos requeridos
 */
@RestController
@RequestMapping("/api/usuarios")
@Validated
public class UsuarioUseCaseController {
    
    private static final Logger logger = LoggerFactory.getLogger(UsuarioUseCaseController.class);
    
    private final UsuarioService usuarioService;
    
    @Autowired
    public UsuarioUseCaseController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    /**
     * 📋 GET /usuarios - Obtener todos los usuarios
     * 🎯 Meta: Tener lista la base de jugadores registrada
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodosLosUsuarios(
            @RequestParam(defaultValue = "false") boolean completo,
            @RequestParam(required = false) String estado) {
        try {
            logger.info("📋 Obteniendo usuarios - completo: {}, estado: {}", completo, estado);
            
            Map<String, Object> response = new HashMap<>();
            
            if (completo) {
                // Usar DTOs con cálculos automáticos (IMC, edad, etc.)
                if (estado != null) {
                    Usuario.EstadoUsuario estadoEnum = Usuario.EstadoUsuario.valueOf(estado);
                    List<UsuarioResponseDTO> usuarios = usuarioService.obtenerUsuariosPorEstadoDTO(estadoEnum);
                    response = crearRespuestaExitosa("Usuarios filtrados obtenidos", usuarios, usuarios.size());
                } else {
                    List<UsuarioResponseDTO> usuarios = usuarioService.obtenerTodosLosUsuariosDTO();
                    response = crearRespuestaExitosa("Usuarios completos obtenidos", usuarios, usuarios.size());
                }
            } else {
                // Respuesta básica usando DTOs para evitar referencia circular
                List<UsuarioResponseDTO> usuarios = usuarioService.obtenerTodosLosUsuariosDTO();
                response = crearRespuestaExitosa("Usuarios obtenidos", usuarios, usuarios.size());
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("❌ Error al obtener usuarios: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * 🆕 POST /usuarios - Crear nuevo usuario
     * 🎯 Validaciones: email y cédula únicos, campos requeridos
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearUsuario(@Valid @RequestBody UsuarioRequestDTO usuarioDTO) {
        try {
            logger.info("🆕 Creando nuevo usuario: {}", usuarioDTO.getCedula());
            
            UsuarioResponseDTO usuarioCreado = usuarioService.crearUsuarioConDTO(usuarioDTO);
            
            Map<String, Object> response = crearRespuestaExitosa(
                "Usuario creado exitosamente", 
                usuarioCreado, 
                null
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Error de validación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(crearRespuestaError(e.getMessage()));
                
        } catch (Exception e) {
            logger.error("❌ Error interno al crear usuario: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * 🔍 GET /usuarios/{id} - Obtener usuario por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerUsuarioPorId(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean completo) {
        try {
            logger.info("🔍 Obteniendo usuario ID: {} - completo: {}", id, completo);
            
            if (completo) {
                Optional<UsuarioResponseDTO> usuario = usuarioService.obtenerUsuarioCompletoDTO(id);
                if (usuario.isPresent()) {
                    return ResponseEntity.ok(crearRespuestaExitosa("Usuario encontrado", usuario.get(), null));
                }
            } else {
                Optional<UsuarioResponseDTO> usuario = usuarioService.obtenerUsuarioCompletoDTO(id);
                if (usuario.isPresent()) {
                    return ResponseEntity.ok(crearRespuestaExitosa("Usuario encontrado", usuario.get(), null));
                }
            }
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(crearRespuestaError("Usuario no encontrado con ID: " + id));
                
        } catch (Exception e) {
            logger.error("❌ Error al obtener usuario por ID: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * ✏️ PUT /usuarios/{id} - Actualizar usuario
     * 🎯 Validaciones: email y cédula únicos si cambiaron
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarUsuario(
            @PathVariable Long id, 
            @Valid @RequestBody UsuarioRequestDTO usuarioDTO) {
        try {
            logger.info("✏️ Actualizando usuario ID: {} - cédula: {}", id, usuarioDTO.getCedula());
            
            UsuarioResponseDTO usuarioActualizado = usuarioService.actualizarUsuarioConDTO(id, usuarioDTO);
            
            return ResponseEntity.ok(crearRespuestaExitosa(
                "Usuario actualizado exitosamente", 
                usuarioActualizado, 
                null
            ));
            
        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Error de validación en actualización: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(crearRespuestaError(e.getMessage()));
                
        } catch (Exception e) {
            logger.error("❌ Error interno al actualizar usuario: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * 🗑️ DELETE /usuarios/{id} - Eliminar usuario
     * 🎯 Eliminación por ID con validación de existencia
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarUsuario(@PathVariable Long id) {
        try {
            logger.info("🗑️ Eliminando usuario ID: {}", id);
            
            // Verificar que existe antes de eliminar
            Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
            if (usuario.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("Usuario no encontrado con ID: " + id));
            }
            
            usuarioService.eliminarUsuario(id);
            
            return ResponseEntity.ok(crearRespuestaExitosa(
                "Usuario eliminado exitosamente", 
                null, 
                null
            ));
            
        } catch (Exception e) {
            logger.error("❌ Error al eliminar usuario: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * 🔄 PUT /usuarios/{id}/estado - Cambiar estado del usuario
     * 🎯 Activar/Inactivar usuarios
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<Map<String, Object>> cambiarEstadoUsuario(
            @PathVariable Long id, 
            @RequestBody Map<String, String> estadoRequest) {
        try {
            String nuevoEstado = estadoRequest.get("estado");
            logger.info("🔄 Cambiando estado usuario ID: {} a {}", id, nuevoEstado);
            
            Usuario.EstadoUsuario estadoEnum = Usuario.EstadoUsuario.valueOf(nuevoEstado);
            Usuario usuarioActualizado = usuarioService.cambiarEstadoUsuario(id, estadoEnum);
            
            return ResponseEntity.ok(crearRespuestaExitosa(
                "Estado actualizado exitosamente", 
                usuarioActualizado, 
                null
            ));
            
        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Estado inválido: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(crearRespuestaError("Estado inválido. Use: Activo o Inactivo"));
                
        } catch (Exception e) {
            logger.error("❌ Error al cambiar estado: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * 📊 GET /usuarios/estadisticas - Obtener estadísticas de usuarios
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        try {
            logger.info("📊 Obteniendo estadísticas de usuarios");
            
            Map<String, Object> estadisticas = usuarioService.obtenerEstadisticasUsuarios();
            
            return ResponseEntity.ok(crearRespuestaExitosa(
                "Estadísticas obtenidas exitosamente", 
                estadisticas, 
                null
            ));
            
        } catch (Exception e) {
            logger.error("❌ Error al obtener estadísticas: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    /**
     * 🔍 GET /usuarios/buscar - Búsquedas avanzadas
     */
    @GetMapping("/buscar")
    public ResponseEntity<Map<String, Object>> buscarUsuarios(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String cedula,
            @RequestParam(required = false) String email) {
        try {
            logger.info("🔍 Búsqueda avanzada - nombre: {}, apellido: {}, cedula: {}, email: {}", 
                nombre, apellido, cedula, email);
            
            List<Usuario> resultados = new ArrayList<>();
            
            if (cedula != null && !cedula.trim().isEmpty()) {
                usuarioService.obtenerUsuarioPorCedula(cedula).ifPresent(resultados::add);
            } else if (email != null && !email.trim().isEmpty()) {
                usuarioService.obtenerUsuarioPorEmail(email).ifPresent(resultados::add);
            } else if (nombre != null && !nombre.trim().isEmpty()) {
                resultados = usuarioService.buscarUsuariosPorNombres(nombre);
            } else if (apellido != null && !apellido.trim().isEmpty()) {
                resultados = usuarioService.buscarUsuariosPorApellidos(apellido);
            }
            
            return ResponseEntity.ok(crearRespuestaExitosa(
                "Búsqueda completada", 
                resultados, 
                resultados.size()
            ));
            
        } catch (Exception e) {
            logger.error("❌ Error en búsqueda: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(crearRespuestaError("Error interno del servidor"));
        }
    }
    
    // 🛠️ Métodos auxiliares para respuestas
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