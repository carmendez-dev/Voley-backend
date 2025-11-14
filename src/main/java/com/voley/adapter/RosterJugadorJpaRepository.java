package com.voley.adapter;

import com.voley.domain.RosterJugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para RosterJugador
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Repository
public interface RosterJugadorJpaRepository extends JpaRepository<RosterJugador, Long> {
    
    /**
     * Busca todos los jugadores de una inscripción
     */
    @Query("SELECT r FROM RosterJugador r " +
           "JOIN FETCH r.usuario " +
           "WHERE r.inscripcion.idInscripcion = :idInscripcion")
    List<RosterJugador> findByInscripcionId(@Param("idInscripcion") Long idInscripcion);
    
    /**
     * Busca todas las inscripciones de un jugador
     */
    @Query("SELECT r FROM RosterJugador r " +
           "JOIN FETCH r.inscripcion " +
           "WHERE r.usuario.id = :idUsuario")
    List<RosterJugador> findByUsuarioId(@Param("idUsuario") Long idUsuario);
    
    /**
     * Verifica si un jugador ya está en una inscripción
     */
    boolean existsByInscripcionIdInscripcionAndUsuarioId(Long idInscripcion, Long idUsuario);
    
    /**
     * Busca un roster específico por inscripción y usuario
     */
    Optional<RosterJugador> findByInscripcionIdInscripcionAndUsuarioId(Long idInscripcion, Long idUsuario);
    
    /**
     * Cuenta jugadores en una inscripción
     */
    long countByInscripcionIdInscripcion(Long idInscripcion);
    
    /**
     * Elimina todos los jugadores de una inscripción
     */
    void deleteByInscripcionIdInscripcion(Long idInscripcion);
    
    /**
     * Busca jugadores por equipo
     */
    @Query("SELECT r FROM RosterJugador r " +
           "JOIN FETCH r.usuario " +
           "WHERE r.inscripcion.equipo.idEquipo = :idEquipo")
    List<RosterJugador> findByEquipoId(@Param("idEquipo") Long idEquipo);
}
