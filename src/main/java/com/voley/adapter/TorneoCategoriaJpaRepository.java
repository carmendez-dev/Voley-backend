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
    @Query("SELECT tc FROM TorneoCategoria tc JOIN FETCH tc.categoria WHERE tc.torneo.id = :torneoId")
    List<TorneoCategoria> findByTorneoId(@Param("torneoId") Long torneoId);
    
    /**
     * Busca todos los torneos de una categoría específica
     */
    @Query("SELECT tc FROM TorneoCategoria tc JOIN FETCH tc.torneo WHERE tc.categoria.idCategoria = :categoriaId")
    List<TorneoCategoria> findByCategoriaId(@Param("categoriaId") Long categoriaId);
    
    /**
     * Verifica si existe una relación específica entre torneo y categoría
     */
    @Query("SELECT COUNT(tc) > 0 FROM TorneoCategoria tc WHERE tc.torneo.id = :torneoId AND tc.categoria.idCategoria = :categoriaId")
    boolean existsByTorneoIdAndCategoriaId(@Param("torneoId") Long torneoId, @Param("categoriaId") Long categoriaId);
    
    /**
     * Busca una relación específica entre torneo y categoría
     */
    @Query("SELECT tc FROM TorneoCategoria tc WHERE tc.torneo.id = :torneoId AND tc.categoria.idCategoria = :categoriaId")
    Optional<TorneoCategoria> findByTorneoIdAndCategoriaId(@Param("torneoId") Long torneoId, @Param("categoriaId") Long categoriaId);
    
    /**
     * Elimina todas las categorías de un torneo
     */
    @Query("DELETE FROM TorneoCategoria tc WHERE tc.torneo.id = :torneoId")
    void deleteByTorneoId(@Param("torneoId") Long torneoId);
    
    /**
     * Elimina todos los torneos de una categoría
     */
    @Query("DELETE FROM TorneoCategoria tc WHERE tc.categoria.idCategoria = :categoriaId")
    void deleteByCategoriaId(@Param("categoriaId") Long categoriaId);
    
    /**
     * Cuenta las categorías de un torneo
     */
    @Query("SELECT COUNT(tc) FROM TorneoCategoria tc WHERE tc.torneo.id = :torneoId")
    long countByTorneoId(@Param("torneoId") Long torneoId);
    
    /**
     * Cuenta los torneos de una categoría
     */
    @Query("SELECT COUNT(tc) FROM TorneoCategoria tc WHERE tc.categoria.idCategoria = :categoriaId")
    long countByCategoriaId(@Param("categoriaId") Long categoriaId);
}