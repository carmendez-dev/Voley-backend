package com.voley.controller;

import com.voley.application.usuarios.*;
import com.voley.domain.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador REST que utiliza casos de uso para usuarios (Clean Architecture)
 * Demuestra la separación de responsabilidades entre controlador y lógica de negocio
 */
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:5174"}, 
             allowedHeaders = "*", allowCredentials = "true")
public class UsuarioUseCaseController {
    
    private static final Logger logger = LoggerFactory.getLogger(UsuarioUseCaseController.class);
    
    private final CrearUsuarioUseCase crearUsuarioUseCase;
    private final ObtenerTodosLosUsuariosUseCase obtenerTodosLosUsuariosUseCase;
    private final ObtenerUsuarioPorIdUseCase obtenerUsuarioPorIdUseCase;
    private final ObtenerUsuarioPorCedulaUseCase obtenerUsuarioPorCedulaUseCase;
    private final ObtenerUsuarioPorEmailUseCase obtenerUsuarioPorEmailUseCase;
    private final ObtenerUsuariosPorTipoUseCase obtenerUsuariosPorTipoUseCase;
    private final ObtenerUsuariosPorEstadoUseCase obtenerUsuariosPorEstadoUseCase;
    private final ActualizarUsuarioUseCase actualizarUsuarioUseCase;
    private final CambiarEstadoUsuarioUseCase cambiarEstadoUsuarioUseCase;
    private final EliminarUsuarioUseCase eliminarUsuarioUseCase;
    
    @Autowired
    public UsuarioUseCaseController(
            CrearUsuarioUseCase crearUsuarioUseCase,
            ObtenerTodosLosUsuariosUseCase obtenerTodosLosUsuariosUseCase,
            ObtenerUsuarioPorIdUseCase obtenerUsuarioPorIdUseCase,
            ObtenerUsuarioPorCedulaUseCase obtenerUsuarioPorCedulaUseCase,
            ObtenerUsuarioPorEmailUseCase obtenerUsuarioPorEmailUseCase,
            ObtenerUsuariosPorTipoUseCase obtenerUsuariosPorTipoUseCase,
            ObtenerUsuariosPorEstadoUseCase obtenerUsuariosPorEstadoUseCase,
            ActualizarUsuarioUseCase actualizarUsuarioUseCase,
            CambiarEstadoUsuarioUseCase cambiarEstadoUsuarioUseCase,
            EliminarUsuarioUseCase eliminarUsuarioUseCase) {
        this.crearUsuarioUseCase = crearUsuarioUseCase;
        this.obtenerTodosLosUsuariosUseCase = obtenerTodosLosUsuariosUseCase;
        this.obtenerUsuarioPorIdUseCase = obtenerUsuarioPorIdUseCase;
        this.obtenerUsuarioPorCedulaUseCase = obtenerUsuarioPorCedulaUseCase;
        this.obtenerUsuarioPorEmailUseCase = obtenerUsuarioPorEmailUseCase;
        this.obtenerUsuariosPorTipoUseCase = obtenerUsuariosPorTipoUseCase;
        this.obtenerUsuariosPorEstadoUseCase = obtenerUsuariosPorEstadoUseCase;
        this.actualizarUsuarioUseCase = actualizarUsuarioUseCase;
        this.cambiarEstadoUsuarioUseCase = cambiarEstadoUsuarioUseCase;
        this.eliminarUsuarioUseCase = eliminarUsuarioUseCase;
    }
    
