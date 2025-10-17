package com.voley.service;

import com.voley.adapter.TorneoJpaAdapter;
import com.voley.domain.Torneo;
import com.voley.domain.EstadoTorneo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de torneos
 * Contiene la lógica de negocio para operaciones con torneos
 */
@Service
@Transactional
public class TorneoService {
    
    private static final Logger logger = LoggerFactory.getLogger(TorneoService.class);
    
    private final TorneoJpaAdapter torneoAdapter;
    
    @Autowired
    public TorneoService(TorneoJpaAdapter torneoAdapter) {
        this.torneoAdapter = torneoAdapter;
    }
    
    /**
     * Crear un nuevo torneo
     */
    public Torneo crearTorneo(Torneo torneo) {
        logger.info("Creando nuevo torneo: {}", torneo.getNombre());
        
        // Validar que no exista un torneo con el mismo nombre en las mismas fechas
        if (torneoAdapter.existeTorneoEnFechas(torneo.getNombre(), torneo.getFechaInicio(), torneo.getFechaFin())) {
            throw new IllegalArgumentException("Ya existe un torneo con el mismo nombre en fechas que se superponen");
        }
        
        // Establecer valores por defecto
        if (torneo.getEstado() == null) {
            torneo.setEstado(EstadoTorneo.Pendiente);
        }
        
        Torneo torneoCreado = torneoAdapter.save(torneo);
        logger.info("Torneo creado exitosamente con ID: {}", torneoCreado.getId());
        
        return torneoCreado;
    }
    
    /**
     * Obtener todos los torneos
     */
    @Transactional(readOnly = true)
    public List<Torneo> obtenerTodosLosTorneos() {
        logger.debug("Obteniendo todos los torneos");
        return torneoAdapter.findAllOrderByFechaInicio();
    }
    
    /**
     * Obtener torneo por ID
     */
    @Transactional(readOnly = true)
    public Optional<Torneo> obtenerTorneoPorId(Long id) {
        logger.debug("Buscando torneo con ID: {}", id);
        return torneoAdapter.findById(id);
    }
    
    /**
     * Actualizar un torneo existente
     */
    public Torneo actualizarTorneo(Long id, Torneo torneoActualizado) {
        logger.info("Actualizando torneo con ID: {}", id);
        
        Optional<Torneo> torneoExistente = torneoAdapter.findById(id);
        if (torneoExistente.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el torneo con ID: " + id);
        }
        
        Torneo torneo = torneoExistente.get();
        
        // Validar que el torneo no esté finalizado
        if (torneo.getEstado() == EstadoTorneo.Finalizado) {
            throw new IllegalStateException("No se puede actualizar un torneo finalizado");
        }
        
        // Actualizar campos
        if (torneoActualizado.getNombre() != null) {
            // Validar conflicto de nombres y fechas solo si cambió el nombre o las fechas
            if (!torneo.getNombre().equals(torneoActualizado.getNombre()) ||
                !torneo.getFechaInicio().equals(torneoActualizado.getFechaInicio()) ||
                !torneo.getFechaFin().equals(torneoActualizado.getFechaFin())) {
                
                if (torneoAdapter.existeTorneoEnFechas(torneoActualizado.getNombre(), 
                                                     torneoActualizado.getFechaInicio(), 
                                                     torneoActualizado.getFechaFin())) {
                    throw new IllegalArgumentException("Ya existe un torneo con el mismo nombre en fechas que se superponen");
                }
            }
            torneo.setNombre(torneoActualizado.getNombre());
        }
        
        if (torneoActualizado.getDescripcion() != null) {
            torneo.setDescripcion(torneoActualizado.getDescripcion());
        }
        
        if (torneoActualizado.getFechaInicio() != null) {
            torneo.setFechaInicio(torneoActualizado.getFechaInicio());
        }
        
        if (torneoActualizado.getFechaFin() != null) {
            torneo.setFechaFin(torneoActualizado.getFechaFin());
        }
        
        if (torneoActualizado.getEstado() != null) {
            torneo.setEstado(torneoActualizado.getEstado());
        }
        
        Torneo torneoGuardado = torneoAdapter.save(torneo);
        logger.info("Torneo actualizado exitosamente: {}", torneoGuardado.getId());
        
        return torneoGuardado;
    }
    
