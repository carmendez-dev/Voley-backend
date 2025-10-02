package com.voley.controller;

import com.voley.application.pagos.EliminarPagoUseCase;
import com.voley.domain.Pago;
import com.voley.domain.Usuario;
import com.voley.service.PagoService;
import com.voley.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
public class PagoController {
    
    private static final Logger logger = LoggerFactory.getLogger(PagoController.class);
    
    private final PagoService pagoService;
    private final UsuarioService usuarioService;
    private final EliminarPagoUseCase eliminarPagoUseCase;
    
    @Autowired
    public PagoController(PagoService pagoService, UsuarioService usuarioService, EliminarPagoUseCase eliminarPagoUseCase) {
        this.pagoService = pagoService;
        this.usuarioService = usuarioService;
        this.eliminarPagoUseCase = eliminarPagoUseCase;
    }
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodosLosPagos() {
        try {
            logger.info("Obteniendo todos los pagos");
            List<Pago> pagos = pagoService.obtenerTodosLosPagos();
            
            List<Map<String, Object>> pagosList = pagos.stream()
                    .map(this::convertirPagoAMap)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pagos obtenidos exitosamente");
            response.put("timestamp", LocalDate.now().toString());
            response.put("total", pagosList.size());
            response.put("data", pagosList);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error al obtener pagos: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al obtener los pagos: " + e.getMessage());
            errorResponse.put("timestamp", LocalDate.now().toString());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPagoPorId(@PathVariable Long id) {
        try {
            logger.info("Obteniendo pago con ID: {}", id);
            Optional<Pago> pagoOpt = pagoService.obtenerPagoPorId(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("timestamp", LocalDate.now().toString());
            
            if (pagoOpt.isPresent()) {
                response.put("success", true);
                response.put("message", "Pago encontrado exitosamente");
                response.put("data", convertirPagoAMap(pagoOpt.get()));
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Pago no encontrado");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error al obtener pago por ID {}: {}", id, e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al obtener el pago: " + e.getMessage());
            errorResponse.put("timestamp", LocalDate.now().toString());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Map<String, Object>> obtenerPagosPorUsuario(@PathVariable Long usuarioId) {
        try {
            logger.info("Obteniendo pagos para usuario ID: {}", usuarioId);
            
            Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(usuarioId);
            if (!usuarioOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                response.put("timestamp", LocalDate.now().toString());
                return ResponseEntity.notFound().build();
            }
            
            Usuario usuario = usuarioOpt.get();
            List<Pago> pagos = pagoService.obtenerPagosPorUsuario(usuario);
            
            List<Map<String, Object>> pagosList = pagos.stream()
                    .map(this::convertirPagoAMap)
                    .collect(Collectors.toList());
            
            Map<String, Object> usuarioInfo = new HashMap<>();
            usuarioInfo.put("id", usuario.getId());
            usuarioInfo.put("nombreCompleto", usuario.getNombres() + " " + usuario.getApellidos());
            usuarioInfo.put("email", usuario.getEmail());
            usuarioInfo.put("celular", usuario.getCelular());
            usuarioInfo.put("estado", usuario.getEstado().toString());
            usuarioInfo.put("tipo", usuario.getTipo().toString());
            
            Map<String, Object> estadisticas = new HashMap<>();
            estadisticas.put("totalPagos", pagos.size());
            estadisticas.put("montoTotal", pagos.stream().mapToDouble(p -> p.getMonto().doubleValue()).sum());
            estadisticas.put("pagosPendientes", pagos.stream().filter(p -> "pendiente".equals(p.getEstado().toString())).count());
            estadisticas.put("pagosPagados", pagos.stream().filter(p -> "pagado".equals(p.getEstado().toString())).count());
            estadisticas.put("pagosVencidos", pagos.stream().filter(p -> "atraso".equals(p.getEstado().toString())).count());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pagos del usuario obtenidos exitosamente");
            response.put("timestamp", LocalDate.now().toString());
            response.put("usuario", usuarioInfo);
            response.put("estadisticas", estadisticas);
            response.put("pagos", pagosList);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error al obtener pagos por usuario {}: {}", usuarioId, e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al obtener pagos del usuario: " + e.getMessage());
            errorResponse.put("timestamp", LocalDate.now().toString());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearPago(@RequestBody Map<String, Object> pagoData) {
        try {
            logger.info("Creando nuevo pago: {}", pagoData);
            
            if (!pagoData.containsKey("usuario_id") || pagoData.get("usuario_id") == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "El usuario_id es obligatorio",
                    "timestamp", LocalDate.now().toString()
                ));
            }
            
            if (!pagoData.containsKey("monto") || pagoData.get("monto") == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "El monto es obligatorio",
                    "timestamp", LocalDate.now().toString()
                ));
            }
            
            Long usuarioId = Long.valueOf(pagoData.get("usuario_id").toString());
            Double monto = Double.valueOf(pagoData.get("monto").toString());
            Integer periodoMes = pagoData.containsKey("periodo_mes") ? 
                Integer.valueOf(pagoData.get("periodo_mes").toString()) : LocalDate.now().getMonthValue();
            Integer periodoAnio = pagoData.containsKey("periodo_anio") ? 
                Integer.valueOf(pagoData.get("periodo_anio").toString()) : LocalDate.now().getYear();
            String metodoPago = pagoData.getOrDefault("metodo_pago", "transferencia").toString();
            
            // Buscar el usuario
            Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(usuarioId);
            if (!usuarioOpt.isPresent()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Usuario no encontrado",
                    "timestamp", LocalDate.now().toString()
                ));
            }
            
            // Crear el pago
            Pago nuevoPago = new Pago();
            nuevoPago.setUsuario(usuarioOpt.get());
            nuevoPago.setMonto(BigDecimal.valueOf(monto));
            nuevoPago.setPeriodoMes(periodoMes);
            nuevoPago.setPeriodoAnio(periodoAnio);
            nuevoPago.setMetodoPago(metodoPago);
            nuevoPago.setEstado(Pago.EstadoPago.pendiente);
            
            if (pagoData.containsKey("observaciones")) {
                nuevoPago.setObservaciones(pagoData.get("observaciones").toString());
            }
            
            Pago pagoCreado = pagoService.crearPago(nuevoPago);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pago creado exitosamente");
            response.put("timestamp", LocalDate.now().toString());
            response.put("data", convertirPagoAMap(pagoCreado));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error al crear pago: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al crear el pago: " + e.getMessage());
            errorResponse.put("timestamp", LocalDate.now().toString());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @PutMapping("/{id}/pagar")
    public ResponseEntity<Map<String, Object>> marcarComoPagado(@PathVariable Long id, @RequestBody(required = false) Map<String, Object> pagoData) {
        try {
            logger.info("Marcando pago {} como pagado", id);
            Optional<Pago> pagoOpt = pagoService.obtenerPagoPorId(id);
            
            if (!pagoOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            Double monto = pagoData != null && pagoData.containsKey("monto") ? 
                Double.valueOf(pagoData.get("monto").toString()) : pagoOpt.get().getMonto().doubleValue();
            String metodoPago = pagoData != null && pagoData.containsKey("metodo_pago") ? 
                pagoData.get("metodo_pago").toString() : "transferencia";
            
            Pago pagoActualizado = pagoService.marcarComoPagado(id, monto, metodoPago);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pago marcado como pagado exitosamente");
            response.put("timestamp", LocalDate.now().toString());
            response.put("data", convertirPagoAMap(pagoActualizado));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error al marcar pago como pagado {}: {}", id, e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al marcar el pago como pagado: " + e.getMessage());
            errorResponse.put("timestamp", LocalDate.now().toString());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        try {
            logger.info("Obteniendo estadísticas de pagos");
            List<Pago> todosPagos = pagoService.obtenerTodosLosPagos();
            
            Map<String, Object> estadisticas = new HashMap<>();
            estadisticas.put("totalPagos", todosPagos.size());
            estadisticas.put("montoTotal", todosPagos.stream().mapToDouble(p -> p.getMonto().doubleValue()).sum());
            estadisticas.put("pagosPendientes", todosPagos.stream().filter(p -> "pendiente".equals(p.getEstado().toString())).count());
            estadisticas.put("pagosPagados", todosPagos.stream().filter(p -> "pagado".equals(p.getEstado().toString())).count());
            estadisticas.put("pagosVencidos", todosPagos.stream().filter(p -> "atraso".equals(p.getEstado().toString())).count());
            
            Map<String, Long> pagosPorMes = todosPagos.stream()
                .collect(Collectors.groupingBy(
                    pago -> pago.getPeriodoAnio() + "-" + String.format("%02d", pago.getPeriodoMes()),
                    Collectors.counting()
                ));
            estadisticas.put("pagosPorMes", pagosPorMes);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Estadísticas obtenidas exitosamente");
            response.put("timestamp", LocalDate.now().toString());
            response.put("data", estadisticas);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error al obtener estadísticas: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al obtener estadísticas: " + e.getMessage());
            errorResponse.put("timestamp", LocalDate.now().toString());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    /**
     * Verificar y actualizar pagos en atraso manualmente
     */
    @PutMapping("/verificar-atrasos")
    public ResponseEntity<Map<String, Object>> verificarPagosEnAtraso() {
        try {
            logger.info("Verificando pagos en atraso manualmente");
            pagoService.actualizarPagosEnAtraso();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Verificación de pagos en atraso completada exitosamente");
            response.put("timestamp", LocalDate.now().toString());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error al verificar pagos en atraso: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al verificar pagos en atraso: " + e.getMessage());
            errorResponse.put("timestamp", LocalDate.now().toString());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarPago(@PathVariable Long id) {
        try {
            logger.info("Eliminando pago con ID: {}", id);
            
            boolean eliminado = eliminarPagoUseCase.ejecutar(id);
            
            Map<String, Object> response = new HashMap<>();
            
            if (eliminado) {
                response.put("success", true);
                response.put("message", "Pago eliminado exitosamente");
                response.put("timestamp", LocalDate.now().toString());
                response.put("pagoId", id);
                
                logger.info("Pago eliminado exitosamente: {}", id);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "No se pudo eliminar el pago");
                response.put("timestamp", LocalDate.now().toString());
                response.put("pagoId", id);
                
                logger.warn("No se pudo eliminar el pago: {}", id);
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (IllegalArgumentException e) {
            logger.error("Error de validación al eliminar pago {}: {}", id, e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("timestamp", LocalDate.now().toString());
            response.put("pagoId", id);
            
            return ResponseEntity.badRequest().body(response);
            
        } catch (IllegalStateException e) {
            logger.error("Error de estado al eliminar pago {}: {}", id, e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("timestamp", LocalDate.now().toString());
            response.put("pagoId", id);
            
            return ResponseEntity.status(409).body(response); // 409 Conflict
            
        } catch (Exception e) {
            logger.error("Error interno al eliminar pago {}: {}", id, e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error interno del servidor");
            response.put("timestamp", LocalDate.now().toString());
            response.put("pagoId", id);
            
            return ResponseEntity.status(500).body(response);
        }
    }
    
    private Map<String, Object> convertirPagoAMap(Pago pago) {
        Map<String, Object> pagoMap = new HashMap<>();
        pagoMap.put("id", pago.getId());
        pagoMap.put("monto", pago.getMonto());
        pagoMap.put("periodoMes", pago.getPeriodoMes());
        pagoMap.put("periodoAnio", pago.getPeriodoAnio());
        pagoMap.put("estado", pago.getEstado().toString());
        pagoMap.put("fechaRegistro", pago.getFechaRegistro() != null ? pago.getFechaRegistro().toString() : null);
        pagoMap.put("fechaVencimiento", pago.getFechaVencimiento() != null ? pago.getFechaVencimiento().toString() : null);
        pagoMap.put("fechaPago", pago.getFechaPago() != null ? pago.getFechaPago().toString() : null);
        pagoMap.put("metodoPago", pago.getMetodoPago());
        pagoMap.put("observaciones", pago.getObservaciones());
        pagoMap.put("usuarioNombre", pago.getUsuarioNombre());
        
        if (pago.getUsuario() != null) {
            Map<String, Object> usuario = new HashMap<>();
            usuario.put("id", pago.getUsuario().getId());
            usuario.put("nombreCompleto", pago.getUsuario().getNombres() + " " + pago.getUsuario().getApellidos());
            usuario.put("email", pago.getUsuario().getEmail());
            usuario.put("estado", pago.getUsuario().getEstado().toString());
            usuario.put("tipo", pago.getUsuario().getTipo().toString());
            pagoMap.put("usuario", usuario);
        }
        
        return pagoMap;
    }
}