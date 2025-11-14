package com.voley.repository;

import com.voley.domain.InscripcionEquipo;
import com.voley.domain.InscripcionEquipo.EstadoInscripcion;

import java.util.List;
import java.util.Optional;

/**
 * Puerto (Interface) para el repositorio de InscripcionEquipo
 * Define las operaciones de persistencia
 * 
 * @author Sistema Voley
 * @version 1.0
 */
public interface InscripcionEquipoRepository {
    
    /**
     * Guarda una inscripción
     */
    InscripcionEquipo guardar(InscripcionEquipo inscripcion);
    
    /**
     * Busca una inscripción por ID
     */
    Optional<InscripcionEquipo> buscarPorId(Long id);
    
    /**
     * Obtiene todas las inscripciones
     */
    List<InscripcionEquipo> buscarTodas();
    
    /**
     * Busca inscripciones por torneo-categoría
     */
    List<InscripcionEquipo> buscarPorTorneoCategoria(Long idTorneoCategoria);
    
    /**
     * Busca inscripciones por equipo
     */
    List<InscripcionEquipo> buscarPorEquipo(Long idEquipo);
    
    /**
     * Busca inscripciones por estado
     */
    List<InscripcionEquipo> buscarPorEstado(EstadoInscripcion estado);
    
    /**
     * Busca inscripciones por torneo y categoría específicos
     */
    List<InscripcionEquipo> buscarPorTorneoYCategoria(Long idTorneo, Long idCategoria);
    
    /**
     * Verifica si existe una inscripción para un equipo en una categoría de torneo
     */
    boolean existeInscripcion(Long idTorneoCategoria, Long idEquipo);
    
    /**
     * Busca una inscripción específica por torneo-categoría y equipo
     */
    Optional<InscripcionEquipo> buscarPorTorneoCategoriaYEquipo(Long idTorneoCategoria, Long idEquipo);
    
    /**
     * Elimina una inscripción
     */
    void eliminar(Long id);
    
    /**
     * Verifica si existe una inscripción por ID
     */
    boolean existe(Long id);
    
    /**
     * Cuenta inscripciones por torneo-categoría
     */
    long contarPorTorneoCategoria(Long idTorneoCategoria);
    
    /**
     * Cuenta inscripciones por estado
     */
    long contarPorEstado(EstadoInscripcion estado);
}
