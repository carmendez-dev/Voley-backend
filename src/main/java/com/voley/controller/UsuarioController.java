package com.voley.controller;

import com.voley.domain.Usuario;
import com.voley.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para operaciones CRUD de Usuario
 * Expone endpoints para la gestión de usuarios
 */
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {
    
    private final UsuarioService usuarioService;
    
    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    /**
     * Crear un nuevo usuario
     * POST /api/usuarios
     */
    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioCreado = usuarioService.crearUsuario(usuario);
            return new ResponseEntity<>(usuarioCreado, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Error interno del servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Obtener todos los usuarios
     * GET /api/usuarios
     */
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }
    
    /**
     * Obtener usuario por ID
     * GET /api/usuarios/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
        if (usuario.isPresent()) {
            return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorResponse("Usuario no encontrado"), HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Obtener usuario por cédula
     * GET /api/usuarios/cedula/{cedula}
     */
    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<?> obtenerUsuarioPorCedula(@PathVariable String cedula) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorCedula(cedula);
        if (usuario.isPresent()) {
            return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorResponse("Usuario no encontrado"), HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Obtener usuario por email
     * GET /api/usuarios/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> obtenerUsuarioPorEmail(@PathVariable String email) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorEmail(email);
        if (usuario.isPresent()) {
            return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorResponse("Usuario no encontrado"), HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Obtener usuarios por tipo
     * GET /api/usuarios/tipo/{tipo}
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Usuario>> obtenerUsuariosPorTipo(@PathVariable Usuario.TipoUsuario tipo) {
        List<Usuario> usuarios = usuarioService.obtenerUsuariosPorTipo(tipo);
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }
    
    /**
     * Obtener usuarios por estado
     * GET /api/usuarios/estado/{estado}
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Usuario>> obtenerUsuariosPorEstado(@PathVariable Usuario.EstadoUsuario estado) {
        List<Usuario> usuarios = usuarioService.obtenerUsuariosPorEstado(estado);
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }
    
    /**
     * Actualizar usuario
     * PUT /api/usuarios/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuario);
            return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Error interno del servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Eliminar usuario
     * DELETE /api/usuarios/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id);
            return new ResponseEntity<>(new SuccessResponse("Usuario eliminado exitosamente"), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Error interno del servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Cambiar estado de usuario
     * PATCH /api/usuarios/{id}/estado
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstadoUsuario(@PathVariable Long id, @RequestBody EstadoRequest estadoRequest) {
        try {
            Usuario usuario = usuarioService.cambiarEstadoUsuario(id, estadoRequest.getEstado());
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Error interno del servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Clases auxiliares para respuestas
    public static class ErrorResponse {
        private String mensaje;
        
        public ErrorResponse(String mensaje) {
            this.mensaje = mensaje;
        }
        
        public String getMensaje() { return mensaje; }
        public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    }
    
    public static class SuccessResponse {
        private String mensaje;
        
        public SuccessResponse(String mensaje) {
            this.mensaje = mensaje;
        }
        
        public String getMensaje() { return mensaje; }
        public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    }
    
    public static class EstadoRequest {
        private Usuario.EstadoUsuario estado;
        
        public Usuario.EstadoUsuario getEstado() { return estado; }
        public void setEstado(Usuario.EstadoUsuario estado) { this.estado = estado; }
    }
}