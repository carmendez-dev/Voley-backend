package com.voley.adapter;

import com.voley.domain.CategoriaEquipo;
import com.voley.domain.Categoria;
import com.voley.domain.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad CategoriaEquipo
 * 
 * Maneja las relaciones many-to-many entre categorías y equipos:
 * - Consultas de asociación/desasociación
 * - Búsquedas de equipos por categoría
 * - Búsquedas de categorías por equipo
 * - Validaciones de existencia de relaciones
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Repository
public interface CategoriaEquipoJpaRepository extends JpaRepository<CategoriaEquipo, Long> {
    
    /**
     * Encuentra todos los equipos asociados a una categoría específica
     * @param idCategoria ID de la categoría
     * @return Lista de relaciones CategoriaEquipo para esa categoría
     */
    @Query("SELECT ce FROM CategoriaEquipo ce WHERE ce.categoria.idCategoria = :idCategoria")
    List<CategoriaEquipo> findByCategoriaIdCategoria(@Param("idCategoria") Long idCategoria);
    
    /**
     * Encuentra todas las categorías asociadas a un equipo específico
     * @param idEquipo ID del equipo
     * @return Lista de relaciones CategoriaEquipo para ese equipo
     */
    @Query("SELECT ce FROM CategoriaEquipo ce WHERE ce.equipo.idEquipo = :idEquipo")
    List<CategoriaEquipo> findByEquipoIdEquipo(@Param("idEquipo") Long idEquipo);
    
    /**
     * Verifica si existe una relación entre una categoría y un equipo
     * @param idCategoria ID de la categoría
     * @param idEquipo ID del equipo
     * @return true si existe la relación
     */
    @Query("SELECT COUNT(ce) > 0 FROM CategoriaEquipo ce WHERE ce.categoria.idCategoria = :idCategoria AND ce.equipo.idEquipo = :idEquipo")
    boolean existsByCategoriaIdCategoriaAndEquipoIdEquipo(@Param("idCategoria") Long idCategoria, @Param("idEquipo") Long idEquipo);
    
    /**
     * Encuentra una relación específica entre categoría y equipo
     * @param idCategoria ID de la categoría
     * @param idEquipo ID del equipo
     * @return Optional con la relación si existe
     */
    @Query("SELECT ce FROM CategoriaEquipo ce WHERE ce.categoria.idCategoria = :idCategoria AND ce.equipo.idEquipo = :idEquipo")
    Optional<CategoriaEquipo> findByCategoriaIdCategoriaAndEquipoIdEquipo(@Param("idCategoria") Long idCategoria, @Param("idEquipo") Long idEquipo);
    
    /**
     * Cuenta cuántos equipos tiene una categoría
     * @param idCategoria ID de la categoría
     * @return Número de equipos en la categoría
     */
    @Query("SELECT COUNT(ce) FROM CategoriaEquipo ce WHERE ce.categoria.idCategoria = :idCategoria")
    long countEquiposByCategoria(@Param("idCategoria") Long idCategoria);
    
    /**
     * Cuenta en cuántas categorías participa un equipo
     * @param idEquipo ID del equipo
     * @return Número de categorías del equipo
     */
    @Query("SELECT COUNT(ce) FROM CategoriaEquipo ce WHERE ce.equipo.idEquipo = :idEquipo")
    long countCategoriasByEquipo(@Param("idEquipo") Long idEquipo);
    
    /**
     * Elimina todas las relaciones de una categoría específica
     * @param idCategoria ID de la categoría
     */
    @Query("DELETE FROM CategoriaEquipo ce WHERE ce.categoria.idCategoria = :idCategoria")
    void deleteByCategoriaIdCategoria(@Param("idCategoria") Long idCategoria);
    
    /**
     * Elimina todas las relaciones de un equipo específico
     * @param idEquipo ID del equipo
     */
    @Query("DELETE FROM CategoriaEquipo ce WHERE ce.equipo.idEquipo = :idEquipo")
    void deleteByEquipoIdEquipo(@Param("idEquipo") Long idEquipo);
    
    /**
     * Obtiene equipos de una categoría con información completa
     * Incluye datos tanto del equipo como de la categoría
     * @param idCategoria ID de la categoría
     * @return Lista de relaciones con datos completos
     */
    @Query("SELECT ce FROM CategoriaEquipo ce " +
           "JOIN FETCH ce.equipo " +
           "JOIN FETCH ce.categoria " +
           "WHERE ce.categoria.idCategoria = :idCategoria " +
           "ORDER BY ce.equipo.nombre ASC")
    List<CategoriaEquipo> findEquiposConDatosByCategoria(@Param("idCategoria") Long idCategoria);
    
    /**
     * Obtiene categorías de un equipo con información completa
     * @param idEquipo ID del equipo
     * @return Lista de relaciones con datos completos
     */
    @Query("SELECT ce FROM CategoriaEquipo ce " +
           "JOIN FETCH ce.categoria " +
           "JOIN FETCH ce.equipo " +
           "WHERE ce.equipo.idEquipo = :idEquipo " +
           "ORDER BY ce.categoria.nombre ASC")
    List<CategoriaEquipo> findCategoriasConDatosByEquipo(@Param("idEquipo") Long idEquipo);
}