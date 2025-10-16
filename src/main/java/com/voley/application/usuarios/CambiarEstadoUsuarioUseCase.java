package com.voley.application.usuarios;

import com.voley.domain.Usuario;
import com.voley.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para cambiar el estado de un usuario
 * Implementa el principio de responsabilidad única de Clean Architecture
 */
@Component
public class CambiarEstadoUsuarioUseCase {
    
    private final UsuarioService usuarioService;
    
    @Autowired
    public CambiarEstadoUsuarioUseCase(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    /**
     * Ejecuta el caso de uso para cambiar el estado de un usuario
     * @param id ID del usuario
     * @param nuevoEstado Nuevo estado del usuario
     * @return Usuario con el estado actualizado
     * @throws IllegalArgumentException si los parámetros son inválidos
     */
    public Usuario ejecutar(Long id, Usuario.EstadoUsuario nuevoEstado) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser un número positivo");
        }
        
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado no puede ser null");
        }
        
        return usuarioService.cambiarEstadoUsuario(id, nuevoEstado);
    }
}