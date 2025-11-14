package com.voley.dto;

import java.util.List;

/**
 * DTO para estadísticas generales de un partido
 */
public class EstadisticasPartidoDTO {
    
    private Long idPartido;
    private String equipoLocal;
    private String equipoVisitante;
    private String resultado;
    private Integer setsGanadosLocal;
    private Integer setsGanadosVisitante;
    
    // Estadísticas del equipo local
    private Integer puntosLocal;
    private Integer erroresLocal;
    private List<EstadisticaPorTipoAccionDTO> puntosPorTipoLocal;
    private List<EstadisticaPorTipoAccionDTO> erroresPorTipoLocal;
    
    // Estadísticas del equipo visitante
    private Integer puntosVisitante;
    private Integer erroresVisitante;
    private List<EstadisticaPorTipoAccionDTO> puntosPorTipoVisitante;
    private List<EstadisticaPorTipoAccionDTO> erroresPorTipoVisitante;
    
    // Constructores
    public EstadisticasPartidoDTO() {}
    
    // Getters y Setters
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
    
    public String getResultado() {
        return resultado;
    }
    
    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
    
    public Integer getSetsGanadosLocal() {
        return setsGanadosLocal;
    }
    
    public void setSetsGanadosLocal(Integer setsGanadosLocal) {
        this.setsGanadosLocal = setsGanadosLocal;
    }
    
    public Integer getSetsGanadosVisitante() {
        return setsGanadosVisitante;
    }
    
    public void setSetsGanadosVisitante(Integer setsGanadosVisitante) {
        this.setsGanadosVisitante = setsGanadosVisitante;
    }
    
    public Integer getPuntosLocal() {
        return puntosLocal;
    }
    
    public void setPuntosLocal(Integer puntosLocal) {
        this.puntosLocal = puntosLocal;
    }
    
    public Integer getErroresLocal() {
        return erroresLocal;
    }
    
    public void setErroresLocal(Integer erroresLocal) {
        this.erroresLocal = erroresLocal;
    }
    
    public List<EstadisticaPorTipoAccionDTO> getPuntosPorTipoLocal() {
        return puntosPorTipoLocal;
    }
    
    public void setPuntosPorTipoLocal(List<EstadisticaPorTipoAccionDTO> puntosPorTipoLocal) {
        this.puntosPorTipoLocal = puntosPorTipoLocal;
    }
    
    public List<EstadisticaPorTipoAccionDTO> getErroresPorTipoLocal() {
        return erroresPorTipoLocal;
    }
    
    public void setErroresPorTipoLocal(List<EstadisticaPorTipoAccionDTO> erroresPorTipoLocal) {
        this.erroresPorTipoLocal = erroresPorTipoLocal;
    }
    
    public Integer getPuntosVisitante() {
        return puntosVisitante;
    }
    
    public void setPuntosVisitante(Integer puntosVisitante) {
        this.puntosVisitante = puntosVisitante;
    }
    
    public Integer getErroresVisitante() {
        return erroresVisitante;
    }
    
    public void setErroresVisitante(Integer erroresVisitante) {
        this.erroresVisitante = erroresVisitante;
    }
    
    public List<EstadisticaPorTipoAccionDTO> getPuntosPorTipoVisitante() {
        return puntosPorTipoVisitante;
    }
    
    public void setPuntosPorTipoVisitante(List<EstadisticaPorTipoAccionDTO> puntosPorTipoVisitante) {
        this.puntosPorTipoVisitante = puntosPorTipoVisitante;
    }
    
    public List<EstadisticaPorTipoAccionDTO> getErroresPorTipoVisitante() {
        return erroresPorTipoVisitante;
    }
    
    public void setErroresPorTipoVisitante(List<EstadisticaPorTipoAccionDTO> erroresPorTipoVisitante) {
        this.erroresPorTipoVisitante = erroresPorTipoVisitante;
    }
}