    /**
     * Crear un nuevo usuario usando caso de uso
     * POST /api/usuarios
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearUsuario(@RequestBody Usuario usuario) {
        try {
            logger.info("Creando nuevo usuario usando caso de uso");
            Usuario usuarioCreado = crearUsuarioUseCase.ejecutar(usuario);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario creado exitosamente");
            response.put("data", convertirUsuarioAMap(usuarioCreado));
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Error de validación al crear usuario: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error de validación");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            logger.error("Error interno al crear usuario: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Obtener todos los usuarios usando caso de uso
     * GET /api/usuarios
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodosLosUsuarios() {
        try {
            logger.info("Obteniendo todos los usuarios usando caso de uso");
            List<Usuario> usuarios = obtenerTodosLosUsuariosUseCase.ejecutar();
            
            List<Map<String, Object>> usuariosList = usuarios.stream()
                    .map(this::convertirUsuarioAMap)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuarios obtenidos exitosamente");
            response.put("timestamp", LocalDate.now().toString());
            response.put("total", usuariosList.size());
            response.put("data", usuariosList);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error al obtener usuarios: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al obtener los usuarios");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Obtener usuario por ID usando caso de uso
     * GET /api/usuarios/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerUsuarioPorId(@PathVariable Long id) {
        try {
            logger.info("Obteniendo usuario por ID: {} usando caso de uso", id);
            
            Optional<Usuario> usuarioOpt = obtenerUsuarioPorIdUseCase.ejecutar(id);
            
            if (usuarioOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Usuario encontrado exitosamente");
                response.put("data", convertirUsuarioAMap(usuarioOpt.get()));
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                response.put("usuarioId", id);
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
        } catch (IllegalArgumentException e) {
            logger.warn("Error de validación al obtener usuario {}: {}", id, e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            logger.error("Error interno al obtener usuario {}: ", id, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Obtener usuario por cédula usando caso de uso
     * GET /api/usuarios/cedula/{cedula}
     */
    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<Map<String, Object>> obtenerUsuarioPorCedula(@PathVariable String cedula) {
        try {
            logger.info("Obteniendo usuario por cédula: {} usando caso de uso", cedula);
            
            Optional<Usuario> usuarioOpt = obtenerUsuarioPorCedulaUseCase.ejecutar(cedula);
            
            if (usuarioOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Usuario encontrado exitosamente");
                response.put("data", convertirUsuarioAMap(usuarioOpt.get()));
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                response.put("cedula", cedula);
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
        } catch (IllegalArgumentException e) {
            logger.warn("Error de validación: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    
    /**
     * Obtener usuario por email usando caso de uso
     * GET /api/usuarios/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Map<String, Object>> obtenerUsuarioPorEmail(@PathVariable String email) {
        try {
            logger.info("Obteniendo usuario por email: {} usando caso de uso", email);
            
            Optional<Usuario> usuarioOpt = obtenerUsuarioPorEmailUseCase.ejecutar(email);
            
            if (usuarioOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Usuario encontrado exitosamente");
                response.put("data", convertirUsuarioAMap(usuarioOpt.get()));
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                response.put("email", email);
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
        } catch (IllegalArgumentException e) {
            logger.warn("Error de validación: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    
    /**
     * Obtener usuarios por tipo usando caso de uso
     * GET /api/usuarios/tipo/{tipo}
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<Map<String, Object>> obtenerUsuariosPorTipo(@PathVariable String tipo) {
        try {
            logger.info("Obteniendo usuarios por tipo: {}", tipo);
            
            Usuario.TipoUsuario tipoUsuario = Usuario.TipoUsuario.valueOf(tipo.toUpperCase());
            List<Usuario> usuarios = obtenerUsuariosPorTipoUseCase.ejecutar(tipoUsuario);
            
            List<Map<String, Object>> usuariosList = usuarios.stream()
                    .map(this::convertirUsuarioAMap)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuarios por tipo obtenidos exitosamente");
            response.put("tipo", tipo);
            response.put("total", usuariosList.size());
            response.put("data", usuariosList);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Tipo inválido: {}", tipo);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Tipo de usuario inválido: " + tipo);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    
    /**
     * Obtener usuarios por estado usando caso de uso
     * GET /api/usuarios/estado/{estado}
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<Map<String, Object>> obtenerUsuariosPorEstado(@PathVariable String estado) {
        try {
            logger.info("Obteniendo usuarios por estado: {}", estado);
            
            Usuario.EstadoUsuario estadoUsuario = Usuario.EstadoUsuario.valueOf(estado.toUpperCase());
            List<Usuario> usuarios = obtenerUsuariosPorEstadoUseCase.ejecutar(estadoUsuario);
            
            List<Map<String, Object>> usuariosList = usuarios.stream()
                    .map(this::convertirUsuarioAMap)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuarios por estado obtenidos exitosamente");
            response.put("estado", estado);
            response.put("total", usuariosList.size());
            response.put("data", usuariosList);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Estado inválido: {}", estado);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Estado de usuario inválido: " + estado);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    
    /**
     * Actualizar usuario usando caso de uso
     * PUT /api/usuarios/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            logger.info("Actualizando usuario con ID: {} usando caso de uso", id);
            Usuario usuarioActualizado = actualizarUsuarioUseCase.ejecutar(id, usuario);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario actualizado exitosamente");
            response.put("data", convertirUsuarioAMap(usuarioActualizado));
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Error de validación al actualizar usuario: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            logger.error("Error al actualizar usuario: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Cambiar estado de usuario usando caso de uso
     * PATCH /api/usuarios/{id}/estado
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Map<String, Object>> cambiarEstadoUsuario(
            @PathVariable Long id, 
            @RequestBody EstadoRequest estadoRequest) {
        try {
            logger.info("Cambiando estado del usuario ID: {} a: {}", id, estadoRequest.getEstado());
            
            Usuario usuarioActualizado = cambiarEstadoUsuarioUseCase.ejecutar(id, estadoRequest.getEstado());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Estado del usuario cambiado exitosamente");
            response.put("data", convertirUsuarioAMap(usuarioActualizado));
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Error de validación al cambiar estado: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            logger.error("Error al cambiar estado del usuario: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Eliminar usuario usando caso de uso
     * DELETE /api/usuarios/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarUsuario(@PathVariable Long id) {
        try {
            logger.info("Eliminando usuario con ID: {} usando caso de uso", id);
            
            eliminarUsuarioUseCase.ejecutar(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario eliminado exitosamente");
            response.put("timestamp", LocalDate.now().toString());
            response.put("usuarioId", id);
            
            logger.info("Usuario eliminado exitosamente: {}", id);
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            logger.error("Error de validación al eliminar usuario {}: {}", id, e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("timestamp", LocalDate.now().toString());
            response.put("usuarioId", id);
            
            return ResponseEntity.badRequest().body(response);
            
        } catch (Exception e) {
            logger.error("Error interno al eliminar usuario {}: {}", id, e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error interno del servidor");
            response.put("timestamp", LocalDate.now().toString());
            response.put("usuarioId", id);
            
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * Convierte un objeto Usuario a un Map para la respuesta JSON
     */
    private Map<String, Object> convertirUsuarioAMap(Usuario usuario) {
        Map<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("id", usuario.getId());
        usuarioMap.put("nombres", usuario.getNombres());
        usuarioMap.put("apellidos", usuario.getApellidos());
        usuarioMap.put("nombreCompleto", usuario.getNombreCompleto());
        usuarioMap.put("fechaNacimiento", usuario.getFechaNacimiento());
        usuarioMap.put("cedula", usuario.getCedula());
        usuarioMap.put("genero", usuario.getGenero() != null ? usuario.getGenero().toString() : null);
        usuarioMap.put("email", usuario.getEmail());
        usuarioMap.put("celular", usuario.getCelular());
        usuarioMap.put("direccion", usuario.getDireccion());
        usuarioMap.put("contactoEmergencia", usuario.getContactoEmergencia());
        usuarioMap.put("tipo", usuario.getTipo() != null ? usuario.getTipo().toString() : null);
        usuarioMap.put("estado", usuario.getEstado() != null ? usuario.getEstado().toString() : null);
        usuarioMap.put("fechaRegistro", usuario.getFechaRegistro());
        usuarioMap.put("createdAt", usuario.getCreatedAt());
        usuarioMap.put("updatedAt", usuario.getUpdatedAt());
        return usuarioMap;
    }
    
    // Clases auxiliares para requests y responses
    public static class EstadoRequest {
        private Usuario.EstadoUsuario estado;
        
        public Usuario.EstadoUsuario getEstado() { return estado; }
        public void setEstado(Usuario.EstadoUsuario estado) { this.estado = estado; }
    }
    
    // Clases para respuestas (mantenidas para compatibilidad)
    public static class ErrorResponse {
        private String message;
        private String type = "error";
        
        public ErrorResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() { return message; }
        public String getType() { return type; }
    }
    
    public static class SuccessResponse {
        private String message;
        private String type = "success";
        
        public SuccessResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() { return message; }
        public String getType() { return type; }
    }
}