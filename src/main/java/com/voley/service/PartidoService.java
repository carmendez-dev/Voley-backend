package com.voley.service;

import com.voley.adapter.PartidoJpaRepository;
import com.voley.adapter.InscripcionEquipoJpaRepository;
import com.voley.adapter.EquipoVisitanteJpaRepository;
import com.voley.application.partido.CrearPartidoUseCase;
import com.voley.application.partido.ObtenerTodosPartidosUseCase;
import com.voley.domain.Partido;
import com.voley.domain.Partido.ResultadoPartido;
import com.voley.dto.PartidoDTO;
import com.voley.dto.PartidoUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar Partidos
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Service
public class PartidoService {
    
    private final CrearPartidoUseCase crearUseCase;
    private final ObtenerTodosPartidosUseCase obtenerTodosUseCase;
    private final PartidoJpaRepository partidoRepository;
    private final InscripcionEquipoJpaRepository inscripcionRepository;
    private final EquipoVisitanteJpaRepository equipoVisitanteRepository;
    
    @Autowired
    public PartidoService(
            CrearPartidoUseCase crearUseCase,
            ObtenerTodosPartidosUseCase obtenerTodosUseCase,
            PartidoJpaRepository partidoRepository,
            InscripcionEquipoJpaRepository inscripcionRepository,
            EquipoVisitanteJpaRepository equipoVisitanteRepository) {
        this.crearUseCase = crearUseCase;
        this.obtenerTodosUseCase = obtenerTodosUseCase;
        this.partidoRepository = partidoRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.equipoVisitanteRepository = equipoVisitanteRepository;
    }
    
    public PartidoDTO crear(PartidoDTO dto) {
        return crearUseCase.ejecutar(dto);
    }
    
    public List<PartidoDTO> obtenerTodos() {
        return obtenerTodosUseCase.ejecutar();
    }
    
    @Transactional(readOnly = true)
    public Optional<PartidoDTO> obtenerPorId(Long id) {
        return partidoRepository.findById(id)
            .map(this::convertirADTO);
    }
    
    @Transactional(readOnly = true)
    public List<PartidoDTO> obtenerPorResultado(ResultadoPartido resultado) {
        return partidoRepository.findByResultado(resultado).stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<PartidoDTO> obtenerPartidosPendientes() {
        return partidoRepository.findPartidosPendientes().stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public PartidoDTO actualizar(Long id, PartidoUpdateDTO dto) {
        Partido partido = partidoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No existe partido con ID: " + id));
        
        // Actualizar campos solo si vienen en el DTO
        if (dto.getIdInscripcionLocal() != null) {
            var inscripcion = inscripcionRepository.findById(dto.getIdInscripcionLocal())
                .orElseThrow(() -> new IllegalArgumentException(
                    "No existe inscripción con ID: " + dto.getIdInscripcionLocal()));
            partido.setInscripcionLocal(inscripcion);
        }
        
        if (dto.getIdEquipoVisitante() != null) {
            var equipoVisitante = equipoVisitanteRepository.findById(dto.getIdEquipoVisitante())
                .orElseThrow(() -> new IllegalArgumentException(
                    "No existe equipo visitante con ID: " + dto.getIdEquipoVisitante()));
            partido.setEquipoVisitante(equipoVisitante);
        }
        
        if (dto.getFecha() != null) {
            partido.setFecha(dto.getFecha());
        }
        
        if (dto.getUbicacion() != null) {
            partido.setUbicacion(dto.getUbicacion());
        }
        
        if (dto.getResultado() != null) {
            partido.setResultado(dto.getResultado());
        }
        
        partido.validar();
        Partido actualizado = partidoRepository.save(partido);
        
        return convertirADTO(actualizado);
    }
    
    @Transactional
    public PartidoDTO cambiarResultado(Long id, ResultadoPartido resultado) {
        Partido partido = partidoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No existe partido con ID: " + id));
        
        partido.setResultado(resultado);
        Partido actualizado = partidoRepository.save(partido);
        
        return convertirADTO(actualizado);
    }
    
    @Transactional
    public void eliminar(Long id) {
        Partido partido = partidoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No existe partido con ID: " + id));
        
        partidoRepository.delete(partido);
    }
    
    private PartidoDTO convertirADTO(Partido partido) {
        PartidoDTO dto = new PartidoDTO();
        dto.setIdPartido(partido.getIdPartido());
        dto.setIdInscripcionLocal(partido.getInscripcionLocal().getIdInscripcion());
        dto.setIdEquipoVisitante(partido.getEquipoVisitante().getIdEquipoVisitante());
        dto.setFecha(partido.getFecha());
        dto.setUbicacion(partido.getUbicacion());
        dto.setResultado(partido.getResultado());
        dto.setNombreEquipoLocal(partido.getInscripcionLocal().getEquipo().getNombre());
        dto.setNombreEquipoVisitante(partido.getEquipoVisitante().getNombre());
        
        if (partido.getInscripcionLocal().getTorneoCategoria() != null) {
            dto.setNombreTorneo(partido.getInscripcionLocal().getTorneoCategoria()
                .getTorneo().getNombre());
            dto.setNombreCategoria(partido.getInscripcionLocal().getTorneoCategoria()
                .getCategoria().getNombre());
        }
        
        return dto;
    }
}
