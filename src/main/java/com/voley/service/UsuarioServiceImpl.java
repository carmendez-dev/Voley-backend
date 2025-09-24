package com.voley.service;

import com.voley.domain.Usuario;
import com.voley.repository.UsuarioRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de Usuario
 * Contiene la lógica de negocio
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {
    
    private final UsuarioRepositoryPort usuarioRepository;
    
    @Autowired
    public UsuarioServiceImpl(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    
    @Override
    public Usuario crearUsuario(Usuario usuario) {
        // Validar que la cédula no esté en uso
        if (usuarioRepository.existsByCedula(usuario.getCedula())) {
            throw new IllegalArgumentException("Ya existe un usuario con la cédula: " + usuario.getCedula());
        }
        
        // Validar que el email no esté en uso
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + usuario.getEmail());
        }
        
        return usuarioRepository.save(usuario);
    }
    
    @Override
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }
    
    @Override
    public Optional<Usuario> obtenerUsuarioPorCedula(String cedula) {
        return usuarioRepository.findByCedula(cedula);
    }
    
    @Override
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    @Override
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }
    
    @Override
    public List<Usuario> obtenerUsuariosPorTipo(Usuario.TipoUsuario tipo) {
        return usuarioRepository.findByTipo(tipo);
    }
    
    @Override
    public List<Usuario> obtenerUsuariosPorEstado(Usuario.EstadoUsuario estado) {
        return usuarioRepository.findByEstado(estado);
    }
    
    @Override
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
        
        if (usuarioExistente.isEmpty()) {
            throw new IllegalArgumentException("No existe un usuario con ID: " + id);
        }
        
        Usuario usuario = usuarioExistente.get();
        
        // Validar cédula si se está cambiando
        if (!usuario.getCedula().equals(usuarioActualizado.getCedula()) 
            && usuarioRepository.existsByCedula(usuarioActualizado.getCedula())) {
            throw new IllegalArgumentException("Ya existe un usuario con la cédula: " + usuarioActualizado.getCedula());
        }
        
        // Validar email si se está cambiando
        if (!usuario.getEmail().equals(usuarioActualizado.getEmail()) 
            && usuarioRepository.existsByEmail(usuarioActualizado.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + usuarioActualizado.getEmail());
        }
        
        // Actualizar campos
        usuario.setNombres(usuarioActualizado.getNombres());
        usuario.setApellidos(usuarioActualizado.getApellidos());
        usuario.setFechaNacimiento(usuarioActualizado.getFechaNacimiento());
        usuario.setCedula(usuarioActualizado.getCedula());
        usuario.setGenero(usuarioActualizado.getGenero());
        usuario.setEmail(usuarioActualizado.getEmail());
        usuario.setCelular(usuarioActualizado.getCelular());
        usuario.setDireccion(usuarioActualizado.getDireccion());
        usuario.setContactoEmergencia(usuarioActualizado.getContactoEmergencia());
        usuario.setTipo(usuarioActualizado.getTipo());
        usuario.setEstado(usuarioActualizado.getEstado());
        
        return usuarioRepository.save(usuario);
    }
    
    @Override
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("No existe un usuario con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
    
    @Override
    public Usuario cambiarEstadoUsuario(Long id, Usuario.EstadoUsuario nuevoEstado) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        
        if (usuarioOptional.isEmpty()) {
            throw new IllegalArgumentException("No existe un usuario con ID: " + id);
        }
        
        Usuario usuario = usuarioOptional.get();
        usuario.setEstado(nuevoEstado);
        
        return usuarioRepository.save(usuario);
    }
}