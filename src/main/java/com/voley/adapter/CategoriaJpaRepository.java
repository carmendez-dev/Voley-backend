package com.voley.adapter;

import com.voley.domain.Categoria;
import com.voley.domain.GeneroCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Categoria
 * Proporciona operaciones de acceso a datos para categorías
 */
@Repository
public interface CategoriaJpaRepository extends JpaRepository<Categoria, Long> {
    
    /**
     * Busca categorías por género
     */
    List<Categoria> findByGenero(GeneroCategoria genero);
    
    /**
     * Busca categorías por nombre (case insensitive)
     */
    @Query("SELECT c FROM Categoria c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Categoria> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);
    
    /**
     * Busca una categoría por nombre exacto (case insensitive)
     */
    @Query("SELECT c FROM Categoria c WHERE LOWER(c.nombre) = LOWER(:nombre)")
    Optional<Categoria> findByNombreIgnoreCase(@Param("nombre") String nombre);
    
    /**
     * Verifica si existe una categoría con el nombre dado (excluyendo una específica)
     */
    @Query("SELECT COUNT(c) > 0 FROM Categoria c WHERE LOWER(c.nombre) = LOWER(:nombre) AND c.idCategoria != :id")
    boolean existsByNombreIgnoreCaseAndIdNot(@Param("nombre") String nombre, @Param("id") Long id);
    
    /**
     * Verifica si existe una categoría con el nombre dado
     */
    @Query("SELECT COUNT(c) > 0 FROM Categoria c WHERE LOWER(c.nombre) = LOWER(:nombre)")
    boolean existsByNombreIgnoreCase(@Param("nombre") String nombre);
    
    /**
     * Obtiene todas las categorías ordenadas por nombre
     */
    @Query("SELECT c FROM Categoria c ORDER BY c.nombre ASC")
    List<Categoria> findAllOrderByNombre();
    
    /**
     * Obtiene categorías por género ordenadas por nombre
     */
    @Query("SELECT c FROM Categoria c WHERE c.genero = :genero ORDER BY c.nombre ASC")
    List<Categoria> findByGeneroOrderByNombre(@Param("genero") GeneroCategoria genero);
    
    /**
     * Cuenta categorías por género
     */
    @Query("SELECT COUNT(c) FROM Categoria c WHERE c.genero = :genero")
    long countByGenero(@Param("genero") GeneroCategoria genero);
    
    /**
     * Obtiene estadísticas de categorías por género
     */
    @Query("SELECT c.genero, COUNT(c) FROM Categoria c GROUP BY c.genero")
    List<Object[]> getEstadisticasPorGenero();
    
    /**
     * Verifica si existe una categoría con el mismo nombre y género
     */
    @Query("SELECT COUNT(c) > 0 FROM Categoria c WHERE LOWER(c.nombre) = LOWER(:nombre) AND c.genero = :genero")
    boolean existsByNombreAndGenero(@Param("nombre") String nombre, @Param("genero") GeneroCategoria genero);
    
    /**
     * Verifica si existe una categoría con el mismo nombre y género (excluyendo una específica)
     */
    @Query("SELECT COUNT(c) > 0 FROM Categoria c WHERE LOWER(c.nombre) = LOWER(:nombre) AND c.genero = :genero AND c.idCategoria != :id")
    boolean existsByNombreAndGeneroAndIdNot(@Param("nombre") String nombre, @Param("genero") GeneroCategoria genero, @Param("id") Long id);
}