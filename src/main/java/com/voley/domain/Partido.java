package com.voley.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad Partido
 * Representa un partido entre un equipo local (inscrito) y un equipo visitante
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Entity
@Table(name = "partido")
public class Partido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_partido")
    private Long idPartido;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_inscripcion_local", nullable = false)
    private InscripcionEquipo inscripcionLocal;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_equipo_visitante", nullable = false)
    private EquipoVisitante equipoVisitante;
    
    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;
    
    @Column(name = "ubicacion", length = 255)
    private String ubicacion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "resultado", nullable = false)
    private ResultadoPartido resultado = ResultadoPartido.Pendiente;
    
    /**
     * Enum para el resultado del partido
     */
    public enum ResultadoPartido {
        Pendiente,
        Ganado,
        Perdido,
        Walkover,
        WalkoverContra
    }
    
    // Constructores
    public Partido() {
    }
    
    public Partido(InscripcionEquipo inscripcionLocal, EquipoVisitante equipoVisitante, 
                   LocalDateTime fecha, String ubicacion) {
        this.inscripcionLocal = inscripcionLocal;
        this.equipoVisitante = equipoVisitante;
        this.fecha = fecha;
        this.ubicacion = ubicacion;
        this.resultado = ResultadoPartido.Pendiente;
    }
    
    // Getters y Setters
    public Long getIdPartido() {
        return idPartido;
    }
    
    public void setIdPartido(Long idPartido) {
        this.idPartido = idPartido;
    }
    
    public InscripcionEquipo getInscripcionLocal() {
        return inscripcionLocal;
    }
    
    public void setInscripcionLocal(InscripcionEquipo inscripcionLocal) {
        this.inscripcionLocal = inscripcionLocal;
    }
    
    public EquipoVisitante getEquipoVisitante() {
        return equipoVisitante;
    }
    
    public void setEquipoVisitante(EquipoVisitante equipoVisitante) {
        this.equipoVisitante = equipoVisitante;
    }
    
    public LocalDateTime getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    
    public String getUbicacion() {
        return ubicacion;
    }
    
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
    
    public ResultadoPartido getResultado() {
        return resultado;
    }
    
    public void setResultado(ResultadoPartido resultado) {
        this.resultado = resultado;
    }
    
    // Métodos de negocio
    public void validar() {
        if (inscripcionLocal == null) {
            throw new IllegalArgumentException("La inscripción local es obligatoria");
        }
        if (equipoVisitante == null) {
            throw new IllegalArgumentException("El equipo visitante es obligatorio");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha del partido es obligatoria");
        }
        if (ubicacion != null && ubicacion.length() > 255) {
            throw new IllegalArgumentException("La ubicación no puede exceder 255 caracteres");
        }
    }
    
    public boolean esPendiente() {
        return resultado == ResultadoPartido.Pendiente;
    }
    
    public boolean estaFinalizado() {
        return resultado != ResultadoPartido.Pendiente;
    }
    
    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Partido partido = (Partido) o;
        return Objects.equals(idPartido, partido.idPartido);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idPartido);
    }
    
    @Override
    public String toString() {
        return "Partido{" +
                "idPartido=" + idPartido +
                ", inscripcionLocal=" + (inscripcionLocal != null ? inscripcionLocal.getIdInscripcion() : null) +
                ", equipoVisitante=" + (equipoVisitante != null ? equipoVisitante.getIdEquipoVisitante() : null) +
                ", fecha=" + fecha +
                ", resultado=" + resultado +
                '}';
    }
}
