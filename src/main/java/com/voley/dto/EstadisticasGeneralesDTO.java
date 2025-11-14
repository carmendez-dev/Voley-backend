package com.voley.dto;

/**
 * DTO para estadísticas generales del dashboard
 */
public class EstadisticasGeneralesDTO {
    
    private Integer totalPartidos;
    private Integer partidosGanados;
    private Integer partidosPerdidos;
    private Integer partidosWalkover;
    private Integer partidosWalkoverContra;
    private Integer partidosPendientes;
    
    private Integer totalSetsJugados;
    private Integer setsGanados;
    private Integer setsPerdidos;
    
    private Integer totalPuntos;
    private Integer totalErrores;
    
    // Constructores
    public EstadisticasGeneralesDTO() {}
    
    // Getters y Setters
    public Integer getTotalPartidos() {
        return totalPartidos;
    }
    
    public void setTotalPartidos(Integer totalPartidos) {
        this.totalPartidos = totalPartidos;
    }
    
    public Integer getPartidosGanados() {
        return partidosGanados;
    }
    
    public void setPartidosGanados(Integer partidosGanados) {
        this.partidosGanados = partidosGanados;
    }
    
    public Integer getPartidosPerdidos() {
        return partidosPerdidos;
    }
    
    public void setPartidosPerdidos(Integer partidosPerdidos) {
        this.partidosPerdidos = partidosPerdidos;
    }
    
    public Integer getPartidosWalkover() {
        return partidosWalkover;
    }
    
    public void setPartidosWalkover(Integer partidosWalkover) {
        this.partidosWalkover = partidosWalkover;
    }
    
    public Integer getPartidosWalkoverContra() {
        return partidosWalkoverContra;
    }
    
    public void setPartidosWalkoverContra(Integer partidosWalkoverContra) {
        this.partidosWalkoverContra = partidosWalkoverContra;
    }
    
    public Integer getPartidosPendientes() {
        return partidosPendientes;
    }
    
    public void setPartidosPendientes(Integer partidosPendientes) {
        this.partidosPendientes = partidosPendientes;
    }
    
    public Integer getTotalSetsJugados() {
        return totalSetsJugados;
    }
    
    public void setTotalSetsJugados(Integer totalSetsJugados) {
        this.totalSetsJugados = totalSetsJugados;
    }
    
    public Integer getSetsGanados() {
        return setsGanados;
    }
    
    public void setSetsGanados(Integer setsGanados) {
        this.setsGanados = setsGanados;
    }
    
    public Integer getSetsPerdidos() {
        return setsPerdidos;
    }
    
    public void setSetsPerdidos(Integer setsPerdidos) {
        this.setsPerdidos = setsPerdidos;
    }
    
    public Integer getTotalPuntos() {
        return totalPuntos;
    }
    
    public void setTotalPuntos(Integer totalPuntos) {
        this.totalPuntos = totalPuntos;
    }
    
    public Integer getTotalErrores() {
        return totalErrores;
    }
    
    public void setTotalErrores(Integer totalErrores) {
        this.totalErrores = totalErrores;
    }
}
