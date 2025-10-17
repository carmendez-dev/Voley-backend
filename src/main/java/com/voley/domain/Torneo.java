package com.voley.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "torneo")
public class Torneo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_torneo")
    private Long id;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;
    
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoTorneo estado = EstadoTorneo.Pendiente;
    
    // Constructor vacío
    public Torneo() {
    }
    
    // Constructor con parámetros esenciales
    public Torneo(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
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
    
    // Métodos de negocio
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
    
    public boolean estaEnFechasValidas() {
        if (fechaInicio == null || fechaFin == null) {
            return false;
        }
        LocalDate hoy = LocalDate.now();
        return !hoy.isBefore(fechaInicio) && !hoy.isAfter(fechaFin);
    }
    
    // toString para depuración
    @Override
    public String toString() {
        return "Torneo{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", estado=" + estado +
                '}';
    }
    
    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Torneo torneo = (Torneo) o;
        
        return id != null ? id.equals(torneo.id) : torneo.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}