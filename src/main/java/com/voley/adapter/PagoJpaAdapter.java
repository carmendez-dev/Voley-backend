package com.voley.adapter;

import com.voley.domain.Pago;
import com.voley.domain.Usuario;
import com.voley.repository.PagoRepositoryPort;
import com.voley.adapter.PagoJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class PagoJpaAdapter implements PagoRepositoryPort {
    
    private static final Logger logger = LoggerFactory.getLogger(PagoJpaAdapter.class);
    private final PagoJpaRepository pagoJpaRepository;
    
    @Autowired
    public PagoJpaAdapter(PagoJpaRepository pagoJpaRepository) {
        this.pagoJpaRepository = pagoJpaRepository;
    }
    
    @Override
    public Pago save(Pago pago) {
        logger.debug("🏛️ ADAPTER: Intentando guardar pago en base de datos: {}", pago);
        Pago pagoGuardado = pagoJpaRepository.save(pago);
        logger.debug("🏛️ ADAPTER: Pago guardado exitosamente con ID: {}", pagoGuardado.getId());
        return pagoGuardado;
    }
    
    @Override
    public Optional<Pago> findById(Long id) {
        return pagoJpaRepository.findById(id);
    }
    
    @Override
    public List<Pago> findByUsuario(Usuario usuario) {
        return pagoJpaRepository.findByUsuario(usuario);
    }
    
    @Override
    public List<Pago> findByUsuarioOrderByPeriodoAnioDescPeriodoMesDesc(Usuario usuario) {
        return pagoJpaRepository.findByUsuarioOrderByPeriodoAnioDescPeriodoMesDesc(usuario);
    }
    
    @Override
    public Optional<Pago> findByUsuarioAndPeriodoMesAndPeriodoAnio(Usuario usuario, Integer mes, Integer anio) {
        return pagoJpaRepository.findByUsuarioAndPeriodoMesAndPeriodoAnio(usuario, mes, anio);
    }
    
    @Override
    public List<Pago> findByEstado(Pago.EstadoPago estado) {
        return pagoJpaRepository.findByEstado(estado);
    }
    
    @Override
    public List<Pago> findPagosPendientesParaAtraso(LocalDate fechaLimite) {
        return pagoJpaRepository.findPagosPendientesParaAtraso(fechaLimite);
    }
    
    @Override
    public List<Pago> findByPeriodoMesAndPeriodoAnio(Integer mes, Integer anio) {
        return pagoJpaRepository.findByPeriodoMesAndPeriodoAnio(mes, anio);
    }
    
    @Override
    public List<Pago> findByUsuarioAndFechaRegistroBetween(Usuario usuario, LocalDate fechaInicio, LocalDate fechaFin) {
        return pagoJpaRepository.findByUsuarioAndFechaRegistroBetween(usuario, fechaInicio, fechaFin);
    }
    
    @Override
    public boolean existsByUsuarioAndPeriodoMesAndPeriodoAnio(Usuario usuario, Integer mes, Integer anio) {
        return pagoJpaRepository.existsByUsuarioAndPeriodoMesAndPeriodoAnio(usuario, mes, anio);
    }
    
    @Override
    public long countByUsuarioAndEstado(Usuario usuario, Pago.EstadoPago estado) {
        return pagoJpaRepository.countByUsuarioAndEstado(usuario, estado);
    }
    
    @Override
    public void delete(Pago pago) {
        pagoJpaRepository.delete(pago);
    }
    
    @Override
    public List<Pago> findAll() {
        return pagoJpaRepository.findAll();
    }
}