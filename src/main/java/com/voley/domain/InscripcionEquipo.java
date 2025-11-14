package com.voley.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa la inscripción de un equipo en una categoría de un torneo
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Entity
@Table(name = "inscripcion_equipo",
       uniqueConstraints = @UniqueConstraint(columnNames = {"id_torneo_categoria", "id_equipo"}))
public class InscripcionEquipo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inscripcion")
    private Long idInscripcion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_torneo_categoria", nullable = false)
    private TorneoCategoria torneoCategoria;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_equipo", nullable = false)
    private Equipo equipo;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoInscripcion estado = EstadoInscripcion.inscrito;
    
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
    
    @Column(name = "fecha_inscripcion")
    private LocalDateTime fechaInscripcion;
    
    // Enum para estados de inscripción
    public enum EstadoInscripcion {
        inscrito, retirado, descalificado
    }
    
    // Constructores
    public InscripcionEquipo() {
    }
    
    public InscripcionEquipo(TorneoCategoria torneoCategoria, Equipo equipo) {
        this.torneoCategoria = torneoCategoria;
        this.equipo = equipo;
        this.estado = EstadoInscripcion.inscrito;
        this.fechaInscripcion = LocalDateTime.now();
    }
    
    // Callbacks JPA
    @PrePersist
    protected void onCreate() {
        if (fechaInscripcion == null) {
            fechaInscripcion = LocalDateTime.now();
        }
        if (estado == null) {
            estado = EstadoInscripcion.inscrito;
        }
    }
    
    // Métodos de negocio
    public boolean estaInscrito() {
        return EstadoInscripcion.inscrito.equals(this.estado);
    }
    
    public boolean estaRetirado() {
        return EstadoInscripcion.retirado.equals(this.estado);
    }
    
    public boolean estaDescalificado() {
        return EstadoInscripcion.descalificado.equals(this.estado);
    }
    
    public void retirar(String motivo) {
        this.estado = EstadoInscripcion.retirado;
        this.observaciones = motivo;
    }
    
    public void descalificar(String motivo) {
        this.estado = EstadoInscripcion.descalificado;
        this.observaciones = motivo;
    }
    
    public void validar() {
        if (torneoCategoria == null) {
            throw new IllegalArgumentException("La categoría del torneo es obligatoria");
        }
        if (equipo == null) {
            throw new IllegalArgumentException("El equipo es obligatorio");
        }
    }
    
    // Getters y Setters
    public Long getIdInscripcion() {
        return idInscripcion;
    }
    
    public void setIdInscripcion(Long idInscripcion) {
        this.idInscripcion = idInscripcion;
    }
    
    public TorneoCategoria getTorneoCategoria() {
        return torneoCategoria;
    }
    
    public void setTorneoCategoria(TorneoCategoria torneoCategoria) {
        this.torneoCategoria = torneoCategoria;
    }
    
    public Equipo getEquipo() {
        return equipo;
    }
    
    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
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
    
    @Override
    public String toString() {
        return "InscripcionEquipo{" +
                "idInscripcion=" + idInscripcion +
                ", estado=" + estado +
                ", fechaInscripcion=" + fechaInscripcion +
                '}';
    }
}
