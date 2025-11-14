package com.voley.adapter;

import com.voley.domain.InscripcionEquipo;
import com.voley.domain.InscripcionEquipo.EstadoInscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para InscripcionEquipo
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Repository
public interface InscripcionEquipoJpaRepository extends JpaRepository<InscripcionEquipo, Long> {
    
    /**
     * Busca inscripciones por torneo-categoría
     */
    @Query("SELECT i FROM InscripcionEquipo i WHERE i.torneoCategoria.id = :idTorneoCategoria")
    List<InscripcionEquipo> findByTorneoCategoriaId(@Param("idTorneoCategoria") Long idTorneoCategoria);
    
    /**
     * Busca inscripciones por equipo
     */
    @Query("SELECT i FROM InscripcionEquipo i WHERE i.equipo.idEquipo = :idEquipo")
    List<InscripcionEquipo> findByEquipoId(@Param("idEquipo") Long idEquipo);
    
    /**
     * Busca inscripciones por estado
     */
    List<InscripcionEquipo> findByEstado(EstadoInscripcion estado);
    
    /**
     * Busca inscripciones por torneo y categoría
     */
    @Query("SELECT i FROM InscripcionEquipo i " +
           "WHERE i.torneoCategoria.torneo.id = :idTorneo " +
           "AND i.torneoCategoria.categoria.idCategoria = :idCategoria")
    List<InscripcionEquipo> findByTorneoAndCategoria(@Param("idTorneo") Long idTorneo, 
                                                      @Param("idCategoria") Long idCategoria);
    
    /**
     * Verifica si existe una inscripción
     */
    @Query("SELECT COUNT(i) > 0 FROM InscripcionEquipo i " +
           "WHERE i.torneoCategoria.id = :idTorneoCategoria " +
           "AND i.equipo.idEquipo = :idEquipo")
    boolean existsByTorneoCategoriaAndEquipo(@Param("idTorneoCategoria") Long idTorneoCategoria,
                                             @Param("idEquipo") Long idEquipo);
    
    /**
     * Busca una inscripción específica
     */
    @Query("SELECT i FROM InscripcionEquipo i " +
           "WHERE i.torneoCategoria.id = :idTorneoCategoria " +
           "AND i.equipo.idEquipo = :idEquipo")
    Optional<InscripcionEquipo> findByTorneoCategoriaAndEquipo(@Param("idTorneoCategoria") Long idTorneoCategoria,
                                                                @Param("idEquipo") Long idEquipo);
    
    /**
     * Cuenta inscripciones por torneo-categoría
     */
    @Query("SELECT COUNT(i) FROM InscripcionEquipo i WHERE i.torneoCategoria.id = :idTorneoCategoria")
    long countByTorneoCategoria(@Param("idTorneoCategoria") Long idTorneoCategoria);
    
    /**
     * Cuenta inscripciones por estado
     */
    long countByEstado(EstadoInscripcion estado);
}
