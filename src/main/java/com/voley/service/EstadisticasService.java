package com.voley.service;

import com.voley.domain.*;
import com.voley.dto.*;
import com.voley.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para generar estadísticas del sistema
 */
@Service
@Transactional(readOnly = true)
public class EstadisticasService {
    
    private static final Logger logger = LoggerFactory.getLogger(EstadisticasService.class);
    
    @Autowired
    private AccionJuegoRepository accionJuegoRepository;
    
    @Autowired
    private SetPartidoRepository setPartidoRepository;
    
    @Autowired
    private PartidoRepository partidoRepository;
    
    /**
     * Obtiene estadísticas completas de un partido
     */
    public EstadisticasPartidoDTO obtenerEstadisticasPartido(Long idPartido) {
        logger.info("Obteniendo estadísticas del partido: {}", idPartido);
        
        Partido partido = partidoRepository.findById(idPartido)
                .orElseThrow(() -> new RuntimeException("Partido no encontrado"));
        
        EstadisticasPartidoDTO stats = new EstadisticasPartidoDTO();
        stats.setIdPartido(idPartido);
        stats.setEquipoLocal(partido.getInscripcionLocal().getEquipo().getNombre());
        stats.setEquipoVisitante(partido.getEquipoVisitante().getNombre());
        stats.setResultado(partido.getResultado().name());
        
        // Obtener todos los sets del partido
        List<SetPartido> sets = setPartidoRepository.findAll().stream()
                .filter(s -> s.getPartido().getIdPartido().equals(idPartido))
                .collect(Collectors.toList());
        
        // Contar sets ganados
        int setsLocal = 0;
        int setsVisitante = 0;
        for (SetPartido set : sets) {
            if (set.getPuntosLocal() > set.getPuntosVisitante()) {
                setsLocal++;
            } else if (set.getPuntosVisitante() > set.getPuntosLocal()) {
                setsVisitante++;
            }
        }
        stats.setSetsGanadosLocal(setsLocal);
        stats.setSetsGanadosVisitante(setsVisitante);
        
        // Obtener todas las acciones del partido
        List<AccionJuego> acciones = new ArrayList<>();
        for (SetPartido set : sets) {
            acciones.addAll(accionJuegoRepository.findBySetPartidoId(set.getIdSetPartido()));
        }
        
        // Estadísticas del equipo local (idRoster > 0)
        List<AccionJuego> accionesLocal = acciones.stream()
                .filter(a -> a.getRosterJugador() != null)
                .collect(Collectors.toList());
        
        stats.setPuntosLocal((int) accionesLocal.stream()
                .filter(a -> a.getResultadoAccion().getIdResultadoAccion() == 1)
                .count());
        
        stats.setErroresLocal((int) accionesLocal.stream()
                .filter(a -> a.getResultadoAccion().getIdResultadoAccion() == 2)
                .count());
        
        stats.setPuntosPorTipoLocal(obtenerEstadisticasPorTipo(accionesLocal, 1));
        stats.setErroresPorTipoLocal(obtenerEstadisticasPorTipo(accionesLocal, 2));
        
        // Estadísticas del equipo visitante (idRoster = 0 o null)
        List<AccionJuego> accionesVisitante = acciones.stream()
                .filter(a -> a.getRosterJugador() == null)
                .collect(Collectors.toList());
        
        stats.setPuntosVisitante((int) accionesVisitante.stream()
                .filter(a -> a.getResultadoAccion().getIdResultadoAccion() == 1)
                .count());
        
        stats.setErroresVisitante((int) accionesVisitante.stream()
                .filter(a -> a.getResultadoAccion().getIdResultadoAccion() == 2)
                .count());
        
        stats.setPuntosPorTipoVisitante(obtenerEstadisticasPorTipo(accionesVisitante, 1));
        stats.setErroresPorTipoVisitante(obtenerEstadisticasPorTipo(accionesVisitante, 2));
        
        return stats;
    }
    
