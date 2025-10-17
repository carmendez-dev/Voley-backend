package com.voley.repository;

import com.voley.domain.CategoriaEquipo;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz del repositorio para la entidad CategoriaEquipo
 * 
 * Define las operaciones de persistencia para las relaciones
 * entre categorías y equipos siguiendo Clean Architecture.
 * 
 * @author Sistema Voley
 * @version 1.0
 */
public interface CategoriaEquipoRepository {
    
    /**
     * Guarda una relación categoría-equipo
     * @param categoriaEquipo Relación a guardar
     * @return Relación guardada con ID asignado
     */
    CategoriaEquipo guardar(CategoriaEquipo categoriaEquipo);
    
    /**
     * Busca una relación por su ID
     * @param id ID de la relación
     * @return Optional con la relación si existe
     */
    Optional<CategoriaEquipo> buscarPorId(Long id);
    
    /**
     * Obtiene todas las relaciones
     * @return Lista de todas las relaciones
     */
    List<CategoriaEquipo> buscarTodos();
    
    /**
     * Elimina una relación por ID
     * @param id ID de la relación a eliminar
     */
    void eliminar(Long id);
    
    /**
     * Verifica si existe una relación con el ID dado
     * @param id ID a verificar
     * @return true si existe la relación
     */
    boolean existePorId(Long id);
    
    /**
     * Busca todos los equipos de una categoría específica
     * @param idCategoria ID de la categoría
     * @return Lista de relaciones con equipos de esa categoría
     */
    List<CategoriaEquipo> buscarEquiposPorCategoria(Long idCategoria);
    
    /**
     * Busca todas las categorías de un equipo específico
     * @param idEquipo ID del equipo
     * @return Lista de relaciones con categorías de ese equipo
     */
    List<CategoriaEquipo> buscarCategoriasPorEquipo(Long idEquipo);
    
    /**
     * Verifica si existe una relación entre categoría y equipo
     * @param idCategoria ID de la categoría
     * @param idEquipo ID del equipo
     * @return true si existe la relación
     */
    boolean existeRelacion(Long idCategoria, Long idEquipo);
    
    /**
     * Busca una relación específica entre categoría y equipo
     * @param idCategoria ID de la categoría
     * @param idEquipo ID del equipo
     * @return Optional con la relación si existe
     */
    Optional<CategoriaEquipo> buscarRelacion(Long idCategoria, Long idEquipo);
    
    /**
     * Elimina una relación específica entre categoría y equipo
     * @param idCategoria ID de la categoría
     * @param idEquipo ID del equipo
     */
    void eliminarRelacion(Long idCategoria, Long idEquipo);
    
    /**
     * Cuenta cuántos equipos tiene una categoría
     * @param idCategoria ID de la categoría
     * @return Número de equipos en la categoría
     */
    long contarEquiposPorCategoria(Long idCategoria);
    
    /**
     * Cuenta en cuántas categorías participa un equipo
     * @param idEquipo ID del equipo
     * @return Número de categorías del equipo
     */
    long contarCategoriasPorEquipo(Long idEquipo);
    
    /**
     * Elimina todas las relaciones de una categoría
     * @param idCategoria ID de la categoría
     */
    void eliminarTodasRelacionesCategoria(Long idCategoria);
    
    /**
     * Elimina todas las relaciones de un equipo
     * @param idEquipo ID del equipo
     */
    void eliminarTodasRelacionesEquipo(Long idEquipo);
}