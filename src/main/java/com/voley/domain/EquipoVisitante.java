package com.voley.domain;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * Entidad EquipoVisitante
 * Representa equipos externos que participan en partidos
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Entity
@Table(name = "equipo_visitante")
public class EquipoVisitante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_equipo_visitante")
    private Long idEquipoVisitante;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    // Constructores
    public EquipoVisitante() {
    }
    
    public EquipoVisitante(String nombre) {
        this.nombre = nombre;
    }
    
    // Getters y Setters
    public Long getIdEquipoVisitante() {
        return idEquipoVisitante;
    }
    
    public void setIdEquipoVisitante(Long idEquipoVisitante) {
        this.idEquipoVisitante = idEquipoVisitante;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    // Métodos de negocio
    public void validar() {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del equipo visitante es obligatorio");
        }
        if (nombre.length() > 100) {
            throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
        }
    }
    
    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EquipoVisitante that = (EquipoVisitante) o;
        return Objects.equals(idEquipoVisitante, that.idEquipoVisitante);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idEquipoVisitante);
    }
    
    @Override
    public String toString() {
        return "EquipoVisitante{" +
                "idEquipoVisitante=" + idEquipoVisitante +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