    /**
     * Eliminar un torneo
     */
    public void eliminarTorneo(Long id) {
        logger.info("Eliminando torneo con ID: {}", id);
        
        Optional<Torneo> torneo = torneoAdapter.findById(id);
        if (torneo.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el torneo con ID: " + id);
        }
        
        // Validar que el torneo no esté activo
        if (torneo.get().getEstado() == EstadoTorneo.Activo) {
            throw new IllegalStateException("No se puede eliminar un torneo que está activo");
        }
        
        torneoAdapter.deleteById(id);
        logger.info("Torneo eliminado exitosamente: {}", id);
    }
    
    /**
     * Obtener torneos por estado
     */
    @Transactional(readOnly = true)
    public List<Torneo> obtenerTorneosPorEstado(EstadoTorneo estado) {
        logger.debug("Obteniendo torneos con estado: {}", estado);
        return torneoAdapter.findByEstado(estado);
    }
    
    /**
     * Obtener torneos activos
     */
    @Transactional(readOnly = true)
    public List<Torneo> obtenerTorneosActivos() {
        logger.debug("Obteniendo torneos activos");
        return torneoAdapter.findTorneosActivos();
    }
    
    /**
     * Obtener torneos disponibles para inscripción (torneos pendientes)
     */
    @Transactional(readOnly = true)
    public List<Torneo> obtenerTorneosDisponiblesParaInscripcion() {
        logger.debug("Obteniendo torneos disponibles para inscripción");
        return torneoAdapter.findTorneosPendientes();
    }
    
    /**
     * Buscar torneos por nombre
     */
    @Transactional(readOnly = true)
    public List<Torneo> buscarTorneosPorNombre(String nombre) {
        logger.debug("Buscando torneos por nombre: {}", nombre);
        return torneoAdapter.findByNombre(nombre);
    }
    
    /**
     * Cambiar estado de un torneo
     */
    public Torneo cambiarEstadoTorneo(Long id, EstadoTorneo nuevoEstado) {
        logger.info("Cambiando estado del torneo {} a {}", id, nuevoEstado);
        
        Optional<Torneo> torneoOpt = torneoAdapter.findById(id);
        if (torneoOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el torneo con ID: " + id);
        }
        
        Torneo torneo = torneoOpt.get();
        EstadoTorneo estadoAnterior = torneo.getEstado();
        
        // Validar transiciones de estado válidas
        validarTransicionEstado(estadoAnterior, nuevoEstado);
        
        torneo.setEstado(nuevoEstado);
        
        Torneo torneoActualizado = torneoAdapter.save(torneo);
        logger.info("Estado del torneo {} cambiado de {} a {}", id, estadoAnterior, nuevoEstado);
        
        return torneoActualizado;
    }
    
    /**
     * Obtener estadísticas de torneos
     */
    @Transactional(readOnly = true)
    public TorneoEstadisticas obtenerEstadisticas() {
        long total = torneoAdapter.count();
        long pendientes = torneoAdapter.countByEstado(EstadoTorneo.Pendiente);
        long activos = torneoAdapter.countByEstado(EstadoTorneo.Activo);
        long finalizados = torneoAdapter.countByEstado(EstadoTorneo.Finalizado);
        
        return new TorneoEstadisticas(total, pendientes, activos, finalizados);
    }
    
    /**
     * Validar transiciones de estado
     */
    private void validarTransicionEstado(EstadoTorneo estadoActual, EstadoTorneo nuevoEstado) {
        // Un torneo finalizado no puede cambiar de estado
        if (estadoActual == EstadoTorneo.Finalizado) {
            throw new IllegalStateException("No se puede cambiar el estado de un torneo finalizado");
        }
        
        // Un torneo no puede volver a estar pendiente desde otros estados
        if (nuevoEstado == EstadoTorneo.Pendiente && estadoActual != EstadoTorneo.Pendiente) {
            throw new IllegalStateException("Un torneo no puede volver al estado Pendiente");
        }
    }
    
    /**
     * Clase interna para estadísticas
     */
    public static class TorneoEstadisticas {
        private final long total;
        private final long pendientes;
        private final long activos;
        private final long finalizados;
        
        public TorneoEstadisticas(long total, long pendientes, long activos, long finalizados) {
            this.total = total;
            this.pendientes = pendientes;
            this.activos = activos;
            this.finalizados = finalizados;
        }
        
        // Getters
        public long getTotal() { return total; }
        public long getPendientes() { return pendientes; }
        public long getActivos() { return activos; }
        public long getFinalizados() { return finalizados; }
    }
}