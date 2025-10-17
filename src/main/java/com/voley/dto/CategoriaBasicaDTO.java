package com.voley.dto;

/**
 * DTO para mostrar información básica de una categoría
 * Utilizado en respuestas donde solo se necesita ID y nombre
 */
public class CategoriaBasicaDTO {
    
    private Long idCategoria;
    private String nombre;
    private Long idTorneo;
    private String nombreTorneo;
    
    // Constructores
    public CategoriaBasicaDTO() {}
    
    public CategoriaBasicaDTO(Long idCategoria, String nombre) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
    }
    
    public CategoriaBasicaDTO(Long idCategoria, String nombre, Long idTorneo, String nombreTorneo) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.idTorneo = idTorneo;
        this.nombreTorneo = nombreTorneo;
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
    
    public Long getIdTorneo() {
        return idTorneo;
    }
    
    public void setIdTorneo(Long idTorneo) {
        this.idTorneo = idTorneo;
    }
    
    public String getNombreTorneo() {
        return nombreTorneo;
    }
    
    public void setNombreTorneo(String nombreTorneo) {
        this.nombreTorneo = nombreTorneo;
    }
    
    @Override
    public String toString() {
        return "CategoriaBasicaDTO{" +
                "idCategoria=" + idCategoria +
                ", nombre='" + nombre + '\'' +
                ", idTorneo=" + idTorneo +
                ", nombreTorneo='" + nombreTorneo + '\'' +
                '}';
    }
}