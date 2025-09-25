package com.voley.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Configuración de propiedades relacionadas con los pagos
 */
@Component
@ConfigurationProperties(prefix = "app.pago")
public class PagoConfiguracion {
    
    /**
     * Monto por defecto para pagos mensuales (en Bs)
     */
    private BigDecimal cuotaMensual = new BigDecimal("80.00");
    
    /**
     * Día del mes para vencimiento de pagos (por defecto día 15)
     */
    private Integer diaVencimiento = 15;
    
    /**
     * Si se deben generar pagos automáticamente para nuevos usuarios
     */
    private boolean generarPagosAutomaticos = true;
    
    /**
     * Tipos de usuario que deben tener pagos automáticos
     */
    private String[] tiposUsuarioConPagos = {"jugador", "miembro"};
    
    /**
     * Número de meses hacia adelante para generar pagos
     */
    private int mesesAdelante = 1;
    
    // Getters y Setters
    public BigDecimal getCuotaMensual() {
        return cuotaMensual;
    }
    
    public void setCuotaMensual(BigDecimal cuotaMensual) {
        this.cuotaMensual = cuotaMensual;
    }
    
    public Integer getDiaVencimiento() {
        return diaVencimiento;
    }
    
    public void setDiaVencimiento(Integer diaVencimiento) {
        this.diaVencimiento = diaVencimiento;
    }
    
    public boolean isGenerarPagosAutomaticos() {
        return generarPagosAutomaticos;
    }
    
    public void setGenerarPagosAutomaticos(boolean generarPagosAutomaticos) {
        this.generarPagosAutomaticos = generarPagosAutomaticos;
    }
    
    public String[] getTiposUsuarioConPagos() {
        return tiposUsuarioConPagos;
    }
    
    public void setTiposUsuarioConPagos(String[] tiposUsuarioConPagos) {
        this.tiposUsuarioConPagos = tiposUsuarioConPagos;
    }
    
    public int getMesesAdelante() {
        return mesesAdelante;
    }
    
    public void setMesesAdelante(int mesesAdelante) {
        this.mesesAdelante = mesesAdelante;
    }
}