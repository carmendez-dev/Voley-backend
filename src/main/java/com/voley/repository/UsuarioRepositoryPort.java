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
     * @param usuario el usuario a guardar
     * @return el usuario guardado con su ID asignado
     */
    Usuario save(Usuario usuario);
    
    /**
     * Busca un usuario por su ID
     * @param id el ID del usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> findById(Long id);
    
    /**
     * Busca un usuario por su cédula
     * @param cedula la cédula del usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> findByCedula(String cedula);
    
    /**
     * Busca un usuario por su email
     * @param email el email del usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> findByEmail(String email);
    
    /**
     * Obtiene todos los usuarios
     * @return lista de todos los usuarios
     */
    List<Usuario> findAll();
    
    /**
     * Busca usuarios por tipo (jugador/profesor)
     * @param tipo el tipo de usuario
     * @return lista de usuarios del tipo especificado
     */
    List<Usuario> findByTipo(Usuario.TipoUsuario tipo);
    
    /**
     * Busca usuarios por estado (Activo/Inactivo)
     * @param estado el estado del usuario
     * @return lista de usuarios con el estado especificado
     */
    List<Usuario> findByEstado(Usuario.EstadoUsuario estado);
    
    /**
     * Elimina un usuario por su ID
     * @param id el ID del usuario a eliminar
     */
    void deleteById(Long id);
    
    /**
     * Verifica si existe un usuario con la cédula especificada
     * @param cedula la cédula a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByCedula(String cedula);
    
    /**
     * Verifica si existe un usuario con el email especificado
     * @param email el email a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmail(String email);
}