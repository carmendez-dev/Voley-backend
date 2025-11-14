package com.voley.dto;

import java.util.List;

/**
 * DTO para estadísticas de un jugador en un partido
 */
public class EstadisticasJugadorDTO {
    
    private Long idRoster;
    private String nombreJugador;
    private Long idPartido;
    private String equipoLocal;
    private String equipoVisitante;
    
    private Integer totalPuntos;
    private Integer totalErrores;
    private List<EstadisticaPorTipoAccionDTO> puntosPorTipo;
    private List<EstadisticaPorTipoAccionDTO> erroresPorTipo;
    
    // Constructores
    public EstadisticasJugadorDTO() {}
    
    // Getters y Setters
    public Long getIdRoster() {
        return idRoster;
    }
    
    public void setIdRoster(Long idRoster) {
        this.idRoster = idRoster;
    }
    
    public String getNombreJugador() {
        return nombreJugador;
    }
    
    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }
    
    public Long getIdPartido() {
        return idPartido;
    }
    
    public void setIdPartido(Long idPartido) {
        this.idPartido = idPartido;
    }
    
    public String getEquipoLocal() {
        return equipoLocal;
    }
    
    public void setEquipoLocal(String equipoLocal) {
        this.equipoLocal = equipoLocal;
    }
    
    public String getEquipoVisitante() {
        return equipoVisitante;
    }
    
    public void setEquipoVisitante(String equipoVisitante) {
        this.equipoVisitante = equipoVisitante;
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
    
    public List<EstadisticaPorTipoAccionDTO> getPuntosPorTipo() {
        return puntosPorTipo;
    }
    
    public void setPuntosPorTipo(List<EstadisticaPorTipoAccionDTO> puntosPorTipo) {
        this.puntosPorTipo = puntosPorTipo;
    }
    
    public List<EstadisticaPorTipoAccionDTO> getErroresPorTipo() {
        return erroresPorTipo;
    }
    
    public void setErroresPorTipo(List<EstadisticaPorTipoAccionDTO> erroresPorTipo) {
        this.erroresPorTipo = erroresPorTipo;
    }
}
