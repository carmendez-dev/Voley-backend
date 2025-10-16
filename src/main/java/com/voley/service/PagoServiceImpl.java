package com.voley.service;

import com.voley.config.PagoConfiguracion;
import com.voley.domain.Pago;
import com.voley.domain.Usuario;
import com.voley.repository.PagoRepositoryPort;
import com.voley.repository.UsuarioRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PagoServiceImpl implements PagoService {
    
    private static final Logger logger = LoggerFactory.getLogger(PagoServiceImpl.class);
    
    private final PagoRepositoryPort pagoRepository;
    private final UsuarioRepositoryPort usuarioRepository;
    private final PagoConfiguracion pagoConfig;
    
    @Autowired
    public PagoServiceImpl(PagoRepositoryPort pagoRepository, 
                          UsuarioRepositoryPort usuarioRepository,
                          PagoConfiguracion pagoConfig) {
        this.pagoRepository = pagoRepository;
        this.usuarioRepository = usuarioRepository;
        this.pagoConfig = pagoConfig;
    }
    
    @Override
    public Pago crearPago(Pago pago) {
        logger.debug("Creando pago manual: usuarioId={}, monto={}, estado={}", 
                    pago.getUsuario() != null ? pago.getUsuario().getId() : "null",
                    pago.getMonto(), pago.getEstado());
        
        // Validaciones obligatorias
        if (pago.getUsuario() == null) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }
        if (pago.getPeriodoMes() == null || pago.getPeriodoAnio() == null) {
            throw new IllegalArgumentException("El período (mes y año) es obligatorio");
        }
        if (pago.getMonto() == null) {
            throw new IllegalArgumentException("El monto es obligatorio");
        }
        if (pago.getEstado() == null) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }
        if (pago.getMetodoPago() == null || pago.getMetodoPago().trim().isEmpty()) {
            throw new IllegalArgumentException("El método de pago es obligatorio");
        }
        
        // Establecer valores automáticos
        if (pago.getFechaRegistro() == null) {
            pago.setFechaRegistro(LocalDate.now());
        }
        
        // Generar fecha de vencimiento automáticamente
        if (pago.getFechaVencimiento() == null) {
            LocalDate fechaPeriodo = LocalDate.of(pago.getPeriodoAnio(), pago.getPeriodoMes(), 1);
            pago.setFechaVencimiento(calcularFechaVencimiento(fechaPeriodo));
        }
        
        // Si el estado es "pagado", establecer fecha_pago automáticamente
        if (Pago.EstadoPago.pagado.equals(pago.getEstado())) {
            if (pago.getFechaPago() == null) {
                pago.setFechaPago(LocalDate.now());
            }
            logger.debug("Pago marcado como pagado con fecha: {}", pago.getFechaPago());
        }
        
        // Establecer nombre del usuario automáticamente
        if (pago.getUsuarioNombre() == null || pago.getUsuarioNombre().trim().isEmpty()) {
            pago.setUsuarioNombre(pago.getUsuario().getNombreCompleto());
        }
        
        logger.debug("Guardando pago: periodo={}/{}, vencimiento={}, estado={}, metodoPago={}", 
                    pago.getPeriodoMes(), pago.getPeriodoAnio(), pago.getFechaVencimiento(),
                    pago.getEstado(), pago.getMetodoPago());
        
        return pagoRepository.save(pago);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Pago> obtenerPagoPorId(Long id) {
        return pagoRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Pago> obtenerPagosPorUsuario(Usuario usuario) {
        return pagoRepository.findByUsuario(usuario);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Pago> obtenerPagosPorUsuarioOrdenados(Usuario usuario) {
        return pagoRepository.findByUsuarioOrderByPeriodoAnioDescPeriodoMesDesc(usuario);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Pago> obtenerPagoPorUsuarioYPeriodo(Usuario usuario, Integer mes, Integer anio) {
        return pagoRepository.findByUsuarioAndPeriodoMesAndPeriodoAnio(usuario, mes, anio);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Pago> obtenerPagosPorEstado(Pago.EstadoPago estado) {
        return pagoRepository.findByEstado(estado);
    }
    
    @Override
    public void generarPagosParaNuevoUsuario(Usuario usuario) {
        logger.info("Generando pagos automáticos para nuevo usuario: {} (ID: {})", 
                   usuario.getNombreCompleto(), usuario.getId());
        
        // Verificar si está habilitada la generación automática
        if (!pagoConfig.isGenerarPagosAutomaticos()) {
            logger.debug("Generación automática de pagos deshabilitada en configuración");
            return;
        }
        
        // Verificar si el tipo de usuario debe tener pagos
        if (!debeGenerarPagos(usuario)) {
            logger.debug("El usuario {} no requiere pagos según configuración", usuario.getTipo());
            return;
        }
        
        // Verificar que el usuario esté activo
        if (usuario.getEstado() != Usuario.EstadoUsuario.Activo) {
            logger.debug("No se generan pagos para usuario inactivo: {}", usuario.getNombreCompleto());
            return;
        }
        
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaRegistro = null;
        
        // Obtener la fecha de registro del usuario
        if (usuario.getCreatedAt() != null) {
            fechaRegistro = usuario.getCreatedAt().toLocalDate();
        } else if (usuario.getFechaRegistro() != null) {
            fechaRegistro = usuario.getFechaRegistro();
        } else {
            logger.warn("Usuario {} no tiene fecha de registro, usando fecha actual", usuario.getNombreCompleto());
            fechaRegistro = fechaActual;
        }
        
        int pagosGenerados = 0;
        
        logger.info("Generando pagos desde {} hasta {} para usuario: {}", 
                   fechaRegistro, fechaActual, usuario.getNombreCompleto());
        
        // Generar pagos desde el mes de registro hasta el mes actual
        LocalDate fechaIteracion = fechaRegistro.withDayOfMonth(1); // Inicio del mes de registro
        
        while (!fechaIteracion.isAfter(fechaActual)) {
            pagosGenerados += generarPagoSiNoExiste(usuario, fechaIteracion, 
                String.format("mes %02d/%d", fechaIteracion.getMonthValue(), fechaIteracion.getYear()));
            fechaIteracion = fechaIteracion.plusMonths(1);
        }
        
        // Generar pagos para los meses siguientes según configuración
        for (int i = 1; i <= pagoConfig.getMesesAdelante(); i++) {
            LocalDate fechaFutura = fechaActual.plusMonths(i);
            pagosGenerados += generarPagoSiNoExiste(usuario, fechaFutura, "mes +" + i);
        }
        
        logger.info("Se generaron {} pagos para el usuario: {} (Total: {} pagos)", 
                   pagosGenerados, usuario.getNombreCompleto(), pagosGenerados);
    }
    
    /**
     * Verifica si se debe generar pagos para el tipo de usuario
     */
    private boolean debeGenerarPagos(Usuario usuario) {
        return Arrays.stream(pagoConfig.getTiposUsuarioConPagos())
                     .anyMatch(tipo -> tipo.equalsIgnoreCase(usuario.getTipo().name()));
    }
    
    /**
     * Genera un pago si no existe para el período especificado
     */
    private int generarPagoSiNoExiste(Usuario usuario, LocalDate fecha, String descripcion) {
        if (!pagoRepository.existsByUsuarioAndPeriodoMesAndPeriodoAnio(
                usuario, fecha.getMonthValue(), fecha.getYear())) {
            
            Pago nuevoPago = crearPago(usuario, fecha);
            pagoRepository.save(nuevoPago);
            
            logger.debug("Pago creado para {} - Usuario: {} - Período: {}/{}", 
                        descripcion, usuario.getNombreCompleto(), fecha.getMonthValue(), fecha.getYear());
            return 1;
        }
        return 0;
    }
    
    /**
     * Crea un nuevo pago con la configuración por defecto
     */
    private Pago crearPago(Usuario usuario, LocalDate fecha) {
        logger.debug("💰 Creando pago con configuración: cuotaMensual={}, diaVencimiento={}", 
                    pagoConfig.getCuotaMensual(), pagoConfig.getDiaVencimiento());
        
        Pago pago = new Pago();
        pago.setUsuario(usuario);
        pago.setPeriodoMes(fecha.getMonthValue());
        pago.setPeriodoAnio(fecha.getYear());
        pago.setMonto(pagoConfig.getCuotaMensual());
        pago.setFechaVencimiento(calcularFechaVencimiento(fecha));
        pago.setEstado(Pago.EstadoPago.pendiente);
        pago.setFechaRegistro(LocalDate.now());
        
        logger.debug("📋 Pago creado: monto={}, vencimiento={}, periodo={}/{}",
                    pago.getMonto(), pago.getFechaVencimiento(), 
                    pago.getPeriodoMes(), pago.getPeriodoAnio());
        
        return pago;
    }
    
    /**
     * Calcula la fecha de vencimiento según configuración
     */
    private LocalDate calcularFechaVencimiento(LocalDate fecha) {
        if (pagoConfig.getDiaVencimiento() != null) {
            try {
                return fecha.withDayOfMonth(pagoConfig.getDiaVencimiento());
            } catch (Exception e) {
                logger.warn("Día de vencimiento inválido ({}), usando último día del mes", 
                           pagoConfig.getDiaVencimiento());
            }
        }
        // Por defecto, último día del mes
        return fecha.withDayOfMonth(fecha.lengthOfMonth());
    }
    
    @Override
    @Scheduled(cron = "0 0 6 1 * ?") // Se ejecuta el día 1 de cada mes a las 6:00 AM
    public void generarPagosMensualesAutomaticos() {
        logger.info("Iniciando generación automática de pagos mensuales");
        
        if (!pagoConfig.isGenerarPagosAutomaticos()) {
            logger.info("Generación automática de pagos deshabilitada en configuración");
            return;
        }
        
        LocalDate fechaActual = LocalDate.now();
        List<Usuario> usuariosActivos = usuarioRepository.findByEstado(Usuario.EstadoUsuario.Activo);
        
        logger.info("Procesando {} usuarios activos para generación de pagos", usuariosActivos.size());
        
        int pagosGenerados = 0;
        int usuariosProcesados = 0;
        
        for (Usuario usuario : usuariosActivos) {
            try {
                // Verificar si el tipo de usuario debe tener pagos
                if (!debeGenerarPagos(usuario)) {
                    continue;
                }
                
                usuariosProcesados++;
                
                // Verificar si ya existe un pago para este mes
                boolean existePago = pagoRepository.existsByUsuarioAndPeriodoMesAndPeriodoAnio(
                        usuario, fechaActual.getMonthValue(), fechaActual.getYear());
                
                if (!existePago) {
                    Pago nuevoPago = crearPago(usuario, fechaActual);
                    pagoRepository.save(nuevoPago);
                    pagosGenerados++;
                    
                    logger.debug("Pago generado para usuario: {} - Período: {}/{}", 
                                usuario.getNombreCompleto(), fechaActual.getMonthValue(), fechaActual.getYear());
                }
                
            } catch (Exception e) {
                logger.error("Error generando pago para usuario {}: {}", 
                           usuario.getNombreCompleto(), e.getMessage());
            }
        }
        
        logger.info("Generación automática completada: {} pagos generados para {} usuarios", 
                   pagosGenerados, usuariosProcesados);
    }
    
    @Override
    @Scheduled(cron = "0 0 12 * * ?") // Se ejecuta todos los días a las 12:00 PM
    public void actualizarPagosEnAtraso() {
        logger.info("🔍 Iniciando verificación de pagos en atraso");
        
        LocalDate fechaActual = LocalDate.now();
        logger.debug("Fecha actual para verificación: {}", fechaActual);
        
        // Buscar pagos pendientes cuya fecha de vencimiento ya pasó
        List<Pago> pagosPendientes = pagoRepository.findPagosPendientesParaAtraso(fechaActual);
        
        if (pagosPendientes.isEmpty()) {
            logger.debug("✅ No hay pagos pendientes vencidos para marcar como atrasados");
            return;
        }
        
        logger.info("📋 Encontrados {} pagos pendientes vencidos para actualizar", pagosPendientes.size());
        
        int pagosActualizados = 0;
        
        for (Pago pago : pagosPendientes) {
            try {
                // Verificación adicional: solo marcar como atrasado si la fecha actual es posterior al vencimiento
                if (pago.getFechaVencimiento() != null && fechaActual.isAfter(pago.getFechaVencimiento())) {
                    logger.debug("⚠️ Marcando pago {} como atrasado - Vencimiento: {}, Fecha actual: {}", 
                               pago.getId(), pago.getFechaVencimiento(), fechaActual);
                    
                    pago.marcarComoEnAtraso();
                    pagoRepository.save(pago);
                    pagosActualizados++;
                    
                    logger.debug("✅ Pago {} marcado como atrasado - Usuario: {} - Período: {}/{}", 
                               pago.getId(), pago.getUsuarioNombre(), pago.getPeriodoMes(), pago.getPeriodoAnio());
                } else {
                    logger.warn("⚠️ Pago {} no se marcó como atrasado - Fecha vencimiento: {}, Fecha actual: {}", 
                              pago.getId(), pago.getFechaVencimiento(), fechaActual);
                }
                
            } catch (Exception e) {
                logger.error("❌ Error actualizando pago {} a estado atrasado: {}", 
                           pago.getId(), e.getMessage());
            }
        }
        
        logger.info("✅ Actualización de pagos en atraso completada: {} de {} pagos actualizados", 
                   pagosActualizados, pagosPendientes.size());
    }
    
    @Override
    public Pago marcarComoPagado(Long pagoId, Double monto, String metodoPago) {
        Optional<Pago> pagoOpt = pagoRepository.findById(pagoId);
        if (pagoOpt.isPresent()) {
            Pago pago = pagoOpt.get();
            pago.marcarComoPagado(metodoPago);
            return pagoRepository.save(pago);
        }
        throw new RuntimeException("Pago no encontrado con ID: " + pagoId);
    }
    
    @Override
    public Pago marcarComoPagadoConComprobante(Long pagoId, Double monto, String metodoPago, String rutaComprobante) {
        Optional<Pago> pagoOpt = pagoRepository.findById(pagoId);
        if (pagoOpt.isPresent()) {
            Pago pago = pagoOpt.get();
            pago.marcarComoPagado(metodoPago);
            
            // Asignar el comprobante si se proporcionó
            if (rutaComprobante != null && !rutaComprobante.trim().isEmpty()) {
                pago.setComprobante(rutaComprobante);
                logger.info("Comprobante asignado al pago ID {}: {}", pagoId, rutaComprobante);
            }
            
            return pagoRepository.save(pago);
        }
        throw new RuntimeException("Pago no encontrado con ID: " + pagoId);
    }
    
    @Override
    public Pago marcarComoPagadoCompleto(Long pagoId, Double monto, String metodoPago, String rutaComprobante, String observaciones) {
        Optional<Pago> pagoOpt = pagoRepository.findById(pagoId);
        if (pagoOpt.isPresent()) {
            Pago pago = pagoOpt.get();
            pago.marcarComoPagado(metodoPago);
            
            // Asignar el comprobante si se proporcionó
            if (rutaComprobante != null && !rutaComprobante.trim().isEmpty()) {
                pago.setComprobante(rutaComprobante);
                logger.info("Comprobante asignado al pago ID {}: {}", pagoId, rutaComprobante);
            }
            
            // Agregar observaciones si se proporcionaron
            if (observaciones != null && !observaciones.trim().isEmpty()) {
                String observacionesExistentes = pago.getObservaciones();
                if (observacionesExistentes != null && !observacionesExistentes.trim().isEmpty()) {
                    pago.setObservaciones(observacionesExistentes + " | " + observaciones.trim());
                } else {
                    pago.setObservaciones(observaciones.trim());
                }
                logger.info("Observaciones agregadas al pago ID {}: {}", pagoId, observaciones);
            }
            
            return pagoRepository.save(pago);
        }
        throw new RuntimeException("Pago no encontrado con ID: " + pagoId);
    }
    
    @Override
    public Pago marcarComoRechazado(Long pagoId, String motivoRechazo) {
        Optional<Pago> pagoOpt = pagoRepository.findById(pagoId);
        if (pagoOpt.isPresent()) {
            Pago pago = pagoOpt.get();
            pago.marcarComoRechazado(motivoRechazo);
            return pagoRepository.save(pago);
        }
        throw new RuntimeException("Pago no encontrado con ID: " + pagoId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean tieneDeudas(Usuario usuario) {
        long pagosEnAtraso = pagoRepository.countByUsuarioAndEstado(usuario, Pago.EstadoPago.atraso);
        long pagosPendientes = pagoRepository.countByUsuarioAndEstado(usuario, Pago.EstadoPago.pendiente);
        return (pagosEnAtraso + pagosPendientes) > 0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public long contarPagosEnAtraso(Usuario usuario) {
        return pagoRepository.countByUsuarioAndEstado(usuario, Pago.EstadoPago.atraso);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Pago> obtenerPagosPorPeriodo(Integer mes, Integer anio) {
        return pagoRepository.findByPeriodoMesAndPeriodoAnio(mes, anio);
    }
    
    @Override
    public boolean eliminarPago(Long pagoId) {
        Optional<Pago> pagoOpt = pagoRepository.findById(pagoId);
        if (pagoOpt.isPresent()) {
            Pago pago = pagoOpt.get();
            if (pago.getEstado() == Pago.EstadoPago.pendiente) {
                pagoRepository.delete(pago);
                return true;
            }
        }
        return false;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Pago> obtenerTodosLosPagos() {
        return pagoRepository.findAll();
    }
    
    @Override
    public Pago actualizarPago(Pago pago) {
        logger.debug("🔄 Actualizando pago: id={}, estado={}", pago.getId(), pago.getEstado());
        
        if (pago.getId() == null) {
            throw new IllegalArgumentException("El ID del pago es obligatorio para actualizar");
        }
        
        // Verificar que el pago existe
        Optional<Pago> pagoExistente = pagoRepository.findById(pago.getId());
        if (!pagoExistente.isPresent()) {
            throw new IllegalArgumentException("Pago no encontrado con ID: " + pago.getId());
        }
        
        // Establecer fecha de actualización
        pago.setUpdatedAt(java.time.LocalDateTime.now());
        
        Pago pagoActualizado = pagoRepository.save(pago);
        logger.debug("✅ Pago actualizado exitosamente: id={}, estado={}", pagoActualizado.getId(), pagoActualizado.getEstado());
        
        return pagoActualizado;
    }
    
    @Override
    public Pago actualizarComprobante(Long pagoId, String rutaComprobante) {
        Optional<Pago> pagoOpt = pagoRepository.findById(pagoId);
        if (pagoOpt.isPresent()) {
            Pago pago = pagoOpt.get();
            pago.setComprobante(rutaComprobante);
            logger.info("Actualizando comprobante del pago ID {}: {}", pagoId, rutaComprobante);
            return pagoRepository.save(pago);
        }
        throw new RuntimeException("Pago no encontrado con ID: " + pagoId);
    }
}