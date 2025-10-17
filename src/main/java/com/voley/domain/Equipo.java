package com.voley.domain;

import jakarta.persistence.*;

/**
 * Entidad que representa un Equipo en el sistema de voleibol
 * 
 * Mapea la tabla 'equipo' de la base de datos con los campos:
 * - id_equipo: Identificador único del equipo
 * - nombre: Nombre del equipo (requerido, máximo 100 caracteres)
 * - descripcion: Descripción opcional del equipo
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Entity
@Table(name = "equipo")
public class Equipo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_equipo")
    private Long idEquipo;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    // Constructores
    public Equipo() {
    }
    
    public Equipo(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    
    // Getters y Setters
    public Long getIdEquipo() {
        return idEquipo;
    }
    
    public void setIdEquipo(Long idEquipo) {
        this.idEquipo = idEquipo;
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
    
    // Métodos utilitarios
    @Override
    public String toString() {
        return "Equipo{" +
                "idEquipo=" + idEquipo +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Equipo equipo = (Equipo) o;
        return idEquipo != null ? idEquipo.equals(equipo.idEquipo) : equipo.idEquipo == null;
    }
    
    @Override
    public int hashCode() {
        return idEquipo != null ? idEquipo.hashCode() : 0;
    }
    
    /**
     * Valida que los datos del equipo sean correctos
     * @return true si el equipo es válido
     */
    public boolean esValido() {
        return nombre != null && !nombre.trim().isEmpty() && nombre.length() <= 100;
    }
}