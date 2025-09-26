package com.voley.controller;

import com.voley.domain.Pago;
import com.voley.domain.Usuario;
import com.voley.dto.PagoResumenDTO;
import com.voley.dto.PagosPorUsuarioDTO;
import com.voley.service.PagoService;
import com.voley.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
public class PagoController {
    
    private final PagoService pagoService;
    private final UsuarioService usuarioService;
    
    @Autowired
    public PagoController(PagoService pagoService, UsuarioService usuarioService) {
        this.pagoService = pagoService;
        this.usuarioService = usuarioService;
    }
    
    /**
     * Crear un nuevo pago manualmente
     */
    @PostMapping
    public ResponseEntity<?> crearPago(@RequestBody Map<String, Object> pagoData) {
        try {
            // Validar campos obligatorios
            if (!pagoData.containsKey("usuario_id") || pagoData.get("usuario_id") == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "El usuario_id es obligatorio"));
            }
            if (!pagoData.containsKey("periodo_mes") || pagoData.get("periodo_mes") == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "El periodo_mes es obligatorio"));
            }
            if (!pagoData.containsKey("periodo_anio") || pagoData.get("periodo_anio") == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "El periodo_anio es obligatorio"));
            }
            if (!pagoData.containsKey("monto") || pagoData.get("monto") == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "El monto es obligatorio"));
            }
            if (!pagoData.containsKey("estado") || pagoData.get("estado") == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "El estado es obligatorio"));
            }
            if (!pagoData.containsKey("metodo_pago") || pagoData.get("metodo_pago") == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "El metodo_pago es obligatorio"));
            }
            
            // Obtener usuario
            Long usuarioId = Long.valueOf(pagoData.get("usuario_id").toString());
            Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(usuarioId);
            if (!usuarioOpt.isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Usuario no encontrado"));
            }
            
            // Validar estado
            String estadoStr = pagoData.get("estado").toString().toLowerCase();
            Pago.EstadoPago estado;
            try {
                estado = Pago.EstadoPago.valueOf(estadoStr);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Estado inválido. Debe ser: pendiente, pagado, atraso, o rechazado"));
            }
            
            // Crear objeto Pago
            Pago pago = new Pago();
            pago.setUsuario(usuarioOpt.get());
            pago.setPeriodoMes(Integer.valueOf(pagoData.get("periodo_mes").toString()));
            pago.setPeriodoAnio(Integer.valueOf(pagoData.get("periodo_anio").toString()));
            pago.setMonto(new java.math.BigDecimal(pagoData.get("monto").toString()));
            pago.setEstado(estado);
            pago.setMetodoPago(pagoData.get("metodo_pago").toString());
            
            // Campos opcionales
            if (pagoData.containsKey("comprobante") && pagoData.get("comprobante") != null) {
                pago.setComprobante(pagoData.get("comprobante").toString());
            }
            if (pagoData.containsKey("observaciones") && pagoData.get("observaciones") != null) {
                pago.setObservaciones(pagoData.get("observaciones").toString());
            }
            
            // Crear el pago (el service se encarga de fecha_registro, created_at, updated_at, fecha_vencimiento, y fecha_pago si es pagado)
            Pago nuevoPago = pagoService.crearPago(pago);
            
            // Preparar respuesta con mensaje de éxito y información del usuario
            Map<String, Object> respuesta = new java.util.HashMap<>();
            respuesta.put("mensaje", "Pago creado exitosamente");
            
            // Información del usuario (sin referencias circulares)
            Map<String, Object> usuarioInfo = new java.util.HashMap<>();
            usuarioInfo.put("id", usuarioOpt.get().getId());
            usuarioInfo.put("nombreCompleto", usuarioOpt.get().getNombreCompleto());
            usuarioInfo.put("email", usuarioOpt.get().getEmail() != null ? usuarioOpt.get().getEmail() : "No registrado");
            respuesta.put("usuario", usuarioInfo);
            
            // Información básica del pago (sin referencias al usuario)
            Map<String, Object> pagoInfo = new java.util.HashMap<>();
            pagoInfo.put("id", nuevoPago.getId());
            pagoInfo.put("periodo", formatearPeriodo(nuevoPago.getPeriodoMes(), nuevoPago.getPeriodoAnio()));
            pagoInfo.put("monto", nuevoPago.getMonto().toString());
            pagoInfo.put("estado", nuevoPago.getEstado().toString());
            respuesta.put("pago", pagoInfo);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
            
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Formato numérico inválido: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }
    
    /**
     * Obtener un pago por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPagoPorId(@PathVariable Long id) {
        Optional<Pago> pago = pagoService.obtenerPagoPorId(id);
        return pago.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Modificar el estado de un pago
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<Map<String, Object>> modificarEstadoPago(
            @PathVariable Long id, 
            @RequestBody Map<String, String> request) {
        try {
            // Validar que se proporcione el estado
            if (!request.containsKey("estado") || request.get("estado") == null || request.get("estado").trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El estado es requerido"));
            }
            
            // Obtener el pago
            Optional<Pago> pagoOpt = pagoService.obtenerPagoPorId(id);
            if (!pagoOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            Pago pago = pagoOpt.get();
            
            // Validar el nuevo estado
            String nuevoEstadoStr = request.get("estado").toLowerCase();
            Pago.EstadoPago nuevoEstado;
            try {
                nuevoEstado = Pago.EstadoPago.valueOf(nuevoEstadoStr);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Estado inválido. Debe ser: pendiente, pagado, atraso, o rechazado"));
            }
            
            // Actualizar el estado
            Pago.EstadoPago estadoAnterior = pago.getEstado();
            pago.setEstado(nuevoEstado);
            
            // Lógica para manejar fecha_pago según el cambio de estado
            if (nuevoEstado == Pago.EstadoPago.pagado && estadoAnterior != Pago.EstadoPago.pagado) {
                // Si cambia a "pagado", establecer la fecha de pago
                pago.setFechaPago(java.time.LocalDate.now());
            } else if (estadoAnterior == Pago.EstadoPago.pagado && nuevoEstado != Pago.EstadoPago.pagado) {
                // Si cambia de "pagado" a cualquier otro estado, eliminar la fecha de pago
                pago.setFechaPago(null);
            }
            
            // Guardar el pago actualizado
            Pago pagoActualizado = pagoService.actualizarPago(pago);
            
            // Preparar respuesta exitosa
            Map<String, Object> respuesta = new java.util.HashMap<>();
            respuesta.put("mensaje", "Estado del pago actualizado exitosamente");
            respuesta.put("pagoId", pagoActualizado.getId());
            respuesta.put("estadoAnterior", estadoAnterior.toString());
            respuesta.put("estadoNuevo", pagoActualizado.getEstado().toString());
            respuesta.put("periodo", formatearPeriodo(pagoActualizado.getPeriodoMes(), pagoActualizado.getPeriodoAnio()));
            respuesta.put("monto", pagoActualizado.getMonto().toString());
            
            // Información del usuario
            Map<String, Object> usuarioInfo = new java.util.HashMap<>();
            usuarioInfo.put("id", pagoActualizado.getUsuario().getId());
            usuarioInfo.put("nombreCompleto", pagoActualizado.getUsuario().getNombreCompleto());
            usuarioInfo.put("email", pagoActualizado.getUsuario().getEmail() != null ? pagoActualizado.getUsuario().getEmail() : "No registrado");
            respuesta.put("usuario", usuarioInfo);
            
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }
    
    /**
     * Obtener todos los pagos - Información resumida
     */
    @GetMapping
    public ResponseEntity<List<PagoResumenDTO>> obtenerTodosLosPagos() {
        List<Pago> pagos = pagoService.obtenerTodosLosPagos();
        List<PagoResumenDTO> pagosResumen = pagos.stream()
                .map(PagoResumenDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pagosResumen);
    }
    
    /**
     * Obtener todos los pagos - Información completa (para administradores)
     */
    @GetMapping("/completos")
    public ResponseEntity<List<Pago>> obtenerTodosLosPagosCompletos() {
        List<Pago> pagos = pagoService.obtenerTodosLosPagos();
        return ResponseEntity.ok(pagos);
    }
    
    /**
     * Obtener pagos por usuario con información detallada y organizada
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<PagosPorUsuarioDTO> obtenerPagosPorUsuario(@PathVariable Long usuarioId) {
        try {
            Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(usuarioId);
            if (usuario.isPresent()) {
                List<Pago> pagos = pagoService.obtenerPagosPorUsuarioOrdenados(usuario.get());
                
                // Crear DTO con información del usuario
                PagosPorUsuarioDTO.UsuarioInfoDTO usuarioInfo = new PagosPorUsuarioDTO.UsuarioInfoDTO();
                usuarioInfo.setId(usuario.get().getId());
                usuarioInfo.setNombreCompleto(usuario.get().getNombreCompleto());
                usuarioInfo.setEmail(usuario.get().getEmail() != null ? usuario.get().getEmail() : "No registrado");
                usuarioInfo.setCelular(usuario.get().getCelular() != null ? usuario.get().getCelular() : "No registrado");
                usuarioInfo.setEstado(usuario.get().getEstado() != null ? usuario.get().getEstado().toString() : "No definido");
                
                // Crear DTO principal
                PagosPorUsuarioDTO respuesta = new PagosPorUsuarioDTO();
                respuesta.setUsuario(usuarioInfo);
                respuesta.setResumen(calcularResumenPagos(pagos));
                respuesta.setPagos(formatearPagosDetallados(pagos));
                
                return ResponseEntity.ok(respuesta);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Obtener pagos por estado
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pago>> obtenerPagosPorEstado(@PathVariable String estado) {
        try {
            Pago.EstadoPago estadoPago = Pago.EstadoPago.valueOf(estado.toUpperCase());
            List<Pago> pagos = pagoService.obtenerPagosPorEstado(estadoPago);
            return ResponseEntity.ok(pagos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Obtener pagos por período
     */
    @GetMapping("/periodo/{anio}/{mes}")
    public ResponseEntity<List<Pago>> obtenerPagosPorPeriodo(
            @PathVariable Integer anio, 
            @PathVariable Integer mes) {
        List<Pago> pagos = pagoService.obtenerPagosPorPeriodo(mes, anio);
        return ResponseEntity.ok(pagos);
    }
    
    /**
     * Obtener pago específico de un usuario por período
     */
    @GetMapping("/usuario/{usuarioId}/periodo/{anio}/{mes}")
    public ResponseEntity<Pago> obtenerPagoPorUsuarioYPeriodo(
            @PathVariable Long usuarioId,
            @PathVariable Integer anio,
            @PathVariable Integer mes) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(usuarioId);
        if (usuario.isPresent()) {
            Optional<Pago> pago = pagoService.obtenerPagoPorUsuarioYPeriodo(usuario.get(), mes, anio);
            return pago.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Marcar pago como pagado
     */
    @PutMapping("/{id}/pagar")
    public ResponseEntity<Pago> marcarComoPagado(
            @PathVariable Long id,
            @RequestBody Map<String, Object> datos) {
        try {
            Double monto = Double.valueOf(datos.get("monto").toString());
            String metodoPago = datos.get("metodoPago").toString();
            
            Pago pago = pagoService.marcarComoPagado(id, monto, metodoPago);
            return ResponseEntity.ok(pago);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Marcar pago como rechazado
     */
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<Pago> marcarComoRechazado(
            @PathVariable Long id,
            @RequestBody Map<String, String> datos) {
        try {
            String motivoRechazo = datos.get("motivoRechazo");
            Pago pago = pagoService.marcarComoRechazado(id, motivoRechazo);
            return ResponseEntity.ok(pago);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Verificar si un usuario tiene deudas
     */
    @GetMapping("/usuario/{usuarioId}/deudas")
    public ResponseEntity<Map<String, Object>> verificarDeudas(@PathVariable Long usuarioId) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(usuarioId);
        if (usuario.isPresent()) {
            boolean tieneDeudas = pagoService.tieneDeudas(usuario.get());
            long pagosEnAtraso = pagoService.contarPagosEnAtraso(usuario.get());
            
            Map<String, Object> respuesta = Map.of(
                "tieneDeudas", tieneDeudas,
                "pagosEnAtraso", pagosEnAtraso
            );
            return ResponseEntity.ok(respuesta);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Generar pagos para un nuevo usuario
     */
    @PostMapping("/generar/usuario/{usuarioId}")
    public ResponseEntity<String> generarPagosParaNuevoUsuario(@PathVariable Long usuarioId) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(usuarioId);
        if (usuario.isPresent()) {
            pagoService.generarPagosParaNuevoUsuario(usuario.get());
            return ResponseEntity.ok("Pagos generados exitosamente para el usuario");
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Ejecutar generación mensual automática (endpoint para administradores)
     */
    @PostMapping("/generar/mensual")
    public ResponseEntity<String> generarPagosMensuales() {
        try {
            pagoService.generarPagosMensualesAutomaticos();
            return ResponseEntity.ok("Pagos mensuales generados exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body("Error al generar pagos mensuales: " + e.getMessage());
        }
    }
    
    /**
     * Actualizar pagos en atraso (endpoint para administradores)
     */
    @PostMapping("/actualizar/atrasos")
    public ResponseEntity<String> actualizarPagosEnAtraso() {
        try {
            pagoService.actualizarPagosEnAtraso();
            return ResponseEntity.ok("Pagos en atraso actualizados exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body("Error al actualizar pagos en atraso: " + e.getMessage());
        }
    }
    
    /**
     * Eliminar un pago (solo si está pendiente)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarPago(@PathVariable Long id) {
        try {
            // Obtener el pago antes de eliminarlo para mostrar información
            Optional<Pago> pagoOpt = pagoService.obtenerPagoPorId(id);
            if (!pagoOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            Pago pago = pagoOpt.get();
            
            // Verificar que el pago esté en estado PENDIENTE
            if (pago.getEstado() != Pago.EstadoPago.pendiente) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Solo se pueden eliminar pagos en estado PENDIENTE",
                    "estadoActual", pago.getEstado().toString(),
                    "pagoId", pago.getId(),
                    "periodo", formatearPeriodo(pago.getPeriodoMes(), pago.getPeriodoAnio())
                ));
            }
            
            // Intentar eliminar el pago
            boolean eliminado = pagoService.eliminarPago(id);
            
            if (eliminado) {
                // Preparar respuesta exitosa
                Map<String, Object> respuesta = new java.util.HashMap<>();
                respuesta.put("mensaje", "Pago eliminado exitosamente");
                respuesta.put("pagoEliminado", Map.of(
                    "id", pago.getId(),
                    "periodo", formatearPeriodo(pago.getPeriodoMes(), pago.getPeriodoAnio()),
                    "monto", pago.getMonto().toString(),
                    "estado", pago.getEstado().toString()
                ));
                
                // Información del usuario
                Map<String, Object> usuarioInfo = new java.util.HashMap<>();
                usuarioInfo.put("id", pago.getUsuario().getId());
                usuarioInfo.put("nombreCompleto", pago.getUsuario().getNombreCompleto());
                usuarioInfo.put("email", pago.getUsuario().getEmail() != null ? pago.getUsuario().getEmail() : "No registrado");
                respuesta.put("usuario", usuarioInfo);
                
                return ResponseEntity.ok(respuesta);
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "No se pudo eliminar el pago"));
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }
    
    /**
     * Ejecutar generación manual de pagos para un usuario específico
     */
    @PostMapping("/generar/usuario/{usuarioId}/manual")
    public ResponseEntity<String> generarPagosManualParaUsuario(@PathVariable Long usuarioId) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(usuarioId);
        if (usuario.isPresent()) {
            try {
                pagoService.generarPagosParaNuevoUsuario(usuario.get());
                return ResponseEntity.ok("Pagos generados manualmente para el usuario: " + usuario.get().getNombreCompleto());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                   .body("Error generando pagos: " + e.getMessage());
            }
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Obtener estadísticas de pagos automáticos
     */
    @GetMapping("/estadisticas/automaticos")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasPagosAutomaticos() {
        try {
            LocalDate fechaActual = LocalDate.now();
            List<Usuario> usuariosActivos = usuarioService.obtenerUsuariosPorEstado(Usuario.EstadoUsuario.Activo);
            List<Pago> pagosDelMes = pagoService.obtenerPagosPorPeriodo(
                fechaActual.getMonthValue(), fechaActual.getYear());
            
            Map<String, Object> estadisticas = Map.of(
                "usuariosActivos", usuariosActivos.size(),
                "pagosGeneradosEsteMes", pagosDelMes.size(),
                "mesActual", fechaActual.getMonthValue(),
                "anioActual", fechaActual.getYear()
            );
            
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Simular generación de pagos para un usuario con fecha de registro específica (solo para testing)
     */
    @PostMapping("/simular-pagos/{usuarioId}/{fechaRegistro}")
    public ResponseEntity<Map<String, Object>> simularPagosParaUsuario(
            @PathVariable Long usuarioId,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaRegistro) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(usuarioId);
        if (usuario.isPresent()) {
            try {
                // Temporalmente cambiar la fecha de registro para la simulación
                LocalDate fechaOriginal = usuario.get().getCreatedAt() != null ? 
                    usuario.get().getCreatedAt().toLocalDate() : usuario.get().getFechaRegistro();
                
                // Simular que el usuario se registró en la fecha especificada
                // Para esto, calculamos los meses que deberían tener pagos
                LocalDate fechaActual = LocalDate.now();
                List<String> mesesSimulados = new ArrayList<>();
                int pagosEsperados = 0;
                
                LocalDate fechaIteracion = fechaRegistro.withDayOfMonth(1);
                while (!fechaIteracion.isAfter(fechaActual)) {
                    mesesSimulados.add(fechaIteracion.getMonthValue() + "/" + fechaIteracion.getYear());
                    pagosEsperados++;
                    fechaIteracion = fechaIteracion.plusMonths(1);
                }
                
                Map<String, Object> resultado = Map.of(
                    "usuario", usuario.get().getNombreCompleto(),
                    "fechaRegistroSimulada", fechaRegistro,
                    "fechaActual", fechaActual,
                    "mesesConPago", mesesSimulados,
                    "totalPagosEsperados", pagosEsperados,
                    "nota", "Esta es una simulación. Use /generar/usuario/{id}/manual para generar pagos reales."
                );
                
                return ResponseEntity.ok(resultado);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                   .body(Map.of("error", e.getMessage()));
            }
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Verificar información de fecha de registro de un usuario (para debugging)
     */
    @GetMapping("/debug/usuario/{usuarioId}/fechas")
    public ResponseEntity<Map<String, Object>> verificarFechasUsuario(@PathVariable Long usuarioId) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(usuarioId);
        if (usuario.isPresent()) {
            Usuario u = usuario.get();
            
            LocalDate fechaUsada = null;
            String origen = "";
            
            if (u.getCreatedAt() != null) {
                fechaUsada = u.getCreatedAt().toLocalDate();
                origen = "created_at (tabla usuarios)";
            } else if (u.getFechaRegistro() != null) {
                fechaUsada = u.getFechaRegistro();
                origen = "fecha_registro (tabla usuarios)";
            } else {
                fechaUsada = LocalDate.now();
                origen = "fecha actual (fallback)";
            }
            
            Map<String, Object> info = Map.of(
                "usuario", u.getNombreCompleto(),
                "usuarioId", u.getId(),
                "createdAt", u.getCreatedAt() != null ? u.getCreatedAt().toString() : "NULL",
                "fechaRegistro", u.getFechaRegistro() != null ? u.getFechaRegistro().toString() : "NULL",
                "fechaUsadaParaPagos", fechaUsada.toString(),
                "origenFecha", origen,
                "explicacion", "El sistema usa 'created_at' como prioridad principal para generar pagos desde la fecha de registro"
            );
            
            return ResponseEntity.ok(info);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Calcular resumen de pagos de un usuario
     */
    private Map<String, Object> calcularResumenPagos(List<Pago> pagos) {
        int totalPagos = pagos.size();
        int pagosPendientes = (int) pagos.stream().filter(p -> p.getEstado() == Pago.EstadoPago.pendiente).count();
        int pagosVencidos = (int) pagos.stream().filter(p -> p.getEstado() == Pago.EstadoPago.atraso).count();
        int pagosPagados = (int) pagos.stream().filter(p -> p.getEstado() == Pago.EstadoPago.pagado).count();
        int pagosRechazados = (int) pagos.stream().filter(p -> p.getEstado() == Pago.EstadoPago.rechazado).count();
        
        java.math.BigDecimal montoTotalPendiente = pagos.stream()
            .filter(p -> p.getEstado() != Pago.EstadoPago.pagado)
            .map(Pago::getMonto)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            
        java.math.BigDecimal montoTotalPagado = pagos.stream()
            .filter(p -> p.getEstado() == Pago.EstadoPago.pagado)
            .map(Pago::getMonto)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        
        return Map.of(
            "totalPagos", totalPagos,
            "pagosPendientes", pagosPendientes,
            "pagosVencidos", pagosVencidos,
            "pagosPagados", pagosPagados,
            "pagosRechazados", pagosRechazados,
            "montoTotalPendiente", montoTotalPendiente,
            "montoTotalPagado", montoTotalPagado,
            "porcentajePagado", totalPagos > 0 ? (pagosPagados * 100.0 / totalPagos) : 0.0
        );
    }
    
    /**
     * Formatear pagos con información detallada
     */
    private List<PagosPorUsuarioDTO.PagoDetalleDTO> formatearPagosDetallados(List<Pago> pagos) {
        return pagos.stream().map(pago -> {
            LocalDate hoy = LocalDate.now();
            int diasVencimiento = 0;
            boolean estaVencido = false;
            String estadoVencimiento = "A tiempo";
            
            if (pago.getFechaVencimiento() != null) {
                diasVencimiento = (int) pago.getFechaVencimiento().until(hoy).getDays();
                estaVencido = diasVencimiento > 0 && pago.getEstado() != Pago.EstadoPago.pagado;
                
                if (pago.getEstado() == Pago.EstadoPago.pagado) {
                    estadoVencimiento = "Pagado";
                } else if (diasVencimiento > 0) {
                    estadoVencimiento = "Vencido (" + diasVencimiento + " días)";
                } else if (diasVencimiento == 0) {
                    estadoVencimiento = "Vence hoy";
                } else {
                    estadoVencimiento = "Faltan " + Math.abs(diasVencimiento) + " días";
                }
            }
            
            PagosPorUsuarioDTO.PagoDetalleDTO detalle = new PagosPorUsuarioDTO.PagoDetalleDTO();
            detalle.setId(pago.getId());
            detalle.setPeriodo(formatearPeriodo(pago.getPeriodoMes(), pago.getPeriodoAnio()));
            detalle.setMonto(pago.getMonto());
            detalle.setEstado(pago.getEstado().toString());
            detalle.setFechaVencimiento(pago.getFechaVencimiento() != null ? 
                pago.getFechaVencimiento().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A");
            detalle.setFechaPago(pago.getFechaPago() != null ? 
                pago.getFechaPago().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null);
            detalle.setMetodoPago(pago.getMetodoPago() != null ? pago.getMetodoPago() : "N/A");
            detalle.setComprobante(pago.getComprobante() != null ? pago.getComprobante() : "N/A");
            detalle.setObservaciones(pago.getObservaciones() != null ? pago.getObservaciones() : "N/A");
            detalle.setVencido(estaVencido);
            detalle.setDiasVencimiento(diasVencimiento);
            detalle.setEstadoTexto(estadoVencimiento);
            
            return detalle;
        }).collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Formatear período (mes/año) a texto legible
     */
    private String formatearPeriodo(Integer mes, Integer anio) {
        if (mes == null || anio == null) return "N/A";
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                         "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return meses[mes - 1] + " " + anio;
    }
}