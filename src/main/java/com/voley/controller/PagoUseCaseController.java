package com.voley.controller;

import com.voley.application.pagos.*;
import com.voley.domain.Pago;
import com.voley.domain.Usuario;
import com.voley.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador REST que utiliza casos de uso para pagos (Clean Architecture)
 * Demuestra la separación de responsabilidades entre controlador y lógica de negocio
 */
@RestController
@RequestMapping("/api/v2/pagos")
@CrossOrigin(origins = "*")
public class PagoUseCaseController {
    
    private static final Logger logger = LoggerFactory.getLogger(PagoUseCaseController.class);
    
    private final ObtenerTodosLosPagosUseCase obtenerTodosLosPagosUseCase;
    private final CrearPagoUseCase crearPagoUseCase;
    private final ObtenerPagoPorIdUseCase obtenerPagoPorIdUseCase;
    private final ObtenerPagosPorUsuarioUseCase obtenerPagosPorUsuarioUseCase;
    private final ProcesarPagoUseCase procesarPagoUseCase;
    private final VerificarPagosEnAtrasoUseCase verificarPagosEnAtrasoUseCase;
    private final ObtenerPagosPorEstadoUseCase obtenerPagosPorEstadoUseCase;
    private final UsuarioService usuarioService; // Para obtener usuarios por ID
    
    @Autowired
    public PagoUseCaseController(
            ObtenerTodosLosPagosUseCase obtenerTodosLosPagosUseCase,
            CrearPagoUseCase crearPagoUseCase,
            ObtenerPagoPorIdUseCase obtenerPagoPorIdUseCase,
            ObtenerPagosPorUsuarioUseCase obtenerPagosPorUsuarioUseCase,
            ProcesarPagoUseCase procesarPagoUseCase,
            VerificarPagosEnAtrasoUseCase verificarPagosEnAtrasoUseCase,
            ObtenerPagosPorEstadoUseCase obtenerPagosPorEstadoUseCase,
            UsuarioService usuarioService) {
        this.obtenerTodosLosPagosUseCase = obtenerTodosLosPagosUseCase;
        this.crearPagoUseCase = crearPagoUseCase;
        this.obtenerPagoPorIdUseCase = obtenerPagoPorIdUseCase;
        this.obtenerPagosPorUsuarioUseCase = obtenerPagosPorUsuarioUseCase;
        this.procesarPagoUseCase = procesarPagoUseCase;
        this.verificarPagosEnAtrasoUseCase = verificarPagosEnAtrasoUseCase;
        this.obtenerPagosPorEstadoUseCase = obtenerPagosPorEstadoUseCase;
        this.usuarioService = usuarioService;
    }
    
    /**
     * Obtener todos los pagos usando caso de uso
     * GET /api/v2/pagos
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodosLosPagos() {
        try {
            logger.info("Obteniendo todos los pagos usando caso de uso");
            List<Pago> pagos = obtenerTodosLosPagosUseCase.ejecutar();
            
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
            logger.error("Error al obtener pagos: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al obtener los pagos");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Crear nuevo pago usando caso de uso
     * POST /api/v2/pagos
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearPago(@RequestBody Pago pago) {
        try {
            logger.info("Creando nuevo pago usando caso de uso");
            Pago pagoCreado = crearPagoUseCase.ejecutar(pago);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pago creado exitosamente");
            response.put("data", convertirPagoAMap(pagoCreado));
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Error de validación al crear pago: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error de validación");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            logger.error("Error interno al crear pago: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Obtener pagos por usuario usando caso de uso
     * GET /api/v2/pagos/usuario/{usuarioId}
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Map<String, Object>> obtenerPagosPorUsuario(@PathVariable Long usuarioId) {
        try {
            logger.info("Obteniendo pagos para usuario ID: {}", usuarioId);
            
            // Obtener el usuario completo
            Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(usuarioId);
            if (!usuarioOpt.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
            List<Pago> pagos = obtenerPagosPorUsuarioUseCase.ejecutar(usuarioOpt.get());
            
            List<Map<String, Object>> pagosList = pagos.stream()
                    .map(this::convertirPagoAMap)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pagos del usuario obtenidos exitosamente");
            response.put("usuario", usuarioOpt.get().getNombreCompleto());
            response.put("total", pagosList.size());
            response.put("data", pagosList);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Error de validación: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    
    /**
     * Procesar pago usando caso de uso
     * PUT /api/v2/pagos/{id}/pagar
     */
    @PutMapping("/{id}/pagar")
    public ResponseEntity<Map<String, Object>> procesarPago(
            @PathVariable Long id,
            @RequestParam Double monto,
            @RequestParam String metodoPago) {
        try {
            logger.info("Procesando pago ID: {} con monto: {} y método: {}", id, monto, metodoPago);
            
            Pago pagoProcesado = procesarPagoUseCase.ejecutar(id, monto, metodoPago);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pago procesado exitosamente");
            response.put("data", convertirPagoAMap(pagoProcesado));
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Error de validación al procesar pago: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            logger.error("Error al procesar pago: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Verificar pagos en atraso usando caso de uso
     * PUT /api/v2/pagos/verificar-atrasos
     */
    @PutMapping("/verificar-atrasos")
    public ResponseEntity<Map<String, Object>> verificarPagosEnAtraso() {
        try {
            logger.info("Ejecutando verificación manual de pagos en atraso");
            verificarPagosEnAtrasoUseCase.ejecutar();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Verificación de pagos en atraso completada");
            response.put("timestamp", LocalDate.now().toString());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error en verificación de atrasos: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error en la verificación de atrasos");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Obtener pagos por estado usando caso de uso
     * GET /api/v2/pagos/estado/{estado}
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<Map<String, Object>> obtenerPagosPorEstado(@PathVariable String estado) {
        try {
            logger.info("Obteniendo pagos por estado: {}", estado);
            
            Pago.EstadoPago estadoPago = Pago.EstadoPago.valueOf(estado.toLowerCase());
            List<Pago> pagos = obtenerPagosPorEstadoUseCase.ejecutar(estadoPago);
            
            List<Map<String, Object>> pagosList = pagos.stream()
                    .map(this::convertirPagoAMap)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pagos por estado obtenidos exitosamente");
            response.put("estado", estado);
            response.put("total", pagosList.size());
            response.put("data", pagosList);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Estado inválido: {}", estado);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Estado de pago inválido: " + estado);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    
    /**
     * Convierte un objeto Pago a un Map para la respuesta JSON
     */
    private Map<String, Object> convertirPagoAMap(Pago pago) {
        Map<String, Object> pagoMap = new HashMap<>();
        pagoMap.put("id", pago.getId());
        pagoMap.put("usuarioId", pago.getUsuario() != null ? pago.getUsuario().getId() : null);
        pagoMap.put("usuarioNombre", pago.getUsuarioNombre());
        pagoMap.put("periodo", pago.getPeriodoMes() + "/" + pago.getPeriodoAnio());
        pagoMap.put("monto", pago.getMonto());
        pagoMap.put("estado", pago.getEstado().toString());
        pagoMap.put("fechaRegistro", pago.getFechaRegistro());
        pagoMap.put("fechaVencimiento", pago.getFechaVencimiento());
        pagoMap.put("fechaPago", pago.getFechaPago());
        pagoMap.put("observaciones", pago.getObservaciones());
        pagoMap.put("comprobante", pago.getComprobante());
        return pagoMap;
    }
}