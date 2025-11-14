package com.voley.controller;

import com.voley.service.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para manejo de uploads de archivos
 * Maneja la subida de comprobantes de pago
 */
@RestController
@RequestMapping("/api/upload")
public class UploadController {
    
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    
    private final FileUploadService fileUploadService;
    
    @Autowired
    public UploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }
    
    /**
     * Subir comprobante de pago
     * POST /api/upload/comprobantes
     */
    @PostMapping("/comprobantes")
    public ResponseEntity<Map<String, Object>> subirComprobante(
            @RequestParam("comprobante") MultipartFile file,
            @RequestParam("pagoId") Long pagoId) {
        
        try {
            logger.info("Subiendo comprobante para pago ID: {} - Archivo: {} (tamaño: {} bytes)", 
                       pagoId, file.getOriginalFilename(), file.getSize());
            
            // Validar pagoId
            if (pagoId == null || pagoId <= 0) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "ID de pago inválido");
                errorResponse.put("timestamp", LocalDate.now().toString());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
            // Subir archivo
            FileUploadService.FileUploadResult result = fileUploadService.subirComprobante(file, pagoId);
            
            // Respuesta exitosa
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", result.getMessage());
            response.put("timestamp", LocalDate.now().toString());
            
            Map<String, Object> data = new HashMap<>();
            data.put("ruta", result.getFilePath());
            data.put("nombreArchivo", result.getFileName());
            data.put("nombreOriginal", result.getOriginalName());
            data.put("tamaño", result.getFileSize());
            data.put("tipoContenido", result.getContentType());
            data.put("pagoId", pagoId);
            
            response.put("data", data);
            
            logger.info("Comprobante subido exitosamente para pago ID: {} - Ruta: {}", pagoId, result.getFilePath());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            logger.warn("Error de validación al subir comprobante para pago {}: {}", pagoId, e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", LocalDate.now().toString());
            errorResponse.put("pagoId", pagoId);
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            
        } catch (Exception e) {
            logger.error("Error interno al subir comprobante para pago {}: {}", pagoId, e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor al subir el archivo");
            errorResponse.put("timestamp", LocalDate.now().toString());
            errorResponse.put("pagoId", pagoId);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Eliminar comprobante
     * DELETE /api/upload/comprobantes/{rutaArchivo}
     */
    @DeleteMapping("/comprobantes")
    public ResponseEntity<Map<String, Object>> eliminarComprobante(@RequestParam("ruta") String rutaArchivo) {
        try {
            logger.info("Eliminando comprobante: {}", rutaArchivo);
            
            if (rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Ruta de archivo inválida");
                errorResponse.put("timestamp", LocalDate.now().toString());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
            boolean eliminado = fileUploadService.eliminarComprobante(rutaArchivo);
            
            Map<String, Object> response = new HashMap<>();
            response.put("timestamp", LocalDate.now().toString());
            response.put("ruta", rutaArchivo);
            
            if (eliminado) {
                response.put("success", true);
                response.put("message", "Comprobante eliminado exitosamente");
                logger.info("Comprobante eliminado exitosamente: {}", rutaArchivo);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "No se pudo eliminar el comprobante o el archivo no existe");
                logger.warn("No se pudo eliminar el comprobante: {}", rutaArchivo);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
        } catch (Exception e) {
            logger.error("Error interno al eliminar comprobante {}: {}", rutaArchivo, e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            errorResponse.put("timestamp", LocalDate.now().toString());
            errorResponse.put("ruta", rutaArchivo);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Verificar si existe un comprobante
     * GET /api/upload/comprobantes/exists
     */
    @GetMapping("/comprobantes/exists")
    public ResponseEntity<Map<String, Object>> verificarComprobante(@RequestParam("ruta") String rutaArchivo) {
        try {
            logger.info("Verificando existencia de comprobante: {}", rutaArchivo);
            
            boolean existe = fileUploadService.existeArchivo(rutaArchivo);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Verificación completada");
            response.put("timestamp", LocalDate.now().toString());
            response.put("ruta", rutaArchivo);
            response.put("existe", existe);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error al verificar comprobante {}: {}", rutaArchivo, e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error interno del servidor");
            errorResponse.put("timestamp", LocalDate.now().toString());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}