    /**
     * Obtiene estadísticas de un jugador en un partido
     */
    public EstadisticasJugadorDTO obtenerEstadisticasJugador(Long idPartido, Long idRoster) {
        logger.info("Obteniendo estadísticas del jugador {} en partido {}", idRoster, idPartido);
        
        Partido partido = partidoRepository.findById(idPartido)
                .orElseThrow(() -> new RuntimeException("Partido no encontrado"));
        
        // Obtener todos los sets del partido
        List<SetPartido> sets = setPartidoRepository.findAll().stream()
                .filter(s -> s.getPartido().getIdPartido().equals(idPartido))
                .collect(Collectors.toList());
        
        // Obtener acciones del jugador en este partido
        List<AccionJuego> acciones = new ArrayList<>();
        for (SetPartido set : sets) {
            List<AccionJuego> accionesSet = accionJuegoRepository.findBySetPartidoId(set.getIdSetPartido());
            acciones.addAll(accionesSet.stream()
                    .filter(a -> a.getRosterJugador() != null && 
                                 a.getRosterJugador().getIdRoster().equals(idRoster))
                    .collect(Collectors.toList()));
        }
        
        if (acciones.isEmpty()) {
            throw new RuntimeException("No se encontraron acciones para el jugador en este partido");
        }
        
        EstadisticasJugadorDTO stats = new EstadisticasJugadorDTO();
        stats.setIdRoster(idRoster);
        stats.setNombreJugador(acciones.get(0).getRosterJugador().getUsuario().getNombreCompleto());
        stats.setIdPartido(idPartido);
        stats.setEquipoLocal(partido.getInscripcionLocal().getEquipo().getNombre());
        stats.setEquipoVisitante(partido.getEquipoVisitante().getNombre());
        
        stats.setTotalPuntos((int) acciones.stream()
                .filter(a -> a.getResultadoAccion().getIdResultadoAccion() == 1)
                .count());
        
        stats.setTotalErrores((int) acciones.stream()
                .filter(a -> a.getResultadoAccion().getIdResultadoAccion() == 2)
                .count());
        
        stats.setPuntosPorTipo(obtenerEstadisticasPorTipo(acciones, 1));
        stats.setErroresPorTipo(obtenerEstadisticasPorTipo(acciones, 2));
        
        return stats;
    }
    
    /**
     * Obtiene estadísticas generales para el dashboard
     */
    public EstadisticasGeneralesDTO obtenerEstadisticasGenerales() {
        logger.info("Obteniendo estadísticas generales");
        
        List<Partido> partidos = partidoRepository.findAll();
        
        EstadisticasGeneralesDTO stats = new EstadisticasGeneralesDTO();
        stats.setTotalPartidos(partidos.size());
        
        stats.setPartidosGanados((int) partidos.stream()
                .filter(p -> p.getResultado() == Partido.ResultadoPartido.Ganado)
                .count());
        
        stats.setPartidosPerdidos((int) partidos.stream()
                .filter(p -> p.getResultado() == Partido.ResultadoPartido.Perdido)
                .count());
        
        stats.setPartidosWalkover((int) partidos.stream()
                .filter(p -> p.getResultado() == Partido.ResultadoPartido.Walkover)
                .count());
        
        stats.setPartidosWalkoverContra((int) partidos.stream()
                .filter(p -> p.getResultado() == Partido.ResultadoPartido.WalkoverContra)
                .count());
        
        stats.setPartidosPendientes((int) partidos.stream()
                .filter(p -> p.getResultado() == Partido.ResultadoPartido.Pendiente)
                .count());
        
        // Estadísticas de sets
        List<SetPartido> sets = setPartidoRepository.findAll();
        stats.setTotalSetsJugados(sets.size());
        
        stats.setSetsGanados((int) sets.stream()
                .filter(s -> s.getPuntosLocal() > s.getPuntosVisitante())
                .count());
        
        stats.setSetsPerdidos((int) sets.stream()
                .filter(s -> s.getPuntosVisitante() > s.getPuntosLocal())
                .count());
        
        // Estadísticas de acciones
        List<AccionJuego> acciones = accionJuegoRepository.findAll();
        
        stats.setTotalPuntos((int) acciones.stream()
                .filter(a -> a.getResultadoAccion().getIdResultadoAccion() == 1)
                .count());
        
        stats.setTotalErrores((int) acciones.stream()
                .filter(a -> a.getResultadoAccion().getIdResultadoAccion() == 2)
                .count());
        
        return stats;
    }
    
    /**
     * Método auxiliar para agrupar estadísticas por tipo de acción
     */
    private List<EstadisticaPorTipoAccionDTO> obtenerEstadisticasPorTipo(
            List<AccionJuego> acciones, Integer idResultado) {
        
        Map<String, Long> agrupado = acciones.stream()
                .filter(a -> a.getResultadoAccion().getIdResultadoAccion().equals(idResultado))
                .collect(Collectors.groupingBy(
                        a -> a.getTipoAccion().getDescripcion(),
                        Collectors.counting()
                ));
        
        return agrupado.entrySet().stream()
                .map(e -> new EstadisticaPorTipoAccionDTO(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(EstadisticaPorTipoAccionDTO::getCantidad).reversed())
                .collect(Collectors.toList());
    }
}
