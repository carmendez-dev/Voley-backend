package com.voley.dto;

import com.voley.domain.GeneroCategoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para la creación y actualización de categorías
 */
public class CategoriaDTO {
    
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    private GeneroCategoria genero;
    
    // Constructores
    public CategoriaDTO() {}
    
    public CategoriaDTO(String nombre, GeneroCategoria genero) {
        this.nombre = nombre;
        this.genero = genero;
    }
    
    // Getters y Setters
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
    
    @Override
    public String toString() {
        return "CategoriaDTO{" +
                "nombre='" + nombre + '\'' +
                ", genero=" + genero +
                '}';
    }
}