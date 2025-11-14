package com.voley.domain;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * Entidad SetPartido
 * Representa un set dentro de un partido de voleibol
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Entity
@Table(name = "set_partido")
public class SetPartido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_set_partido")
    private Long idSetPartido;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_partido", nullable = false)
    private Partido partido;
    
    @Column(name = "numero_set", nullable = false)
    private Integer numeroSet;
    
    @Column(name = "puntos_local", nullable = false)
    private Integer puntosLocal = 0;
    
    @Column(name = "puntos_visitante", nullable = false)
    private Integer puntosVisitante = 0;
    
    // Constructores
    public SetPartido() {
    }
    
    public SetPartido(Partido partido, Integer numeroSet) {
        this.partido = partido;
        this.numeroSet = numeroSet;
        this.puntosLocal = 0;
        this.puntosVisitante = 0;
    }
    
    // Getters y Setters
    public Long getIdSetPartido() {
        return idSetPartido;
    }
    
    public void setIdSetPartido(Long idSetPartido) {
        this.idSetPartido = idSetPartido;
    }
    
    public Partido getPartido() {
        return partido;
    }
    
    public void setPartido(Partido partido) {
        this.partido = partido;
    }
    
    public Integer getNumeroSet() {
        return numeroSet;
    }
    
    public void setNumeroSet(Integer numeroSet) {
        this.numeroSet = numeroSet;
    }
    
    public Integer getPuntosLocal() {
        return puntosLocal;
    }
    
    public void setPuntosLocal(Integer puntosLocal) {
        this.puntosLocal = puntosLocal;
    }
    
    public Integer getPuntosVisitante() {
        return puntosVisitante;
    }
    
    public void setPuntosVisitante(Integer puntosVisitante) {
        this.puntosVisitante = puntosVisitante;
    }
    
    // Métodos de negocio
    public void validar() {
        if (partido == null) {
            throw new IllegalArgumentException("El partido es obligatorio");
        }
        if (numeroSet == null || numeroSet < 1 || numeroSet > 5) {
            throw new IllegalArgumentException("El número de set debe estar entre 1 y 5");
        }
        if (puntosLocal == null || puntosLocal < 0) {
            throw new IllegalArgumentException("Los puntos del equipo local no pueden ser negativos");
        }
        if (puntosVisitante == null || puntosVisitante < 0) {
            throw new IllegalArgumentException("Los puntos del equipo visitante no pueden ser negativos");
        }
    }
    
    public String getGanador() {
        if (puntosLocal > puntosVisitante) {
            return "Local";
        } else if (puntosVisitante > puntosLocal) {
            return "Visitante";
        }
        return "Empate";
    }
    
    public boolean estaFinalizado() {
        // Set normal: gana el primero en llegar a 25 con diferencia de 2
        if (numeroSet < 5) {
            return (puntosLocal >= 25 || puntosVisitante >= 25) && 
                   Math.abs(puntosLocal - puntosVisitante) >= 2;
        }
        // Set decisivo (5to): gana el primero en llegar a 15 con diferencia de 2
        return (puntosLocal >= 15 || puntosVisitante >= 15) && 
               Math.abs(puntosLocal - puntosVisitante) >= 2;
    }
    
    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SetPartido that = (SetPartido) o;
        return Objects.equals(idSetPartido, that.idSetPartido);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idSetPartido);
    }
    
    @Override
    public String toString() {
        return "SetPartido{" +
                "idSetPartido=" + idSetPartido +
                ", partido=" + (partido != null ? partido.getIdPartido() : null) +
                ", numeroSet=" + numeroSet +
                ", puntosLocal=" + puntosLocal +
                ", puntosVisitante=" + puntosVisitante +
                '}';
    }
}
