package com.voley.adapter;

import com.voley.domain.Torneo;
import com.voley.domain.EstadoTorneo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador JPA para el manejo de torneos
 * Implementa el patrón Repository usando Spring Data JPA
 */
@Component
public class TorneoJpaAdapter {
    
    private final TorneoJpaRepository torneoRepository;
    
    @Autowired
    public TorneoJpaAdapter(TorneoJpaRepository torneoRepository) {
        this.torneoRepository = torneoRepository;
    }
    
    /**
     * Guarda un torneo (crear o actualizar)
     */
    public Torneo save(Torneo torneo) {
        return torneoRepository.save(torneo);
    }
    
    /**
     * Busca un torneo por ID
     */
    public Optional<Torneo> findById(Long id) {
        return torneoRepository.findById(id);
    }
    
    /**
     * Obtiene todos los torneos
     */
    public List<Torneo> findAll() {
        return torneoRepository.findAll();
    }
    
    /**
     * Elimina un torneo por ID
     */
    public void deleteById(Long id) {
        torneoRepository.deleteById(id);
    }
    
    /**
     * Verifica si existe un torneo por ID
     */
    public boolean existsById(Long id) {
        return torneoRepository.existsById(id);
    }
    
    /**
     * Busca torneos por estado
     */
    public List<Torneo> findByEstado(EstadoTorneo estado) {
        return torneoRepository.findByEstado(estado);
    }
    
    /**
     * Busca torneos por nombre (búsqueda parcial)
     */
    public List<Torneo> findByNombre(String nombre) {
        return torneoRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    /**
     * Busca torneos en un rango de fechas
     */
    public List<Torneo> findByFechasBetween(LocalDate fechaDesde, LocalDate fechaHasta) {
        return torneoRepository.findByFechasBetween(fechaDesde, fechaHasta);
    }
    
    /**
     * Busca torneos activos
     */
    public List<Torneo> findTorneosActivos() {
        return torneoRepository.findTorneosActivos();
    }
    
    /**
     * Busca torneos pendientes
     */
    public List<Torneo> findTorneosPendientes() {
        return torneoRepository.findTorneosPendientes();
    }
    
    /**
     * Busca torneos finalizados
     */
    public List<Torneo> findTorneosFinalizados() {
        return torneoRepository.findTorneosFinalizados();
    }
    
    /**
     * Busca torneos próximos a empezar (en los próximos 7 días)
     */
    public List<Torneo> findTorneosProximosAEmpezar() {
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaLimite = fechaActual.plusDays(7);
        return torneoRepository.findTorneosProximosAEmpezar(fechaActual, fechaLimite);
    }
    
    /**
     * Busca torneos en curso
     */
    public List<Torneo> findTorneosEnCurso() {
        return torneoRepository.findTorneosEnCurso(LocalDate.now());
    }
    
    /**
     * Cuenta torneos por estado
     */
    public long countByEstado(EstadoTorneo estado) {
        return torneoRepository.countByEstado(estado);
    }
    
    /**
     * Obtiene todos los torneos ordenados por fecha de inicio
     */
    public List<Torneo> findAllOrderByFechaInicio() {
        return torneoRepository.findAllByOrderByFechaInicioAsc();
    }
    
    /**
     * Verifica si existe un torneo con conflicto de fechas y nombre
     */
    public boolean existeTorneoEnFechas(String nombre, LocalDate fechaInicio, LocalDate fechaFin) {
        return torneoRepository.existeTorneoEnFechas(nombre, fechaInicio, fechaFin);
    }
    
    /**
     * Cuenta el total de torneos
     */
    public long count() {
        return torneoRepository.count();
    }
}