package com.voley.repository;

import com.voley.domain.Equipo;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz del repositorio para la entidad Equipo
 * 
 * Define las operaciones de persistencia para equipos siguiendo
 * los principios de Clean Architecture. Esta interfaz pertenece
 * a la capa de dominio y será implementada por adaptadores.
 * 
 * @author Sistema Voley
 * @version 1.0
 */
public interface EquipoRepository {
    
    /**
     * Guarda un equipo (crear o actualizar)
     * @param equipo Equipo a guardar
     * @return Equipo guardado con ID asignado
     */
    Equipo guardar(Equipo equipo);
    
    /**
     * Busca un equipo por su ID
     * @param id ID del equipo
     * @return Optional con el equipo si existe
     */
    Optional<Equipo> buscarPorId(Long id);
    
    /**
     * Obtiene todos los equipos ordenados por nombre
     * @return Lista de todos los equipos
     */
    List<Equipo> buscarTodos();
    
    /**
     * Elimina un equipo por ID
     * @param id ID del equipo a eliminar
     */
    void eliminar(Long id);
    
    /**
     * Verifica si existe un equipo con el ID dado
     * @param id ID a verificar
     * @return true si existe el equipo
     */
    boolean existePorId(Long id);
    
    /**
     * Busca equipos por nombre (búsqueda parcial)
     * @param nombre Nombre o parte del nombre a buscar
     * @return Lista de equipos que coinciden
     */
    List<Equipo> buscarPorNombre(String nombre);
    
    /**
     * Busca un equipo por nombre exacto
     * @param nombre Nombre exacto del equipo
     * @return Optional con el equipo si existe
     */
    Optional<Equipo> buscarPorNombreExacto(String nombre);
    
    /**
     * Verifica si existe un equipo con el nombre dado
     * @param nombre Nombre a verificar
     * @return true si existe un equipo con ese nombre
     */
    boolean existePorNombre(String nombre);
    
    /**
     * Verifica si existe otro equipo con el mismo nombre (para validación en updates)
     * @param nombre Nombre a verificar
     * @param id ID del equipo a excluir de la búsqueda
     * @return true si existe otro equipo con ese nombre
     */
    boolean existePorNombreExcluyendoId(String nombre, Long id);
    
    /**
     * Busca equipos por texto en nombre o descripción
     * @param texto Texto a buscar
     * @return Lista de equipos que coinciden
     */
    List<Equipo> buscarPorTexto(String texto);
    
    /**
     * Cuenta el total de equipos
     * @return Número total de equipos
     */
    long contarTotal();
}