package com.voley.controller;

import com.voley.domain.Pago;
import com.voley.domain.Usuario;
import com.voley.dto.PagoResumenDTO;
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
    public ResponseEntity<Pago> crearPago(@RequestBody Pago pago) {
        try {
            Pago nuevoPago = pagoService.crearPago(pago);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPago);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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
     * Obtener pagos por usuario
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pago>> obtenerPagosPorUsuario(@PathVariable Long usuarioId) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(usuarioId);
        if (usuario.isPresent()) {
            List<Pago> pagos = pagoService.obtenerPagosPorUsuarioOrdenados(usuario.get());
            return ResponseEntity.ok(pagos);
        }
        return ResponseEntity.notFound().build();
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
    public ResponseEntity<String> eliminarPago(@PathVariable Long id) {
        boolean eliminado = pagoService.eliminarPago(id);
        if (eliminado) {
            return ResponseEntity.ok("Pago eliminado exitosamente");
        } else {
            return ResponseEntity.badRequest()
                               .body("No se puede eliminar el pago. Debe estar en estado PENDIENTE");
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
}