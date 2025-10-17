package com.voley.domain;

import jakarta.persistence.*;

/**
 * Entidad que representa la relación many-to-many entre Categoria y Equipo
 * 
 * Mapea la tabla 'categoria_equipo' de la base de datos con los campos:
 * - id_categoria_equipo: Identificador único de la relación
 * - id_categoria: Referencia a la categoría
 * - id_equipo: Referencia al equipo
 * 
 * Esta entidad permite que:
 * - Una categoría pueda tener múltiples equipos
 * - Un equipo pueda participar en múltiples categorías/torneos
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Entity
@Table(name = "categoria_equipo")
public class CategoriaEquipo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria_equipo")
    private Long idCategoriaEquipo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_equipo", nullable = false)
    private Equipo equipo;
    
    // Constructores
    public CategoriaEquipo() {
    }
    
    public CategoriaEquipo(Categoria categoria, Equipo equipo) {
        this.categoria = categoria;
        this.equipo = equipo;
    }
    
    // Getters y Setters
    public Long getIdCategoriaEquipo() {
        return idCategoriaEquipo;
    }
    
    public void setIdCategoriaEquipo(Long idCategoriaEquipo) {
        this.idCategoriaEquipo = idCategoriaEquipo;
    }
    
    public Categoria getCategoria() {
        return categoria;
    }
    
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    
    public Equipo getEquipo() {
        return equipo;
    }
    
    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }
    
    // Métodos utilitarios
    @Override
    public String toString() {
        return "CategoriaEquipo{" +
                "idCategoriaEquipo=" + idCategoriaEquipo +
                ", categoria=" + (categoria != null ? categoria.getNombre() : "null") +
                ", equipo=" + (equipo != null ? equipo.getNombre() : "null") +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        CategoriaEquipo that = (CategoriaEquipo) o;
        
        if (categoria != null && that.categoria != null && 
            equipo != null && that.equipo != null) {
            return categoria.getIdCategoria().equals(that.categoria.getIdCategoria()) &&
                   equipo.getIdEquipo().equals(that.equipo.getIdEquipo());
        }
        
        return idCategoriaEquipo != null ? 
               idCategoriaEquipo.equals(that.idCategoriaEquipo) : 
               that.idCategoriaEquipo == null;
    }
    
    @Override
    public int hashCode() {
        if (categoria != null && equipo != null) {
            return categoria.getIdCategoria().hashCode() + equipo.getIdEquipo().hashCode();
        }
        return idCategoriaEquipo != null ? idCategoriaEquipo.hashCode() : 0;
    }
    
    /**
     * Valida que la relación sea válida
     * @return true si la relación es válida
     */
    public boolean esValida() {
        return categoria != null && equipo != null &&
               categoria.getIdCategoria() != null && equipo.getIdEquipo() != null;
    }
}