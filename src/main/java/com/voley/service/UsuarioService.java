package com.voley.service;

import com.voley.domain.Usuario;
import java.util.List;
import java.util.Optional;

/**
 * Interface del servicio de Usuario
 * Define las operaciones de lógica de negocio
 */
public interface UsuarioService {
    
    /**
     * Crea un nuevo usuario
     * @param usuario el usuario a crear
     * @return el usuario creado
     * @throws IllegalArgumentException si la cédula o email ya existen
     */
    Usuario crearUsuario(Usuario usuario);
    
    /**
     * Obtiene un usuario por su ID
     * @param id el ID del usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> obtenerUsuarioPorId(Long id);
    
    /**
     * Obtiene un usuario por su cédula
     * @param cedula la cédula del usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> obtenerUsuarioPorCedula(String cedula);
    
    /**
     * Obtiene un usuario por su email
     * @param email el email del usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> obtenerUsuarioPorEmail(String email);
    
    /**
     * Obtiene todos los usuarios
     * @return lista de todos los usuarios
     */
    List<Usuario> obtenerTodosLosUsuarios();
    
    /**
     * Obtiene usuarios por tipo
     * @param tipo el tipo de usuario
     * @return lista de usuarios del tipo especificado
     */
    List<Usuario> obtenerUsuariosPorTipo(Usuario.TipoUsuario tipo);
    
    /**
     * Obtiene usuarios por estado
     * @param estado el estado del usuario
     * @return lista de usuarios con el estado especificado
     */
    List<Usuario> obtenerUsuariosPorEstado(Usuario.EstadoUsuario estado);
    
    /**
     * Actualiza un usuario existente
     * @param id el ID del usuario a actualizar
     * @param usuarioActualizado los datos actualizados del usuario
     * @return el usuario actualizado
     * @throws IllegalArgumentException si el usuario no existe
     */
    Usuario actualizarUsuario(Long id, Usuario usuarioActualizado);
    
    /**
     * Elimina un usuario por su ID
     * @param id el ID del usuario a eliminar
     * @throws IllegalArgumentException si el usuario no existe
     */
    void eliminarUsuario(Long id);
    
    /**
     * Cambia el estado de un usuario
     * @param id el ID del usuario
     * @param nuevoEstado el nuevo estado
     * @return el usuario con el estado actualizado
     * @throws IllegalArgumentException si el usuario no existe
     */
    Usuario cambiarEstadoUsuario(Long id, Usuario.EstadoUsuario nuevoEstado);
}