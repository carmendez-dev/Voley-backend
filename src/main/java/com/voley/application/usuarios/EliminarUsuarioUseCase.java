package com.voley.application.usuarios;

import com.voley.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para eliminar un usuario
 * Encapsula la lógica de negocio para la eliminación de usuarios
 */
@Component
public class EliminarUsuarioUseCase {
    
    private final UsuarioService usuarioService;
    
    @Autowired
    public EliminarUsuarioUseCase(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    /**
     * Ejecuta el caso de uso para eliminar un usuario
     * 
     * @param id El ID del usuario a eliminar
     * @throws IllegalArgumentException Si el ID es inválido
     */
    public void ejecutar(Long id) {
        validarId(id);
        usuarioService.eliminarUsuario(id);
    }
    
    private void validarId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser un número positivo");
        }
    }
}