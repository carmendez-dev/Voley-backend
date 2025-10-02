package com.voley.application.usuarios;

import com.voley.domain.Usuario;
import com.voley.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para crear un nuevo usuario
 * Encapsula la lógica de negocio para la creación de usuarios
 */
@Component
public class CrearUsuarioUseCase {
    
    private final UsuarioService usuarioService;
    
    @Autowired
    public CrearUsuarioUseCase(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    /**
     * Ejecuta el caso de uso para crear un usuario
     * 
     * @param usuario El usuario a crear
     * @return El usuario creado con ID asignado
     * @throws IllegalArgumentException Si los datos del usuario son inválidos
     */
    public Usuario ejecutar(Usuario usuario) {
        // Validaciones específicas del caso de uso
        validarDatosUsuario(usuario);
        
        // Delegar al servicio la lógica de creación
        return usuarioService.crearUsuario(usuario);
    }
    
    private void validarDatosUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        
        if (usuario.getNombres() == null || usuario.getNombres().trim().isEmpty()) {
            throw new IllegalArgumentException("Los nombres del usuario son obligatorios");
        }
        
        if (usuario.getCedula() == null || usuario.getCedula().trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del usuario es obligatoria");
        }
        
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email del usuario es obligatorio");
        }
        
        if (usuario.getCelular() == null || usuario.getCelular().trim().isEmpty()) {
            throw new IllegalArgumentException("El celular del usuario es obligatorio");
        }
    }
}