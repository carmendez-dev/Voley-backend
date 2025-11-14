package com.voley.application.roster;

import com.voley.adapter.RosterJugadorJpaRepository;
import com.voley.domain.RosterJugador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso: Eliminar RosterJugador
 */
@Service
@Transactional
public class EliminarRosterJugadorUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(EliminarRosterJugadorUseCase.class);
    
    private final RosterJugadorJpaRepository rosterRepository;
    
    @Autowired
    public EliminarRosterJugadorUseCase(RosterJugadorJpaRepository rosterRepository) {
        this.rosterRepository = rosterRepository;
    }
    
    public void ejecutar(Long idRoster) {
        logger.info("Eliminando roster ID: {}", idRoster);
        
        RosterJugador roster = rosterRepository.findById(idRoster)
            .orElseThrow(() -> new IllegalArgumentException(
                "No existe roster con ID: " + idRoster));
        
        rosterRepository.delete(roster);
        
        logger.info("Roster eliminado exitosamente");
    }
}
