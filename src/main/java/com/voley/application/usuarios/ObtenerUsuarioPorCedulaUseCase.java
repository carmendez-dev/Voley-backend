package com.voley.application.usuarios;

import com.voley.domain.Usuario;
import com.voley.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Caso de uso para obtener un usuario por cédula
 * Encapsula la lógica de negocio para la búsqueda de usuarios por cédula
 */
@Component
public class ObtenerUsuarioPorCedulaUseCase {
    
    private final UsuarioService usuarioService;
    
    @Autowired
    public ObtenerUsuarioPorCedulaUseCase(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    /**
     * Ejecuta el caso de uso para obtener un usuario por cédula
     * 
     * @param cedula La cédula del usuario a buscar
     * @return Optional con el usuario si existe, Optional.empty() si no existe
     * @throws IllegalArgumentException Si la cédula es inválida
     */
    public Optional<Usuario> ejecutar(String cedula) {
        validarCedula(cedula);
        return usuarioService.obtenerUsuarioPorCedula(cedula);
    }
    
    private void validarCedula(String cedula) {
        if (cedula == null) {
            throw new IllegalArgumentException("La cédula no puede ser nula");
        }
        
        if (cedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula no puede estar vacía");
        }
        
        // Validación básica de formato de cédula (ajustar según el país)
        if (!cedula.matches("\\d{8,15}")) {
            throw new IllegalArgumentException("La cédula debe contener entre 8 y 15 dígitos");
        }
    }
}