package com.voley.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para SetPartido
 * 
 * @author Sistema Voley
 * @version 1.0
 */
public class SetPartidoDTO {
    
    private Long idSetPartido;
    
    @NotNull(message = "El ID del partido es obligatorio")
    private Long idPartido;
    
    @NotNull(message = "El número de set es obligatorio")
    @Min(value = 1, message = "El número de set debe ser al menos 1")
    @Max(value = 5, message = "El número de set no puede ser mayor a 5")
    private Integer numeroSet;
    
    @NotNull(message = "Los puntos del equipo local son obligatorios")
    @Min(value = 0, message = "Los puntos no pueden ser negativos")
    private Integer puntosLocal;
    
    @NotNull(message = "Los puntos del equipo visitante son obligatorios")
    @Min(value = 0, message = "Los puntos no pueden ser negativos")
    private Integer puntosVisitante;
    
    // Información adicional para respuestas
    private String nombreEquipoLocal;
    private String nombreEquipoVisitante;
    private String ganador;
    private Boolean finalizado;
    
    // Constructores
    public SetPartidoDTO() {
    }
    
    public SetPartidoDTO(Long idPartido, Integer numeroSet, Integer puntosLocal, Integer puntosVisitante) {
        this.idPartido = idPartido;
        this.numeroSet = numeroSet;
        this.puntosLocal = puntosLocal;
        this.puntosVisitante = puntosVisitante;
    }
    
    // Getters y Setters
    public Long getIdSetPartido() {
        return idSetPartido;
    }
    
    public void setIdSetPartido(Long idSetPartido) {
        this.idSetPartido = idSetPartido;
    }
    
    public Long getIdPartido() {
        return idPartido;
    }
    
    public void setIdPartido(Long idPartido) {
        this.idPartido = idPartido;
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
    
    public String getNombreEquipoLocal() {
        return nombreEquipoLocal;
    }
    
    public void setNombreEquipoLocal(String nombreEquipoLocal) {
        this.nombreEquipoLocal = nombreEquipoLocal;
    }
    
    public String getNombreEquipoVisitante() {
        return nombreEquipoVisitante;
    }
    
    public void setNombreEquipoVisitante(String nombreEquipoVisitante) {
        this.nombreEquipoVisitante = nombreEquipoVisitante;
    }
    
    public String getGanador() {
        return ganador;
    }
    
    public void setGanador(String ganador) {
        this.ganador = ganador;
    }
    
    public Boolean getFinalizado() {
        return finalizado;
    }
    
    public void setFinalizado(Boolean finalizado) {
        this.finalizado = finalizado;
    }
}
