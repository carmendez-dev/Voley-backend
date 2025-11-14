package com.voley.service;

import com.voley.domain.*;
import com.voley.dto.AccionJuegoDTO;
import com.voley.repository.*;
import com.voley.adapter.RosterJugadorJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de acciones de juego
 */
@Service
@Transactional
public class AccionJuegoService {
    
    private static final Logger logger = LoggerFactory.getLogger(AccionJuegoService.class);
    
    @Autowired
    private AccionJuegoRepository accionJuegoRepository;
    
    @Autowired
    private SetPartidoRepository setPartidoRepository;
    
    @Autowired
    private TipoAccionRepository tipoAccionRepository;
    
    @Autowired
    private ResultadoAccionRepository resultadoAccionRepository;
    
    @Autowired
    private RosterJugadorJpaRepository rosterJugadorRepository;
    
    /**
     * Obtiene todas las acciones de juego
     */
    public List<AccionJuegoDTO> obtenerTodas() {
        logger.info("Obteniendo todas las acciones de juego");
        return accionJuegoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene una acción por ID
     */
    public AccionJuegoDTO obtenerPorId(Long id) {
        logger.info("Obteniendo acción de juego con ID: {}", id);
        return accionJuegoRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("Acción de juego no encontrada con ID: " + id));
    }
    
    /**
     * Obtiene todas las acciones de un set
     */
    public List<AccionJuegoDTO> obtenerPorSetPartido(Long idSetPartido) {
        logger.info("Obteniendo acciones del set: {}", idSetPartido);
        return accionJuegoRepository.findBySetPartidoId(idSetPartido).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Crea una nueva acción de juego
     */
    public AccionJuegoDTO crear(AccionJuegoDTO dto) {
        logger.info("Creando nueva acción de juego para set: {}", dto.getIdSetPartido());
        
        AccionJuego accion = new AccionJuego();
        actualizarEntidadDesdeDTO(accion, dto);
        
        AccionJuego guardada = accionJuegoRepository.save(accion);
        logger.info("Acción de juego creada con ID: {}", guardada.getIdAccionJuego());
        
        return convertirADTO(guardada);
    }
    
    /**
     * Actualiza una acción de juego existente
     */
    public AccionJuegoDTO actualizar(Long id, AccionJuegoDTO dto) {
        logger.info("Actualizando acción de juego con ID: {}", id);
        
        AccionJuego accion = accionJuegoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Acción de juego no encontrada con ID: " + id));
        
        actualizarEntidadDesdeDTO(accion, dto);
        
        AccionJuego actualizada = accionJuegoRepository.save(accion);
        logger.info("Acción de juego actualizada: {}", id);
        
        return convertirADTO(actualizada);
    }
    
    /**
     * Elimina una acción de juego
     */
    public void eliminar(Long id) {
        logger.info("Eliminando acción de juego con ID: {}", id);
        
        if (!accionJuegoRepository.existsById(id)) {
            throw new RuntimeException("Acción de juego no encontrada con ID: " + id);
        }
        
        accionJuegoRepository.deleteById(id);
        logger.info("Acción de juego eliminada: {}", id);
    }
    
    /**
     * Convierte una entidad a DTO
     */
    private AccionJuegoDTO convertirADTO(AccionJuego accion) {
        AccionJuegoDTO dto = new AccionJuegoDTO();
        dto.setIdAccionJuego(accion.getIdAccionJuego());
        dto.setIdSetPartido(accion.getSetPartido().getIdSetPartido());
        dto.setIdTipoAccion(accion.getTipoAccion().getIdTipoAccion());
        dto.setTipoAccionDescripcion(accion.getTipoAccion().getDescripcion());
        dto.setIdResultadoAccion(accion.getResultadoAccion().getIdResultadoAccion());
        dto.setResultadoAccionDescripcion(accion.getResultadoAccion().getDescripcion());
        
        // Si el roster es null, es una acción del equipo visitante (idRoster = 0)
        if (accion.getRosterJugador() != null) {
            dto.setIdRoster(accion.getRosterJugador().getIdRoster());
            dto.setNombreJugador(accion.getRosterJugador().getUsuario().getNombreCompleto());
        } else {
            dto.setIdRoster(0L);
            dto.setNombreJugador(null);
        }
        
        dto.setPosicionVisitante(accion.getPosicionVisitante());
        return dto;
    }
    
    /**
     * Actualiza una entidad desde un DTO
     */
    private void actualizarEntidadDesdeDTO(AccionJuego accion, AccionJuegoDTO dto) {
        SetPartido setPartido = setPartidoRepository.findById(dto.getIdSetPartido())
                .orElseThrow(() -> new RuntimeException("Set de partido no encontrado"));
        accion.setSetPartido(setPartido);
        
        TipoAccion tipoAccion = tipoAccionRepository.findById(dto.getIdTipoAccion())
                .orElseThrow(() -> new RuntimeException("Tipo de acción no encontrado"));
        accion.setTipoAccion(tipoAccion);
        
        ResultadoAccion resultadoAccion = resultadoAccionRepository.findById(dto.getIdResultadoAccion())
                .orElseThrow(() -> new RuntimeException("Resultado de acción no encontrado"));
        accion.setResultadoAccion(resultadoAccion);
        
        // Si idRoster es 0 o null, es una acción del equipo visitante (no tiene roster)
        if (dto.getIdRoster() != null && dto.getIdRoster() > 0L) {
            RosterJugador rosterJugador = rosterJugadorRepository.findById(dto.getIdRoster())
                    .orElseThrow(() -> new RuntimeException("Roster de jugador no encontrado con ID: " + dto.getIdRoster()));
            accion.setRosterJugador(rosterJugador);
        } else {
            // Acción del equipo visitante, no tiene roster asociado
            accion.setRosterJugador(null);
        }
        
        accion.setPosicionVisitante(dto.getPosicionVisitante() != null ? dto.getPosicionVisitante() : 0);
    }
}
