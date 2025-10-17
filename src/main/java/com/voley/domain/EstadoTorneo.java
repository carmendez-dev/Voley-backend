package com.voley.domain;

/**
 * Enum que define los posibles estados de un torneo
 * Coincide con la estructura de la base de datos: 'Activo','Finalizado','Pendiente'
 */
public enum EstadoTorneo {
    Pendiente("Pendiente - En preparación"),
    Activo("Activo - Torneo en curso"),
    Finalizado("Finalizado - Torneo terminado");
    
    private final String descripcion;
    
    EstadoTorneo(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    /**
     * Verifica si el torneo está activo (puede tener partidos)
     */
    public boolean esActivo() {
        return this == Activo;
    }
    
    /**
     * Verifica si el torneo acepta inscripciones
     */
    public boolean aceptaInscripciones() {
        return this == Pendiente;
    }
    
    /**
     * Verifica si el torneo está terminado definitivamente
     */
    public boolean estaTerminado() {
        return this == Finalizado;
    }
}