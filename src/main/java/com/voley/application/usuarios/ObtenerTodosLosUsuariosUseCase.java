package com.voley.application.usuarios;

import com.voley.domain.Usuario;
import com.voley.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Caso de uso para obtener todos los usuarios
 * Encapsula la lógica de negocio para la obtención de todos los usuarios
 */
@Component
public class ObtenerTodosLosUsuariosUseCase {
    
    private final UsuarioService usuarioService;
    
    @Autowired
    public ObtenerTodosLosUsuariosUseCase(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    /**
     * Ejecuta el caso de uso para obtener todos los usuarios
     * 
     * @return Lista de todos los usuarios registrados
     */
    public List<Usuario> ejecutar() {
        return usuarioService.obtenerTodosLosUsuarios();
    }
}