package com.voley.dto;

/**
 * DTO para transferencia de datos de AccionJuego
 */
public class AccionJuegoDTO {
    
    private Long idAccionJuego;
    private Long idSetPartido;
    private Integer idTipoAccion;
    private String tipoAccionDescripcion;
    private Integer idResultadoAccion;
    private String resultadoAccionDescripcion;
    private Long idRoster;
    private String nombreJugador;
    private Byte posicionVisitante;
    
    // Constructores
    public AccionJuegoDTO() {}
    
    // Getters y Setters
    public Long getIdAccionJuego() {
        return idAccionJuego;
    }
    
    public void setIdAccionJuego(Long idAccionJuego) {
        this.idAccionJuego = idAccionJuego;
    }
    
    public Long getIdSetPartido() {
        return idSetPartido;
    }
    
    public void setIdSetPartido(Long idSetPartido) {
        this.idSetPartido = idSetPartido;
    }
    
    public Integer getIdTipoAccion() {
        return idTipoAccion;
    }
    
    public void setIdTipoAccion(Integer idTipoAccion) {
        this.idTipoAccion = idTipoAccion;
    }
    
    public String getTipoAccionDescripcion() {
        return tipoAccionDescripcion;
    }
    
    public void setTipoAccionDescripcion(String tipoAccionDescripcion) {
        this.tipoAccionDescripcion = tipoAccionDescripcion;
    }
    
    public Integer getIdResultadoAccion() {
        return idResultadoAccion;
    }
    
    public void setIdResultadoAccion(Integer idResultadoAccion) {
        this.idResultadoAccion = idResultadoAccion;
    }
    
    public String getResultadoAccionDescripcion() {
        return resultadoAccionDescripcion;
    }
    
    public void setResultadoAccionDescripcion(String resultadoAccionDescripcion) {
        this.resultadoAccionDescripcion = resultadoAccionDescripcion;
    }
    
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
    
    public Byte getPosicionVisitante() {
        return posicionVisitante;
    }
    
    public void setPosicionVisitante(Byte posicionVisitante) {
        this.posicionVisitante = posicionVisitante;
    }
}
