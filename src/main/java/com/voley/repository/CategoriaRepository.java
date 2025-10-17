package com.voley.repository;

import com.voley.domain.Categoria;
import com.voley.domain.GeneroCategoria;

import java.util.List;
import java.util.Optional;

/**
 * Interface del repositorio de categorías (Domain Layer)
 * Define las operaciones de persistencia para categorías
 */
public interface CategoriaRepository {
    
    /**
     * Guarda una categoría (crear o actualizar)
     */
    Categoria guardar(Categoria categoria);
    
    /**
     * Busca una categoría por su ID
     */
    Optional<Categoria> buscarPorId(Long id);
    
    /**
     * Obtiene todas las categorías ordenadas por nombre
     */
    List<Categoria> buscarTodas();
    
    /**
     * Busca categorías por género
     */
    List<Categoria> buscarPorGenero(GeneroCategoria genero);
    
    /**
     * Busca categorías que contengan el nombre dado
     */
    List<Categoria> buscarPorNombre(String nombre);
    
    /**
     * Busca una categoría por nombre exacto
     */
    Optional<Categoria> buscarPorNombreExacto(String nombre);
    
    /**
     * Verifica si existe una categoría con el nombre dado
     */
    boolean existePorNombre(String nombre);
    
    /**
     * Verifica si existe una categoría con el nombre dado (excluyendo una específica)
     */
    boolean existePorNombre(String nombre, Long idExcluir);
    
    /**
     * Elimina una categoría por ID
     */
    void eliminar(Long id);
    
    /**
     * Elimina una categoría
     */
    void eliminar(Categoria categoria);
    
    /**
     * Verifica si existe una categoría con el ID dado
     */
    boolean existe(Long id);
    
    /**
     * Cuenta el total de categorías
     */
    long contar();
    
    /**
     * Cuenta categorías por género
     */
    long contarPorGenero(GeneroCategoria genero);
}