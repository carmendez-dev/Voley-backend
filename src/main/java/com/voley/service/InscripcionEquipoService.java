package com.voley.service;

import com.voley.application.inscripciones.*;
import com.voley.domain.InscripcionEquipo;
import com.voley.domain.InscripcionEquipo.EstadoInscripcion;
import com.voley.dto.InscripcionEquipoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de inscripciones de equipos
 * Orquesta los casos de uso
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Service
@Transactional
public class InscripcionEquipoService {
    
    private static final Logger logger = LoggerFactory.getLogger(InscripcionEquipoService.class);
    
    private final CrearInscripcionUseCase crearInscripcionUseCase;
    private final ObtenerInscripcionPorIdUseCase obtenerInscripcionPorIdUseCase;
    private final ObtenerTodasInscripcionesUseCase obtenerTodasInscripcionesUseCase;
    private final ObtenerInscripcionesPorTorneoYCategoriaUseCase obtenerPorTorneoYCategoriaUseCase;
    private final ObtenerInscripcionesPorEquipoUseCase obtenerPorEquipoUseCase;
    private final ObtenerInscripcionesPorEstadoUseCase obtenerPorEstadoUseCase;
    private final ActualizarInscripcionUseCase actualizarInscripcionUseCase;
    private final CambiarEstadoInscripcionUseCase cambiarEstadoUseCase;
    private final EliminarInscripcionUseCase eliminarInscripcionUseCase;
    
    @Autowired
    public InscripcionEquipoService(
            CrearInscripcionUseCase crearInscripcionUseCase,
            ObtenerInscripcionPorIdUseCase obtenerInscripcionPorIdUseCase,
            ObtenerTodasInscripcionesUseCase obtenerTodasInscripcionesUseCase,
            ObtenerInscripcionesPorTorneoYCategoriaUseCase obtenerPorTorneoYCategoriaUseCase,
            ObtenerInscripcionesPorEquipoUseCase obtenerPorEquipoUseCase,
            ObtenerInscripcionesPorEstadoUseCase obtenerPorEstadoUseCase,
            ActualizarInscripcionUseCase actualizarInscripcionUseCase,
            CambiarEstadoInscripcionUseCase cambiarEstadoUseCase,
            EliminarInscripcionUseCase eliminarInscripcionUseCase) {
        this.crearInscripcionUseCase = crearInscripcionUseCase;
        this.obtenerInscripcionPorIdUseCase = obtenerInscripcionPorIdUseCase;
        this.obtenerTodasInscripcionesUseCase = obtenerTodasInscripcionesUseCase;
        this.obtenerPorTorneoYCategoriaUseCase = obtenerPorTorneoYCategoriaUseCase;
        this.obtenerPorEquipoUseCase = obtenerPorEquipoUseCase;
        this.obtenerPorEstadoUseCase = obtenerPorEstadoUseCase;
        this.actualizarInscripcionUseCase = actualizarInscripcionUseCase;
        this.cambiarEstadoUseCase = cambiarEstadoUseCase;
        this.eliminarInscripcionUseCase = eliminarInscripcionUseCase;
    }
    
    /**
     * Crea una nueva inscripción
     */
    public InscripcionEquipoDTO crearInscripcion(InscripcionEquipoDTO dto) {
        InscripcionEquipo inscripcion = crearInscripcionUseCase.ejecutar(
            dto.getIdTorneoCategoria(), 
            dto.getIdEquipo(), 
            dto.getObservaciones()
        );
        return convertirADTO(inscripcion);
    }
    
    /**
     * Obtiene una inscripción por ID
     */
    @Transactional(readOnly = true)
    public Optional<InscripcionEquipoDTO> obtenerPorId(Long id) {
        return obtenerInscripcionPorIdUseCase.ejecutar(id)
            .map(this::convertirADTO);
    }
    
    /**
     * Obtiene todas las inscripciones
     */
    @Transactional(readOnly = true)
    public List<InscripcionEquipoDTO> obtenerTodas() {
        return obtenerTodasInscripcionesUseCase.ejecutar().stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtiene inscripciones por torneo y categoría
     */
    @Transactional(readOnly = true)
    public List<InscripcionEquipoDTO> obtenerPorTorneoYCategoria(Long idTorneo, Long idCategoria) {
        return obtenerPorTorneoYCategoriaUseCase.ejecutar(idTorneo, idCategoria).stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtiene inscripciones por equipo
     */
    @Transactional(readOnly = true)
    public List<InscripcionEquipoDTO> obtenerPorEquipo(Long idEquipo) {
        return obtenerPorEquipoUseCase.ejecutar(idEquipo).stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtiene inscripciones por estado
     */
    @Transactional(readOnly = true)
    public List<InscripcionEquipoDTO> obtenerPorEstado(EstadoInscripcion estado) {
        return obtenerPorEstadoUseCase.ejecutar(estado).stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Actualiza una inscripción
     */
    public InscripcionEquipoDTO actualizarInscripcion(Long id, InscripcionEquipoDTO dto) {
        InscripcionEquipo actualizada = actualizarInscripcionUseCase.ejecutar(
            id, 
            dto.getEstado(), 
            dto.getObservaciones()
        );
        return convertirADTO(actualizada);
    }
    
    /**
     * Cambia el estado de una inscripción
     */
    public InscripcionEquipoDTO cambiarEstado(Long id, EstadoInscripcion nuevoEstado, String observaciones) {
        InscripcionEquipo actualizada = cambiarEstadoUseCase.ejecutar(id, nuevoEstado, observaciones);
        return convertirADTO(actualizada);
    }
    
    /**
     * Elimina una inscripción
     */
    public void eliminarInscripcion(Long id) {
        eliminarInscripcionUseCase.ejecutar(id);
    }
    
    /**
     * Convierte entidad a DTO
     */
    private InscripcionEquipoDTO convertirADTO(InscripcionEquipo inscripcion) {
        InscripcionEquipoDTO dto = new InscripcionEquipoDTO();
        dto.setIdInscripcion(inscripcion.getIdInscripcion());
        dto.setIdTorneoCategoria(inscripcion.getTorneoCategoria().getId());
        dto.setIdEquipo(inscripcion.getEquipo().getIdEquipo());
        dto.setEstado(inscripcion.getEstado());
        dto.setObservaciones(inscripcion.getObservaciones());
        dto.setFechaInscripcion(inscripcion.getFechaInscripcion());
        
        // Información adicional
        dto.setNombreTorneo(inscripcion.getTorneoCategoria().getTorneo().getNombre());
        dto.setNombreCategoria(inscripcion.getTorneoCategoria().getCategoria().getNombre());
        dto.setNombreEquipo(inscripcion.getEquipo().getNombre());
        
        return dto;
    }
}
