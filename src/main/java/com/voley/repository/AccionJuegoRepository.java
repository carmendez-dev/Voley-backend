package com.voley.repository;

import com.voley.domain.AccionJuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad AccionJuego
 */
@Repository
public interface AccionJuegoRepository extends JpaRepository<AccionJuego, Long> {
    
    /**
     * Busca todas las acciones de un set específico
     */
    @Query("SELECT a FROM AccionJuego a WHERE a.setPartido.idSetPartido = :idSetPartido ORDER BY a.idAccionJuego")
    List<AccionJuego> findBySetPartidoId(@Param("idSetPartido") Long idSetPartido);
    
    /**
     * Busca todas las acciones de un jugador específico
     */
    @Query("SELECT a FROM AccionJuego a WHERE a.rosterJugador.idRoster = :idRoster ORDER BY a.idAccionJuego")
    List<AccionJuego> findByRosterJugadorId(@Param("idRoster") Long idRoster);
}
