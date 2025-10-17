package com.voley.domain;

/**
 * Enum que define los posibles géneros de una categoría
 * Coincide con la estructura de la base de datos: 'Masculino','Femenino','Mixto'
 */
public enum GeneroCategoria {
    Masculino("Masculino - Solo hombres"),
    Femenino("Femenino - Solo mujeres"),
    Mixto("Mixto - Hombres y mujeres");
    
    private final String descripcion;
    
    GeneroCategoria(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    /**
     * Verifica si la categoría es solo para hombres
     */
    public boolean esMasculina() {
        return this == Masculino;
    }
    
    /**
     * Verifica si la categoría es solo para mujeres
     */
    public boolean esFemenina() {
        return this == Femenino;
    }
    
    /**
     * Verifica si la categoría permite ambos géneros
     */
    public boolean esMixta() {
        return this == Mixto;
    }
}