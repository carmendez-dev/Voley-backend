package com.voley.service;

import com.voley.domain.Pago;
import com.voley.domain.Usuario;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PagoService {
    
    /**
     * Crea un nuevo pago
     */
    Pago crearPago(Pago pago);
    
    /**
     * Busca un pago por ID
     */
    Optional<Pago> obtenerPagoPorId(Long id);
    
    /**
     * Obtiene todos los pagos de un usuario
     */
    List<Pago> obtenerPagosPorUsuario(Usuario usuario);
    
    /**
     * Obtiene los pagos de un usuario ordenados por período (más reciente primero)
     */
    List<Pago> obtenerPagosPorUsuarioOrdenados(Usuario usuario);
    
    /**
     * Busca un pago específico por usuario y período
     */
    Optional<Pago> obtenerPagoPorUsuarioYPeriodo(Usuario usuario, Integer mes, Integer anio);
    
    /**
     * Obtiene pagos por estado
     */
    List<Pago> obtenerPagosPorEstado(Pago.EstadoPago estado);
    
    /**
     * Genera pagos automáticamente para un usuario recién registrado
     * Crea el pago del mes actual y del siguiente mes
     */
    void generarPagosParaNuevoUsuario(Usuario usuario);
    
    /**
     * Genera pagos mensuales automáticamente para todos los usuarios activos
     * Se ejecuta el primer día de cada mes
     */
    void generarPagosMensualesAutomaticos();
    
    /**
     * Actualiza pagos pendientes a estado ATRASO después de la fecha límite
     */
    void actualizarPagosEnAtraso();
    
    /**
     * Marcar un pago como pagado
     */
    Pago marcarComoPagado(Long pagoId, Double monto, String metodoPago);
    
    /**
     * Marcar un pago como rechazado
     */
    Pago marcarComoRechazado(Long pagoId, String motivoRechazo);
    
    /**
     * Verifica si un usuario tiene pagos pendientes o en atraso
     */
    boolean tieneDeudas(Usuario usuario);
    
    /**
     * Obtiene el número de pagos en atraso de un usuario
     */
    long contarPagosEnAtraso(Usuario usuario);
    
    /**
     * Obtiene los pagos de un período específico
     */
    List<Pago> obtenerPagosPorPeriodo(Integer mes, Integer anio);
    
    /**
     * Elimina un pago (solo si está en estado PENDIENTE)
     */
    boolean eliminarPago(Long pagoId);
    
    /**
     * Obtiene todos los pagos del sistema
     */
    List<Pago> obtenerTodosLosPagos();
    
    /**
     * Actualiza un pago existente
     */
    Pago actualizarPago(Pago pago);
}