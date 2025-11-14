package com.voley.adapter;

import com.voley.domain.TorneoCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad TorneoCategoria
 * Maneja las relaciones entre torneos y categorías
 */
@Repository
public interface TorneoCategoriaJpaRepository extends JpaRepository<TorneoCategoria, Long> {
    
    /**
     * Busca todas las categorías de un torneo específico
     */
    List<TorneoCategoria> findByTorneoId(Long torneoId);
    
    /**
     * Busca todos los torneos de una categoría específica
     */
    List<TorneoCategoria> findByCategoriaIdCategoria(Long categoriaId);
    
    /**
     * Verifica si existe una relación específica entre torneo y categoría
     */
    boolean existsByTorneoIdAndCategoriaIdCategoria(Long torneoId, Long categoriaId);
    
    /**
     * Busca una relación específica entre torneo y categoría
     */
    Optional<TorneoCategoria> findByTorneoIdAndCategoriaIdCategoria(Long torneoId, Long categoriaId);
    
    /**
     * Elimina todas las categorías de un torneo
     */
    void deleteByTorneoId(Long torneoId);
    
    /**
     * Elimina todos los torneos de una categoría
     */
    void deleteByCategoriaIdCategoria(Long categoriaId);
    
    /**
     * Cuenta las categorías de un torneo
     */
    long countByTorneoId(Long torneoId);
    
    /**
     * Cuenta los torneos de una categoría
     */
    long countByCategoriaIdCategoria(Long categoriaId);
}