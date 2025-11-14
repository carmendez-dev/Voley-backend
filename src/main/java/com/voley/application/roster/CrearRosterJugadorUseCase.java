package com.voley.application.roster;

import com.voley.adapter.InscripcionEquipoJpaRepository;
import com.voley.adapter.RosterJugadorJpaRepository;
import com.voley.adapter.UsuarioJpaRepository;
import com.voley.domain.InscripcionEquipo;
import com.voley.domain.RosterJugador;
import com.voley.domain.Usuario;
import com.voley.dto.RosterJugadorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso: Crear RosterJugador
 */
@Service
@Transactional
public class CrearRosterJugadorUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(CrearRosterJugadorUseCase.class);
    
    private final RosterJugadorJpaRepository rosterRepository;
    private final InscripcionEquipoJpaRepository inscripcionRepository;
    private final UsuarioJpaRepository usuarioRepository;
    
    @Autowired
    public CrearRosterJugadorUseCase(
            RosterJugadorJpaRepository rosterRepository,
            InscripcionEquipoJpaRepository inscripcionRepository,
            UsuarioJpaRepository usuarioRepository) {
        this.rosterRepository = rosterRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.usuarioRepository = usuarioRepository;
    }
    
    public RosterJugadorDTO ejecutar(RosterJugadorDTO dto) {
        logger.info("Creando roster: inscripción={}, usuario={}", 
                   dto.getIdInscripcion(), dto.getIdUsuario());
        
        // Validar que la inscripción existe
        InscripcionEquipo inscripcion = inscripcionRepository
            .findById(dto.getIdInscripcion())
            .orElseThrow(() -> new IllegalArgumentException(
                "No existe inscripción con ID: " + dto.getIdInscripcion()));
        
        // Validar que el usuario existe
        Usuario usuario = usuarioRepository
            .findById(dto.getIdUsuario())
            .orElseThrow(() -> new IllegalArgumentException(
                "No existe usuario con ID: " + dto.getIdUsuario()));
        
        // Validar que no exista duplicado
        if (rosterRepository.existsByInscripcionIdInscripcionAndUsuarioId(
                dto.getIdInscripcion(), dto.getIdUsuario())) {
            throw new IllegalArgumentException(
                "El jugador ya está registrado en esta inscripción");
        }
        
        // Crear roster
        RosterJugador roster = new RosterJugador(inscripcion, usuario);
        RosterJugador guardado = rosterRepository.save(roster);
        
        logger.info("Roster creado exitosamente con ID: {}", guardado.getIdRoster());
        
        return convertirADTO(guardado);
    }
    
    private RosterJugadorDTO convertirADTO(RosterJugador roster) {
        RosterJugadorDTO dto = new RosterJugadorDTO();
        dto.setIdRoster(roster.getIdRoster());
        dto.setIdInscripcion(roster.getInscripcion().getIdInscripcion());
        dto.setIdUsuario(roster.getUsuario().getId());
        dto.setFechaRegistro(roster.getFechaRegistro());
        
        // Información adicional
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
