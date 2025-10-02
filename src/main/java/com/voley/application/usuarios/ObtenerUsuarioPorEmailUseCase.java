package com.voley.application.usuarios;

import com.voley.domain.Usuario;
import com.voley.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Caso de uso para obtener un usuario por email
 * Encapsula la lógica de negocio para la búsqueda de usuarios por email
 */
@Component
public class ObtenerUsuarioPorEmailUseCase {
    
    private final UsuarioService usuarioService;
    
    @Autowired
    public ObtenerUsuarioPorEmailUseCase(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    /**
     * Ejecuta el caso de uso para obtener un usuario por email
     * 
     * @param email El email del usuario a buscar
     * @return Optional con el usuario si existe, Optional.empty() si no existe
     * @throws IllegalArgumentException Si el email es inválido
     */
    public Optional<Usuario> ejecutar(String email) {
        validarEmail(email);
        return usuarioService.obtenerUsuarioPorEmail(email);
    }
    
    private void validarEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("El email no puede ser nulo");
        }
        
        if (email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        
        // Validación básica de formato de email
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("El formato del email no es válido");
        }
    }
}