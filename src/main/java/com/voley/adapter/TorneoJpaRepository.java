package com.voley.adapter;

import com.voley.domain.Torneo;
import com.voley.domain.EstadoTorneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TorneoJpaRepository extends JpaRepository<Torneo, Long> {
    
    /**
     * Busca torneos por estado
     */
    List<Torneo> findByEstado(EstadoTorneo estado);
    
    /**
     * Busca torneos por nombre (búsqueda parcial, case insensitive)
     */
    List<Torneo> findByNombreContainingIgnoreCase(String nombre);
    
    /**
     * Busca torneos que están en un rango de fechas
     */
    @Query("SELECT t FROM Torneo t WHERE t.fechaInicio >= :fechaDesde AND t.fechaFin <= :fechaHasta")
    List<Torneo> findByFechasBetween(@Param("fechaDesde") LocalDate fechaDesde, 
                                     @Param("fechaHasta") LocalDate fechaHasta);
    
    /**
     * Busca torneos activos
     */
    @Query("SELECT t FROM Torneo t WHERE t.estado = 'Activo'")
    List<Torneo> findTorneosActivos();
    
    /**
     * Busca torneos pendientes
     */
    @Query("SELECT t FROM Torneo t WHERE t.estado = 'Pendiente'")
    List<Torneo> findTorneosPendientes();
    
    /**
     * Busca torneos finalizados
     */
    @Query("SELECT t FROM Torneo t WHERE t.estado = 'Finalizado'")
    List<Torneo> findTorneosFinalizados();
    
    /**
     * Busca torneos próximos a empezar (en los próximos N días)
     */
    @Query("SELECT t FROM Torneo t WHERE t.fechaInicio BETWEEN :fechaActual AND :fechaLimite AND t.estado = 'Pendiente'")
    List<Torneo> findTorneosProximosAEmpezar(@Param("fechaActual") LocalDate fechaActual, 
                                            @Param("fechaLimite") LocalDate fechaLimite);
    
    /**
     * Cuenta torneos por estado
     */
    long countByEstado(EstadoTorneo estado);
    
    /**
     * Busca torneos ordenados por fecha de inicio
     */
    List<Torneo> findAllByOrderByFechaInicioAsc();
    
    /**
     * Verifica si existe un torneo con el mismo nombre en las mismas fechas
     */
    @Query("SELECT COUNT(t) > 0 FROM Torneo t WHERE t.nombre = :nombre AND " +
           "((t.fechaInicio <= :fechaFin AND t.fechaFin >= :fechaInicio) OR " +
           "(:fechaInicio <= t.fechaFin AND :fechaFin >= t.fechaInicio))")
    boolean existeTorneoEnFechas(@Param("nombre") String nombre, 
                                @Param("fechaInicio") LocalDate fechaInicio, 
                                @Param("fechaFin") LocalDate fechaFin);
    
    /**
     * Busca torneos por fecha de inicio
     */
    List<Torneo> findByFechaInicio(LocalDate fechaInicio);
    
    /**
     * Busca torneos por fecha de fin
     */
    List<Torneo> findByFechaFin(LocalDate fechaFin);
    
    /**
     * Busca torneos que estén en curso (fecha actual entre inicio y fin)
     */
    @Query("SELECT t FROM Torneo t WHERE :fechaActual BETWEEN t.fechaInicio AND t.fechaFin")
    List<Torneo> findTorneosEnCurso(@Param("fechaActual") LocalDate fechaActual);
}