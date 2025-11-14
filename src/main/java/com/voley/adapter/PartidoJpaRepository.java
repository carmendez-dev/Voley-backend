package com.voley.adapter;

import com.voley.domain.Partido;
import com.voley.domain.Partido.ResultadoPartido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio JPA para Partido
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Repository
public interface PartidoJpaRepository extends JpaRepository<Partido, Long> {
    
    /**
     * Busca partidos por inscripción local
     */
    List<Partido> findByInscripcionLocalIdInscripcion(Long idInscripcionLocal);
    
    /**
     * Busca partidos por equipo visitante
     */
    List<Partido> findByEquipoVisitanteIdEquipoVisitante(Long idEquipoVisitante);
    
    /**
     * Busca partidos por resultado
     */
    List<Partido> findByResultado(ResultadoPartido resultado);
    
    /**
     * Busca partidos por rango de fechas
     */
    @Query("SELECT p FROM Partido p WHERE p.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY p.fecha")
    List<Partido> findByFechaBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                      @Param("fechaFin") LocalDateTime fechaFin);
    
    /**
     * Busca partidos pendientes
     */
    @Query("SELECT p FROM Partido p WHERE p.resultado = 'Pendiente' ORDER BY p.fecha")
    List<Partido> findPartidosPendientes();
    
    /**
     * Busca partidos finalizados
     */
    @Query("SELECT p FROM Partido p WHERE p.resultado != 'Pendiente' ORDER BY p.fecha DESC")
    List<Partido> findPartidosFinalizados();
    
    /**
     * Busca partidos por ubicación
     */
    List<Partido> findByUbicacionContainingIgnoreCase(String ubicacion);
}
