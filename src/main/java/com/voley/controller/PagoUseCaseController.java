package com.voley.controller;

import com.voley.application.pagos.*;
import com.voley.domain.Pago;
import com.voley.domain.Usuario;
import com.voley.service.UsuarioService;
import com.voley.service.FileUploadService;
import com.voley.service.PagoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador REST que utiliza casos de uso para pagos (Clean Architecture)
 * Demuestra la separación de responsabilidades entre controlador y lógica de negocio
 */
@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(originPatterns = {"http://localhost:*", "http://127.0.0.1:*"}, 
             allowedHeaders = {"Origin", "Content-Type", "Accept", "Authorization", 
                              "Access-Control-Request-Method", "Access-Control-Request-Headers"},
             allowCredentials = "true")
public class PagoUseCaseController {
    
    private static final Logger logger = LoggerFactory.getLogger(PagoUseCaseController.class);
    
    private final ObtenerTodosLosPagosUseCase obtenerTodosLosPagosUseCase;
    private final CrearPagoUseCase crearPagoUseCase;
    private final ObtenerPagoPorIdUseCase obtenerPagoPorIdUseCase;
    private final ObtenerPagosPorUsuarioUseCase obtenerPagosPorUsuarioUseCase;
    private final ProcesarPagoUseCase procesarPagoUseCase;
    private final VerificarPagosEnAtrasoUseCase verificarPagosEnAtrasoUseCase;
    private final ObtenerPagosPorEstadoUseCase obtenerPagosPorEstadoUseCase;
    private final EliminarPagoUseCase eliminarPagoUseCase;
    private final ActualizarEstadoPagoUseCase actualizarEstadoPagoUseCase;
    private final UsuarioService usuarioService; // Para obtener usuarios por ID
    private final FileUploadService fileUploadService; // Para manejo de archivos
    private final PagoService pagoService; // Para operaciones adicionales con pagos
    
