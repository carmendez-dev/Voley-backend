package com.voley.adapter;

import com.voley.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador JPA que extiende JpaRepository
 * Proporciona métodos de acceso a datos usando Spring Data JPA
 */
@Repository
public interface UsuarioJpaRepository extends JpaRepository<Usuario, Long> {
    
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
     * Busca usuarios por tipo
     * @param tipo el tipo de usuario
     * @return lista de usuarios del tipo especificado
     */
    List<Usuario> findByTipo(Usuario.TipoUsuario tipo);
    
    /**
     * Busca usuarios por estado
     * @param estado el estado del usuario
     * @return lista de usuarios con el estado especificado
     */
    List<Usuario> findByEstado(Usuario.EstadoUsuario estado);
    
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