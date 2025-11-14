package com.voley.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * DTO para actualizar SetPartido
 * Todos los campos son opcionales para permitir actualizaciones parciales
 * 
 * @author Sistema Voley
 * @version 1.0
 */
public class SetPartidoUpdateDTO {
    
    @Min(value = 1, message = "El número de set debe ser al menos 1")
    @Max(value = 5, message = "El número de set no puede ser mayor a 5")
    private Integer numeroSet;
    
    @Min(value = 0, message = "Los puntos no pueden ser negativos")
    private Integer puntosLocal;
    
    @Min(value = 0, message = "Los puntos no pueden ser negativos")
    private Integer puntosVisitante;
    
    // Constructores
    public SetPartidoUpdateDTO() {
    }
    
    // Getters y Setters
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
}
