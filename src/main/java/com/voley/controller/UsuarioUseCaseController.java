package com.voley.controller;

import com.voley.application.usuarios.*;
import com.voley.domain.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST que utiliza casos de uso (Clean Architecture)
 * Demuestra la separación de responsabilidades entre controlador y lógica de negocio
 */
@RestController
@RequestMapping("/api/v2/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioUseCaseController {
    
    private final CrearUsuarioUseCase crearUsuarioUseCase;
    private final ObtenerTodosLosUsuariosUseCase obtenerTodosLosUsuariosUseCase;
    private final ObtenerUsuarioPorIdUseCase obtenerUsuarioPorIdUseCase;
    private final ObtenerUsuarioPorCedulaUseCase obtenerUsuarioPorCedulaUseCase;
    private final ObtenerUsuarioPorEmailUseCase obtenerUsuarioPorEmailUseCase;
    private final ActualizarUsuarioUseCase actualizarUsuarioUseCase;
    private final EliminarUsuarioUseCase eliminarUsuarioUseCase;
    
    @Autowired
    public UsuarioUseCaseController(
            CrearUsuarioUseCase crearUsuarioUseCase,
            ObtenerTodosLosUsuariosUseCase obtenerTodosLosUsuariosUseCase,
            ObtenerUsuarioPorIdUseCase obtenerUsuarioPorIdUseCase,
            ObtenerUsuarioPorCedulaUseCase obtenerUsuarioPorCedulaUseCase,
            ObtenerUsuarioPorEmailUseCase obtenerUsuarioPorEmailUseCase,
            ActualizarUsuarioUseCase actualizarUsuarioUseCase,
            EliminarUsuarioUseCase eliminarUsuarioUseCase) {
        this.crearUsuarioUseCase = crearUsuarioUseCase;
        this.obtenerTodosLosUsuariosUseCase = obtenerTodosLosUsuariosUseCase;
        this.obtenerUsuarioPorIdUseCase = obtenerUsuarioPorIdUseCase;
        this.obtenerUsuarioPorCedulaUseCase = obtenerUsuarioPorCedulaUseCase;
        this.obtenerUsuarioPorEmailUseCase = obtenerUsuarioPorEmailUseCase;
        this.actualizarUsuarioUseCase = actualizarUsuarioUseCase;
        this.eliminarUsuarioUseCase = eliminarUsuarioUseCase;
    }
    
    /**
     * Crear un nuevo usuario usando caso de uso
     * POST /api/v2/usuarios
     */
    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioCreado = crearUsuarioUseCase.ejecutar(usuario);
            return new ResponseEntity<>(usuarioCreado, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Error interno del servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Obtener todos los usuarios usando caso de uso
     * GET /api/v2/usuarios
     */
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = obtenerTodosLosUsuariosUseCase.ejecutar();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }
    
    /**
     * Obtener usuario por ID usando caso de uso
     * GET /api/v2/usuarios/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUsuarioPorId(@PathVariable Long id) {
        try {
            Optional<Usuario> usuario = obtenerUsuarioPorIdUseCase.ejecutar(id);
            if (usuario.isPresent()) {
                return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorResponse("Usuario no encontrado"), HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * Obtener usuario por cédula usando caso de uso
     * GET /api/v2/usuarios/cedula/{cedula}
     */
    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<?> obtenerUsuarioPorCedula(@PathVariable String cedula) {
        try {
            Optional<Usuario> usuario = obtenerUsuarioPorCedulaUseCase.ejecutar(cedula);
            if (usuario.isPresent()) {
                return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorResponse("Usuario no encontrado"), HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * Obtener usuario por email usando caso de uso
     * GET /api/v2/usuarios/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> obtenerUsuarioPorEmail(@PathVariable String email) {
        try {
            Optional<Usuario> usuario = obtenerUsuarioPorEmailUseCase.ejecutar(email);
            if (usuario.isPresent()) {
                return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorResponse("Usuario no encontrado"), HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * Actualizar usuario usando caso de uso
     * PUT /api/v2/usuarios/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            Usuario usuarioActualizado = actualizarUsuarioUseCase.ejecutar(id, usuario);
            return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Error interno del servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Eliminar usuario usando caso de uso
     * DELETE /api/v2/usuarios/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            eliminarUsuarioUseCase.ejecutar(id);
            return new ResponseEntity<>(new SuccessResponse("Usuario eliminado exitosamente"), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Error interno del servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Clases para respuestas
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