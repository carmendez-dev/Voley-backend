package com.voley.application.usuarios;

import com.voley.domain.Usuario;
import com.voley.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para actualizar un usuario
 * Encapsula la lógica de negocio para la actualización de usuarios
 */
@Component
public class ActualizarUsuarioUseCase {
    
    private final UsuarioService usuarioService;
    
    @Autowired
    public ActualizarUsuarioUseCase(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    /**
     * Ejecuta el caso de uso para actualizar un usuario
     * 
     * @param id El ID del usuario a actualizar
     * @param usuarioActualizado Los datos actualizados del usuario
     * @return El usuario actualizado
     * @throws IllegalArgumentException Si los datos son inválidos
     */
    public Usuario ejecutar(Long id, Usuario usuarioActualizado) {
        validarParametros(id, usuarioActualizado);
        return usuarioService.actualizarUsuario(id, usuarioActualizado);
    }
    
    private void validarParametros(Long id, Usuario usuarioActualizado) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser un número positivo");
        }
        
        if (usuarioActualizado == null) {
            throw new IllegalArgumentException("Los datos del usuario no pueden ser nulos");
        }
        
        // Validaciones de datos actualizados
        if (usuarioActualizado.getNombres() != null && usuarioActualizado.getNombres().trim().isEmpty()) {
            throw new IllegalArgumentException("Los nombres no pueden estar vacíos");
        }
        
        if (usuarioActualizado.getEmail() != null && !usuarioActualizado.getEmail().trim().isEmpty()) {
            if (!usuarioActualizado.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                throw new IllegalArgumentException("El formato del email no es válido");
            }
        }
        
        if (usuarioActualizado.getCedula() != null && !usuarioActualizado.getCedula().trim().isEmpty()) {
            if (!usuarioActualizado.getCedula().matches("\\d{8,15}")) {
                throw new IllegalArgumentException("La cédula debe contener entre 8 y 15 dígitos");
            }
        }
    }
}