package com.voley.adapter;

import com.voley.domain.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Equipo
 * 
 * Proporciona operaciones de base de datos para equipos:
 * - CRUD básico heredado de JpaRepository
 * - Consultas personalizadas para búsquedas específicas
 * - Validaciones de existencia
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Repository
public interface EquipoJpaRepository extends JpaRepository<Equipo, Long> {
    
    /**
     * Busca equipos por nombre (búsqueda parcial, case-insensitive)
     * @param nombre Nombre o parte del nombre a buscar
     * @return Lista de equipos que coinciden con el criterio
     */
    @Query("SELECT e FROM Equipo e WHERE LOWER(e.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Equipo> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);
    
    /**
     * Busca un equipo por nombre exacto (case-insensitive)
     * @param nombre Nombre exacto del equipo
     * @return Optional con el equipo si existe
     */
    @Query("SELECT e FROM Equipo e WHERE LOWER(e.nombre) = LOWER(:nombre)")
    Optional<Equipo> findByNombreIgnoreCase(@Param("nombre") String nombre);
    
    /**
     * Verifica si existe un equipo con el nombre dado (case-insensitive)
     * @param nombre Nombre a verificar
     * @return true si existe un equipo con ese nombre
     */
    @Query("SELECT COUNT(e) > 0 FROM Equipo e WHERE LOWER(e.nombre) = LOWER(:nombre)")
    boolean existsByNombreIgnoreCase(@Param("nombre") String nombre);
    
    /**
     * Verifica si existe un equipo con el nombre dado, excluyendo un ID específico
     * Útil para validaciones en actualizaciones
     * @param nombre Nombre a verificar
     * @param idEquipo ID del equipo a excluir de la búsqueda
     * @return true si existe otro equipo con ese nombre
     */
    @Query("SELECT COUNT(e) > 0 FROM Equipo e WHERE LOWER(e.nombre) = LOWER(:nombre) AND e.idEquipo != :idEquipo")
    boolean existsByNombreIgnoreCaseAndIdEquipoNot(@Param("nombre") String nombre, @Param("idEquipo") Long idEquipo);
    
    /**
     * Busca equipos que contengan texto en nombre o descripción
     * @param texto Texto a buscar en nombre o descripción
     * @return Lista de equipos que coinciden con el criterio
     */
    @Query("SELECT e FROM Equipo e WHERE LOWER(e.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) " +
           "OR LOWER(e.descripcion) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Equipo> findByNombreOrDescripcionContainingIgnoreCase(@Param("texto") String texto);
    
    /**
     * Obtiene todos los equipos ordenados por nombre
     * @return Lista de equipos ordenada alfabéticamente
     */
    @Query("SELECT e FROM Equipo e ORDER BY e.nombre ASC")
    List<Equipo> findAllOrderByNombre();
    
    /**
     * Cuenta el total de equipos en el sistema
     * @return Número total de equipos
     */
    @Query("SELECT COUNT(e) FROM Equipo e")
    long countTotalEquipos();
}