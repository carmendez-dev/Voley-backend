package com.voley.dto;

import com.voley.domain.EstadoTorneo;
import java.time.LocalDate;

/**
 * DTO para transferencia de datos de torneos
 * Utilizado para enviar y recibir datos del frontend
 * Coincide con la estructura de la base de datos
 */
public class TorneoDTO {
    
    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private EstadoTorneo estado;
    
    // Constructor vacío
    public TorneoDTO() {}
    
    // Constructor completo
    public TorneoDTO(Long id, String nombre, String descripcion, LocalDate fechaInicio, 
                     LocalDate fechaFin, EstadoTorneo estado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
    
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    
    public LocalDate getFechaFin() {
        return fechaFin;
    }
    
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    public EstadoTorneo getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoTorneo estado) {
        this.estado = estado;
    }
    
    // Métodos de conveniencia
    public boolean estaActivo() {
        return estado == EstadoTorneo.Activo;
    }
    
    public boolean estaPendiente() {
        return estado == EstadoTorneo.Pendiente;
    }
    
    public boolean estaFinalizado() {
        return estado == EstadoTorneo.Finalizado;
    }
    
    public boolean haComenzado() {
        return fechaInicio != null && LocalDate.now().isAfter(fechaInicio);
    }
    
    public boolean haTerminado() {
        return fechaFin != null && LocalDate.now().isAfter(fechaFin);
    }
    
    @Override
    public String toString() {
        return "TorneoDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", estado=" + estado +
                '}';
    }
}