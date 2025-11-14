package com.voley.service;

import com.voley.application.roster.CrearRosterJugadorUseCase;
import com.voley.application.roster.EliminarRosterJugadorUseCase;
import com.voley.application.roster.ObtenerRosterPorInscripcionUseCase;
import com.voley.application.roster.ObtenerRosterPorUsuarioUseCase;
import com.voley.dto.RosterJugadorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para gestionar RosterJugador
 * Orquesta los casos de uso
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Service
public class RosterJugadorService {
    
    private final CrearRosterJugadorUseCase crearUseCase;
    private final ObtenerRosterPorInscripcionUseCase obtenerPorInscripcionUseCase;
    private final ObtenerRosterPorUsuarioUseCase obtenerPorUsuarioUseCase;
    private final EliminarRosterJugadorUseCase eliminarUseCase;
    
    @Autowired
    public RosterJugadorService(
            CrearRosterJugadorUseCase crearUseCase,
            ObtenerRosterPorInscripcionUseCase obtenerPorInscripcionUseCase,
            ObtenerRosterPorUsuarioUseCase obtenerPorUsuarioUseCase,
            EliminarRosterJugadorUseCase eliminarUseCase) {
        this.crearUseCase = crearUseCase;
        this.obtenerPorInscripcionUseCase = obtenerPorInscripcionUseCase;
        this.obtenerPorUsuarioUseCase = obtenerPorUsuarioUseCase;
        this.eliminarUseCase = eliminarUseCase;
    }
    
    /**
     * Agregar jugador al roster
     */
    public RosterJugadorDTO agregarJugador(RosterJugadorDTO dto) {
        return crearUseCase.ejecutar(dto);
    }
    
    /**
     * Obtener jugadores de una inscripción
     */
    public List<RosterJugadorDTO> obtenerJugadoresPorInscripcion(Long idInscripcion) {
        return obtenerPorInscripcionUseCase.ejecutar(idInscripcion);
    }
    
    /**
     * Obtener inscripciones de un jugador
     */
    public List<RosterJugadorDTO> obtenerInscripcionesPorJugador(Long idUsuario) {
        return obtenerPorUsuarioUseCase.ejecutar(idUsuario);
    }
    
    /**
     * Eliminar jugador del roster
     */
    public void eliminarJugador(Long idRoster) {
        eliminarUseCase.ejecutar(idRoster);
    }
}
