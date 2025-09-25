package com.voley.adapter;

import com.voley.domain.Pago;
import com.voley.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PagoJpaRepository extends JpaRepository<Pago, Long> {
    
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
     * Encuentra todos los pagos por estado
     */
    List<Pago> findByEstado(Pago.EstadoPago estado);
    
    /**
     * Encuentra pagos pendientes que deben pasar a atraso
     */
    @Query("SELECT p FROM Pago p WHERE p.estado = 'PENDIENTE' AND p.fechaRegistro < :fechaLimite")
    List<Pago> findPagosPendientesParaAtraso(@Param("fechaLimite") LocalDate fechaLimite);
    
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
}