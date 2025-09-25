package com.voley.repository;

import com.voley.domain.Pago;
import com.voley.domain.Usuario;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PagoRepositoryPort {
    
    /**
     * Guarda un pago en la base de datos
     */
    Pago save(Pago pago);
    
    /**
     * Busca un pago por su ID
     */
    Optional<Pago> findById(Long id);
    
    /**
     * Encuentra todos los pagos de un usuario
     */
    List<Pago> findByUsuario(Usuario usuario);
    
    /**
     * Encuentra todos los pagos de un usuario ordenados por período
     */
    List<Pago> findByUsuarioOrderByPeriodoAnioDescPeriodoMesDesc(Usuario usuario);
    
    /**
     * Busca un pago específico por usuario, mes y año
     */
    Optional<Pago> findByUsuarioAndPeriodoMesAndPeriodoAnio(Usuario usuario, Integer mes, Integer anio);
    
    /**
     * Encuentra todos los pagos pendientes
     */
    List<Pago> findByEstado(Pago.EstadoPago estado);
    
    /**
     * Encuentra pagos pendientes que deben pasar a atraso
     * (pagos pendientes con fecha de registro anterior a la fecha límite)
     */
    List<Pago> findPagosPendientesParaAtraso(LocalDate fechaLimite);
    
    /**
     * Encuentra todos los pagos de un período específico
     */
    List<Pago> findByPeriodoMesAndPeriodoAnio(Integer mes, Integer anio);
    
    /**
     * Encuentra pagos de un usuario en un rango de fechas
     */
    List<Pago> findByUsuarioAndFechaRegistroBetween(Usuario usuario, LocalDate fechaInicio, LocalDate fechaFin);
    
    /**
     * Verifica si existe un pago para un usuario en un período específico
     */
    boolean existsByUsuarioAndPeriodoMesAndPeriodoAnio(Usuario usuario, Integer mes, Integer anio);
    
    /**
     * Cuenta los pagos por estado de un usuario
     */
    long countByUsuarioAndEstado(Usuario usuario, Pago.EstadoPago estado);
    
    /**
     * Elimina un pago
     */
    void delete(Pago pago);
    
    /**
     * Encuentra todos los pagos
     */
    List<Pago> findAll();
}