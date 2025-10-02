package com.voley.application.usuarios;

import com.voley.domain.Usuario;
import com.voley.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Caso de uso para obtener un usuario por ID
 * Encapsula la lógica de negocio para la búsqueda de usuarios por ID
 */
@Component
public class ObtenerUsuarioPorIdUseCase {
    
    private final UsuarioService usuarioService;
    
    @Autowired
    public ObtenerUsuarioPorIdUseCase(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    /**
     * Ejecuta el caso de uso para obtener un usuario por ID
     * 
     * @param id El ID del usuario a buscar
     * @return Optional con el usuario si existe, Optional.empty() si no existe
     * @throws IllegalArgumentException Si el ID es inválido
     */
    public Optional<Usuario> ejecutar(Long id) {
        validarId(id);
        return usuarioService.obtenerUsuarioPorId(id);
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