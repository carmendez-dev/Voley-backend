package com.voley.dto;

/**
 * DTO para estadísticas agrupadas por tipo de acción
 */
public class EstadisticaPorTipoAccionDTO {
    
    private String tipoAccion;
    private Long cantidad;
    
    // Constructores
    public EstadisticaPorTipoAccionDTO() {}
    
    public EstadisticaPorTipoAccionDTO(String tipoAccion, Long cantidad) {
        this.tipoAccion = tipoAccion;
        this.cantidad = cantidad;
    }
    
    // Getters y Setters
    public String getTipoAccion() {
        return tipoAccion;
    }
    
    public void setTipoAccion(String tipoAccion) {
        this.tipoAccion = tipoAccion;
    }
    
    public Long getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }
}
