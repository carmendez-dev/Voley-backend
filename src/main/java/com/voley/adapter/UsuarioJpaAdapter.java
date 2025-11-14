package com.voley.adapter;

import com.voley.domain.Usuario;
import com.voley.repository.UsuarioRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador que implementa el puerto UsuarioRepositoryPort
 * Conecta la lógica de negocio con la infraestructura JPA
 */
@Component
public class UsuarioJpaAdapter implements UsuarioRepositoryPort {
    
    private final UsuarioJpaRepository usuarioJpaRepository;
    
    @Autowired
    public UsuarioJpaAdapter(UsuarioJpaRepository usuarioJpaRepository) {
        this.usuarioJpaRepository = usuarioJpaRepository;
    }
    
    @Override
    public Usuario save(Usuario usuario) {
        return usuarioJpaRepository.save(usuario);
    }
    
    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioJpaRepository.findById(id);
    }
    
    @Override
    public Optional<Usuario> findByCedula(String cedula) {
        return usuarioJpaRepository.findByCedula(cedula);
    }
    
    @Override
    public Optional<Usuario> findByEmail(String email) {
        return usuarioJpaRepository.findByEmail(email);
    }
    
    @Override
    public List<Usuario> findAll() {
        return usuarioJpaRepository.findAll();
    }
    
    @Override
    public List<Usuario> findByEstado(Usuario.EstadoUsuario estado) {
        return usuarioJpaRepository.findByEstado(estado);
    }
    
    @Override
    public List<Usuario> findByPesoBetween(Float pesoMin, Float pesoMax) {
        return usuarioJpaRepository.findByPesoBetween(pesoMin, pesoMax);
    }
    
    @Override
    public List<Usuario> findByAlturaBetween(Float alturaMin, Float alturaMax) {
        return usuarioJpaRepository.findByAlturaBetween(alturaMin, alturaMax);
    }
    
    @Override
    public List<Usuario> findByPrimerNombreContainingIgnoreCaseOrSegundoNombreContainingIgnoreCaseOrTercerNombreContainingIgnoreCase(
            String primerNombre, String segundoNombre, String tercerNombre) {
        return usuarioJpaRepository.findByPrimerNombreContainingIgnoreCaseOrSegundoNombreContainingIgnoreCaseOrTercerNombreContainingIgnoreCase(
            primerNombre, segundoNombre, tercerNombre);
    }
    
    @Override
    public List<Usuario> findByPrimerApellidoContainingIgnoreCaseOrSegundoApellidoContainingIgnoreCase(
            String primerApellido, String segundoApellido) {
        return usuarioJpaRepository.findByPrimerApellidoContainingIgnoreCaseOrSegundoApellidoContainingIgnoreCase(
            primerApellido, segundoApellido);
    }
    
    @Override
    public void deleteById(Long id) {
        usuarioJpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsByCedula(String cedula) {
        return usuarioJpaRepository.existsByCedula(cedula);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return usuarioJpaRepository.existsByEmail(email);
    }
}