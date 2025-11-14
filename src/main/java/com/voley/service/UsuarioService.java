package com.voley.service;

import com.voley.domain.Usuario;
import com.voley.dto.UsuarioRequestDTO;
import com.voley.dto.UsuarioResponseDTO;
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
     * Crea un nuevo usuario usando DTO con validación automática
     * @param usuarioDTO el DTO con los datos del usuario
     * @return el DTO de respuesta con datos calculados
     * @throws IllegalArgumentException si los datos son inválidos
     */
    UsuarioResponseDTO crearUsuarioConDTO(UsuarioRequestDTO usuarioDTO);
    
    /**
     * Actualiza un usuario existente usando DTO
     * @param id el ID del usuario a actualizar
     * @param usuarioDTO el DTO con los nuevos datos
     * @return el DTO de respuesta actualizado
     * @throws IllegalArgumentException si el usuario no existe
     */
    UsuarioResponseDTO actualizarUsuarioConDTO(Long id, UsuarioRequestDTO usuarioDTO);
    
    /**
     * Obtiene un usuario completo con datos calculados
     * @param id el ID del usuario
     * @return el DTO de respuesta completo o Optional.empty() si no existe
     */
    Optional<UsuarioResponseDTO> obtenerUsuarioCompletoDTO(Long id);
    
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
     * @return lista de usuarios
     */
    List<Usuario> obtenerTodosLosUsuarios();
    
    /**
     * Obtiene todos los usuarios como DTOs con datos completos
     * @return lista de DTOs de respuesta
     */
    List<UsuarioResponseDTO> obtenerTodosLosUsuariosDTO();
    
    /**
     * Obtiene usuarios por estado usando DTOs
     * @param estado el estado del usuario
     * @return lista de DTOs de respuesta filtrada por estado
     */
    List<UsuarioResponseDTO> obtenerUsuariosPorEstadoDTO(Usuario.EstadoUsuario estado);
    
    /**
     * Obtiene usuarios por estado
     * @param estado el estado del usuario
     * @return lista de usuarios con el estado especificado
     */
    List<Usuario> obtenerUsuariosPorEstado(Usuario.EstadoUsuario estado);
    
    /**
     * Obtiene usuarios por tipo (método de compatibilidad)
     * @param tipo el tipo de usuario
     * @return lista de usuarios del tipo especificado
     */
    List<Usuario> obtenerUsuariosPorTipo(Usuario.TipoUsuario tipo);
    
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
    
    /**
     * Obtiene usuarios por peso en un rango
     * @param pesoMin peso mínimo
     * @param pesoMax peso máximo
     * @return lista de usuarios en el rango de peso
     */
    List<Usuario> obtenerUsuariosPorRangoPeso(Float pesoMin, Float pesoMax);
    
    /**
     * Obtiene usuarios por altura en un rango
     * @param alturaMin altura mínima
     * @param alturaMax altura máxima
     * @return lista de usuarios en el rango de altura
     */
    List<Usuario> obtenerUsuariosPorRangoAltura(Float alturaMin, Float alturaMax);
    
    /**
     * Busca usuarios por nombre completo (primer nombre, segundo nombre o tercer nombre)
     * @param nombre nombre a buscar
     * @return lista de usuarios que coinciden
     */
    List<Usuario> buscarUsuariosPorNombres(String nombre);
    
    /**
     * Busca usuarios por apellido completo (primer apellido o segundo apellido)
     * @param apellido apellido a buscar
     * @return lista de usuarios que coinciden
     */
    List<Usuario> buscarUsuariosPorApellidos(String apellido);
    
    /**
     * Obtiene estadísticas básicas de usuarios (totales por tipo, género, etc.)
     * @return mapa con estadísticas
     */
    java.util.Map<String, Object> obtenerEstadisticasUsuarios();
}