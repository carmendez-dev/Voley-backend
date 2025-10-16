package com.voley.application.usuarios;

import com.voley.domain.Usuario;
import com.voley.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Caso de uso para obtener usuarios por estado
 * Implementa el principio de responsabilidad única de Clean Architecture
 */
@Component
public class ObtenerUsuariosPorEstadoUseCase {
    
    private final UsuarioService usuarioService;
    
    @Autowired
    public ObtenerUsuariosPorEstadoUseCase(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    /**
     * Ejecuta el caso de uso para obtener usuarios por estado
     * @param estado Estado de usuario a buscar
     * @return Lista de usuarios con el estado especificado
     * @throws IllegalArgumentException si el estado es null
     */
    public List<Usuario> ejecutar(Usuario.EstadoUsuario estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado de usuario no puede ser null");
        }
        
        return usuarioService.obtenerUsuariosPorEstado(estado);
    }
}