    @Autowired
    public PagoUseCaseController(
            ObtenerTodosLosPagosUseCase obtenerTodosLosPagosUseCase,
            CrearPagoUseCase crearPagoUseCase,
            ObtenerPagoPorIdUseCase obtenerPagoPorIdUseCase,
            ObtenerPagosPorUsuarioUseCase obtenerPagosPorUsuarioUseCase,
            ProcesarPagoUseCase procesarPagoUseCase,
            VerificarPagosEnAtrasoUseCase verificarPagosEnAtrasoUseCase,
            ObtenerPagosPorEstadoUseCase obtenerPagosPorEstadoUseCase,
            EliminarPagoUseCase eliminarPagoUseCase,
            ActualizarEstadoPagoUseCase actualizarEstadoPagoUseCase,
            UsuarioService usuarioService,
            FileUploadService fileUploadService,
            PagoService pagoService) {
        this.obtenerTodosLosPagosUseCase = obtenerTodosLosPagosUseCase;
        this.crearPagoUseCase = crearPagoUseCase;
        this.obtenerPagoPorIdUseCase = obtenerPagoPorIdUseCase;
        this.obtenerPagosPorUsuarioUseCase = obtenerPagosPorUsuarioUseCase;
        this.procesarPagoUseCase = procesarPagoUseCase;
        this.verificarPagosEnAtrasoUseCase = verificarPagosEnAtrasoUseCase;
        this.obtenerPagosPorEstadoUseCase = obtenerPagosPorEstadoUseCase;
        this.eliminarPagoUseCase = eliminarPagoUseCase;
        this.actualizarEstadoPagoUseCase = actualizarEstadoPagoUseCase;
        this.usuarioService = usuarioService;
        this.fileUploadService = fileUploadService;
        this.pagoService = pagoService;
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
     * Crear nuevo pago usando caso de uso (JSON)
     * POST /api/pagos (Content-Type: application/json)
     */
    @PostMapping(consumes = "application/json")
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
     * Crear nuevo pago con comprobante usando caso de uso (Multipart)
     * POST /api/pagos (Content-Type: multipart/form-data)
     */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> crearPagoConComprobante(
            @RequestParam Long usuarioId,
            @RequestParam Integer periodoMes,
            @RequestParam Integer periodoAnio,
            @RequestParam Double monto,
            @RequestParam String metodoPago,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String observaciones,
            @RequestParam(required = false) MultipartFile comprobante) {
        try {
            logger.info("Creando nuevo pago con comprobante para usuario ID: {}", usuarioId);
            
            // Crear objeto Pago con los datos recibidos
            Pago pago = new Pago();
            
            // Obtener el usuario
            Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(usuarioId);
            if (!usuarioOpt.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Usuario no encontrado con ID: " + usuarioId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
            pago.setUsuario(usuarioOpt.get());
            pago.setPeriodoMes(periodoMes);
            pago.setPeriodoAnio(periodoAnio);
            pago.setMonto(java.math.BigDecimal.valueOf(monto));
            pago.setMetodoPago(metodoPago);
            
            // Establecer estado (por defecto pendiente si no se especifica)
            if (estado != null && !estado.trim().isEmpty()) {
                try {
                    pago.setEstado(Pago.EstadoPago.valueOf(estado.toLowerCase()));
                } catch (IllegalArgumentException e) {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("success", false);
                    errorResponse.put("message", "Estado inválido: " + estado);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
                }
            } else {
                pago.setEstado(Pago.EstadoPago.pendiente);
            }
            
            if (observaciones != null && !observaciones.trim().isEmpty()) {
                pago.setObservaciones(observaciones.trim());
            }
            
            // Crear el pago usando el caso de uso
            Pago pagoCreado = crearPagoUseCase.ejecutar(pago);
            
            // Si hay comprobante, subirlo y actualizar la ruta
            if (comprobante != null && !comprobante.isEmpty()) {
                try {
                    logger.info("Comprobante recibido: {} ({} bytes)", comprobante.getOriginalFilename(), comprobante.getSize());
                    
                    // Subir el archivo usando el FileUploadService
                    FileUploadService.FileUploadResult uploadResult = fileUploadService.subirComprobante(comprobante, pagoCreado.getId());
                    String rutaComprobante = uploadResult.getFilePath();
                    logger.info("Comprobante subido exitosamente: {}", rutaComprobante);
                    
                    // Actualizar el pago con la ruta del comprobante usando el servicio
                    pagoCreado = pagoService.actualizarComprobante(pagoCreado.getId(), rutaComprobante);
                    logger.info("Pago actualizado con comprobante - ID: {}, Ruta: {}", pagoCreado.getId(), rutaComprobante);
                    
                } catch (Exception e) {
                    logger.error("Error al subir comprobante para pago ID {}: {}", pagoCreado.getId(), e.getMessage());
                    // No fallar la creación del pago si el comprobante no se puede subir
                    // Solo log del error y continuar
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pago creado exitosamente" + (comprobante != null ? " con comprobante" : ""));
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
     * Obtener pago por ID usando caso de uso
     * GET /api/v2/pagos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPagoPorId(@PathVariable Long id) {
        try {
            logger.info("Obteniendo pago por ID: {} usando caso de uso", id);
            
            Optional<Pago> pagoOpt = obtenerPagoPorIdUseCase.ejecutar(id);
            
            if (pagoOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Pago encontrado exitosamente");
                response.put("data", convertirPagoAMap(pagoOpt.get()));
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Pago no encontrado");
                response.put("pagoId", id);
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
        } catch (IllegalArgumentException e) {
            logger.warn("Error de validación al obtener pago {}: {}", id, e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            logger.error("Error interno al obtener pago {}: ", id, e);
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
     * PUT /api/pagos/{id}/pagar
     */
    @PutMapping("/{id}/pagar")
    public ResponseEntity<Map<String, Object>> procesarPago(
            @PathVariable Long id,
            @RequestParam Double monto,
            @RequestParam String metodoPago,
            @RequestParam(required = false) String rutaComprobante) {
        return procesarPagoInterno(id, monto, metodoPago, rutaComprobante, null);
    }
    
    /**
     * Procesar pago y marcarlo como PAGADO (endpoint que espera el frontend)
     * POST /api/pagos/{id}/procesar
     */
    @PostMapping("/{id}/procesar")
    public ResponseEntity<Map<String, Object>> procesarPagoAlternativo(
            @PathVariable Long id,
            @RequestParam Double monto,
            @RequestParam String metodoPago,
            @RequestParam(required = false) String comprobante,
            @RequestParam(required = false) String observaciones) {
        return procesarPagoInterno(id, monto, metodoPago, comprobante, observaciones);
    }
    
    /**
     * Método interno común para procesar pagos
     */
    private ResponseEntity<Map<String, Object>> procesarPagoInterno(
            Long id, Double monto, String metodoPago, String rutaComprobante, String observaciones) {
        try {
            logger.info("Procesando pago ID: {} con monto: {} y método: {}", id, monto, metodoPago);
            if (rutaComprobante != null) {
                logger.info("Comprobante incluido: {}", rutaComprobante);
            }
            
            // Usar el método completo que guarda todo directamente en la base de datos
            Pago pagoProcesado;
            if ((rutaComprobante != null && !rutaComprobante.trim().isEmpty()) || 
                (observaciones != null && !observaciones.trim().isEmpty())) {
                // Usar método completo que guarda comprobante y observaciones en BD
                pagoProcesado = procesarPagoUseCase.ejecutarCompleto(id, monto, metodoPago, rutaComprobante, observaciones);
                logger.info("Pago procesado completamente - Comprobante: {} | Observaciones: {}", rutaComprobante, observaciones);
            } else {
                // Usar método básico si no hay comprobante ni observaciones
                pagoProcesado = procesarPagoUseCase.ejecutar(id, monto, metodoPago);
            }
            
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
     * Actualizar estado de un pago (para estados diferentes a PAGADO)
     * PUT /api/pagos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarEstadoPago(
            @PathVariable Long id,
            @RequestParam String estado,
            @RequestParam(required = false) String observaciones) {
        try {
            logger.info("Actualizando estado del pago ID: {} a estado: {}", id, estado);
            
            // Convertir string a enum
            Pago.EstadoPago nuevoEstado;
            try {
                nuevoEstado = Pago.EstadoPago.valueOf(estado.toLowerCase());
            } catch (IllegalArgumentException e) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Estado inválido: " + estado + ". Estados válidos: pendiente, atraso, rechazado");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
            Pago pagoActualizado = actualizarEstadoPagoUseCase.ejecutar(id, nuevoEstado, observaciones);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Estado del pago actualizado exitosamente");
            response.put("data", convertirPagoAMap(pagoActualizado));
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Error de validación al actualizar estado: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            logger.error("Error al actualizar estado del pago: ", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Verificar pagos en atraso usando caso de uso
     * PUT /api/pagos/verificar-atrasos
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
     * Eliminar pago usando caso de uso
     * DELETE /api/v2/pagos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarPago(@PathVariable Long id) {
        try {
            logger.info("Eliminando pago con ID: {} usando caso de uso", id);
            
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