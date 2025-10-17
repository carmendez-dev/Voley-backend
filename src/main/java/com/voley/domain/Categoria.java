package com.voley.domain;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * Entidad que representa una Categoría en el sistema
 * Maneja información sobre categorías de voleibol (ej: Sub-18 Masculino, Profesional Femenino, etc.)
 */
@Entity
@Table(name = "categoria")
public class Categoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long idCategoria;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "genero", nullable = true)
    private GeneroCategoria genero;
    
    // Constructores
    public Categoria() {}
    
    public Categoria(String nombre, GeneroCategoria genero) {
        this.nombre = nombre;
        this.genero = genero;
    }
    
    // Getters y Setters
    public Long getIdCategoria() {
        return idCategoria;
    }
    
    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public GeneroCategoria getGenero() {
        return genero;
    }
    
    public void setGenero(GeneroCategoria genero) {
        this.genero = genero;
    }
    
    // Métodos de negocio
    
    /**
     * Verifica si es una categoría mixta
     */
    public boolean esMixta() {
        return genero != null && genero.esMixta();
    }
    
    /**
     * Verifica si es una categoría solo masculina
     */
    public boolean esMasculina() {
        return genero != null && genero.esMasculina();
    }
    
    /**
     * Verifica si es una categoría solo femenina
     */
    public boolean esFemenina() {
        return genero != null && genero.esFemenina();
    }
    
    /**
     * Obtiene una representación legible de la categoría
     */
    public String getDescripcionCompleta() {
        if (genero == null) {
            return nombre;
        }
        return nombre + " - " + genero.getDescripcion();
    }
    
    // Validaciones
    
    /**
     * Valida que los datos de la categoría sean correctos
     */
    public void validar() {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío");
        }
        
        if (nombre.length() > 100) {
            throw new IllegalArgumentException("El nombre de la categoría no puede exceder 100 caracteres");
        }
    }
    
    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return Objects.equals(idCategoria, categoria.idCategoria);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idCategoria);
    }
    
    @Override
    public String toString() {
        return "Categoria{" +
                "idCategoria=" + idCategoria +
                ", nombre='" + nombre + '\'' +
                ", genero=" + genero +
                '}';
    }
}