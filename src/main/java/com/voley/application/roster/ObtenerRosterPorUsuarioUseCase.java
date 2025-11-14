package com.voley.application.roster;

import com.voley.adapter.RosterJugadorJpaRepository;
import com.voley.domain.RosterJugador;
import com.voley.dto.RosterJugadorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Caso de uso: Obtener roster por usuario
 */
@Service
@Transactional(readOnly = true)
public class ObtenerRosterPorUsuarioUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(ObtenerRosterPorUsuarioUseCase.class);
    
    private final RosterJugadorJpaRepository rosterRepository;
    
    @Autowired
    public ObtenerRosterPorUsuarioUseCase(RosterJugadorJpaRepository rosterRepository) {
        this.rosterRepository = rosterRepository;
    }
    
    public List<RosterJugadorDTO> ejecutar(Long idUsuario) {
        logger.debug("Obteniendo inscripciones del usuario: {}", idUsuario);
        
        List<RosterJugador> rosters = rosterRepository.findByUsuarioId(idUsuario);
        
        return rosters.stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }
    
    private RosterJugadorDTO convertirADTO(RosterJugador roster) {
        RosterJugadorDTO dto = new RosterJugadorDTO();
        dto.setIdRoster(roster.getIdRoster());
        dto.setIdInscripcion(roster.getInscripcion().getIdInscripcion());
        dto.setIdUsuario(roster.getUsuario().getId());
        dto.setFechaRegistro(roster.getFechaRegistro());
        dto.setNombreJugador(roster.getUsuario().getPrimerNombre() + " " + 
                            roster.getUsuario().getPrimerApellido());
        dto.setEmailJugador(roster.getUsuario().getEmail());
        dto.setNombreEquipo(roster.getInscripcion().getEquipo().getNombre());
        
        if (roster.getInscripcion().getTorneoCategoria() != null) {
            dto.setNombreTorneo(roster.getInscripcion().getTorneoCategoria()
                .getTorneo().getNombre());
            dto.setNombreCategoria(roster.getInscripcion().getTorneoCategoria()
                .getCategoria().getNombre());
        }
        
        return dto;
    }
}
