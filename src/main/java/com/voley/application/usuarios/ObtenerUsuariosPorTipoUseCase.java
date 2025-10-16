package com.voley.application.usuarios;

import com.voley.domain.Usuario;
import com.voley.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Caso de uso para obtener usuarios por tipo
 * Implementa el principio de responsabilidad única de Clean Architecture
 */
@Component
public class ObtenerUsuariosPorTipoUseCase {
    
    private final UsuarioService usuarioService;
    
    @Autowired
    public ObtenerUsuariosPorTipoUseCase(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    /**
     * Ejecuta el caso de uso para obtener usuarios por tipo
     * @param tipo Tipo de usuario a buscar
     * @return Lista de usuarios del tipo especificado
     * @throws IllegalArgumentException si el tipo es null
     */
    public List<Usuario> ejecutar(Usuario.TipoUsuario tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de usuario no puede ser null");
        }
        
        return usuarioService.obtenerUsuariosPorTipo(tipo);
    }
}