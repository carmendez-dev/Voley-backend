package com.voley.dto;

import com.voley.domain.InscripcionEquipo.EstadoInscripcion;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * DTO para InscripcionEquipo
 * 
 * @author Sistema Voley
 * @version 1.0
 */
public class InscripcionEquipoDTO {
    
    private Long idInscripcion;
    
    @NotNull(message = "El ID de torneo-categoría es obligatorio")
    private Long idTorneoCategoria;
    
    @NotNull(message = "El ID del equipo es obligatorio")
    private Long idEquipo;
    
    private EstadoInscripcion estado;
    private String observaciones;
    private LocalDateTime fechaInscripcion;
    
    // Información adicional para respuestas
    private String nombreTorneo;
    private String nombreCategoria;
    private String nombreEquipo;
    
    // Constructores
    public InscripcionEquipoDTO() {
    }
    
    public InscripcionEquipoDTO(Long idTorneoCategoria, Long idEquipo) {
        this.idTorneoCategoria = idTorneoCategoria;
        this.idEquipo = idEquipo;
    }
    
    // Getters y Setters
    public Long getIdInscripcion() {
        return idInscripcion;
    }
    
    public void setIdInscripcion(Long idInscripcion) {
        this.idInscripcion = idInscripcion;
    }
    
    public Long getIdTorneoCategoria() {
        return idTorneoCategoria;
    }
    
    public void setIdTorneoCategoria(Long idTorneoCategoria) {
        this.idTorneoCategoria = idTorneoCategoria;
    }
    
    public Long getIdEquipo() {
        return idEquipo;
    }
    
    public void setIdEquipo(Long idEquipo) {
        this.idEquipo = idEquipo;
    }
    
    public EstadoInscripcion getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoInscripcion estado) {
        this.estado = estado;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public LocalDateTime getFechaInscripcion() {
        return fechaInscripcion;
    }
    
    public void setFechaInscripcion(LocalDateTime fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }
    
    public String getNombreTorneo() {
        return nombreTorneo;
    }
    
    public void setNombreTorneo(String nombreTorneo) {
        this.nombreTorneo = nombreTorneo;
    }
    
    public String getNombreCategoria() {
        return nombreCategoria;
    }
    
    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
    
    public String getNombreEquipo() {
        return nombreEquipo;
    }
    
    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }
}
