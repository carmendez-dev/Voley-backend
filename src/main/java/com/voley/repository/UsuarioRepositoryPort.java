package com.voley.repository;

import com.voley.domain.Usuario;
import java.util.List;
import java.util.Optional;

/**
 * Puerto (Interface) para el repositorio de Usuario
 * Define las operaciones de persistencia sin depender de tecnología específica
 */
public interface UsuarioRepositoryPort {
    
    /**
     * Guarda un usuario en la base de datos
     * usuario el usuario a guardar
     * el usuario guardado con su ID asignado
     */
    Usuario save(Usuario usuario);
    
    /**
     * Busca un usuario por su ID
     * id el ID del usuario
     * Optional con el usuario si existe
     */
    Optional<Usuario> findById(Long id);
    
    /**
     * Busca un usuario por su cédula
     * cedula la cédula del usuario
     * Optional con el usuario si existe
     */
    Optional<Usuario> findByCedula(String cedula);
    
    /**
     * Busca un usuario por su email
     * email el email del usuario
     * Optional con el usuario si existe
     */
    Optional<Usuario> findByEmail(String email);
    
    /**
     * Obtiene todos los usuarios
     * lista de todos los usuarios
     */
    List<Usuario> findAll();
    
    /**
     * Busca usuarios por tipo (jugador/profesor)
     * tipo el tipo de usuario
     * lista de usuarios del tipo especificado
     */
    List<Usuario> findByTipo(Usuario.TipoUsuario tipo);
    
    /**
     * Busca usuarios por estado (Activo/Inactivo)
     * estado el estado del usuario
     * lista de usuarios con el estado especificado
     */
    List<Usuario> findByEstado(Usuario.EstadoUsuario estado);
    
    /**
     * Elimina un usuario por su ID
     * id el ID del usuario a eliminar
     */
    void deleteById(Long id);
    
    /**
     * Verifica si existe un usuario con la cédula especificada
     * cedula la cédula a verificar
     * true si existe, false en caso contrario
     */
    boolean existsByCedula(String cedula);
    
    /**
     * Verifica si existe un usuario con el email especificado
     * email el email a verificar
     * return true si existe, false en caso contrario
     */
    boolean existsByEmail(String email);
}