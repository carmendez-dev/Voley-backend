package com.voley.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * Servicio para manejo de archivos de comprobantes
 * Permite subir, validar y gestionar archivos de comprobantes de pago
 */
@Service
public class FileUploadService {
    
    private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);
    
    @Value("${app.upload.dir:public/uploads/comprobantes}")
    private String uploadDir;
    
    @Value("${app.upload.max-file-size:5242880}")
    private long maxFileSize;
    
    // Tipos de archivo permitidos (imágenes)
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "webp");
    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList(
        "image/jpeg", "image/jpg", "image/png", "image/gif", "image/bmp", "image/webp"
    );
    
    /**
     * Sube un archivo de comprobante
     * @param file Archivo a subir
     * @param pagoId ID del pago al que pertenece el comprobante
     * @return Información del archivo subido
     * @throws IOException Si hay error al subir el archivo
     * @throws IllegalArgumentException Si el archivo no es válido
     */
    public FileUploadResult subirComprobante(MultipartFile file, Long pagoId) throws IOException {
        // Validar que se envió un archivo
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("No se recibió ningún archivo");
        }
        
        // Validar tamaño
        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("El archivo es demasiado grande. Máximo permitido: " + (maxFileSize / 1024 / 1024) + "MB");
        }
        
        // Validar tipo de archivo
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre de archivo inválido");
        }
        
        String extension = getFileExtension(originalFilename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Tipo de archivo no permitido. Solo se permiten: " + String.join(", ", ALLOWED_EXTENSIONS));
        }
        
        // Validar MIME type
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Tipo de contenido no permitido. Solo se permiten imágenes");
        }
        
        // Crear directorio si no existe
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            logger.info("Directorio de uploads creado: {}", uploadPath.toAbsolutePath());
        }
        
        // Generar nombre único para el archivo
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = String.format("comp_%d_%s.%s", pagoId, timestamp, extension);
        
        // Ruta completa del archivo
        Path filePath = uploadPath.resolve(fileName);
        
        try {
            // Copiar archivo al directorio de destino
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            logger.info("Archivo subido exitosamente: {} (tamaño: {} bytes)", fileName, file.getSize());
            
            // Construir ruta relativa para la base de datos
            String relativePath = "uploads/comprobantes/" + fileName;
            
            return new FileUploadResult(
                true,
                "Archivo subido correctamente",
                fileName,
                relativePath,
                originalFilename,
                file.getSize(),
                contentType
            );
            
        } catch (IOException e) {
            logger.error("Error al subir archivo: {}", e.getMessage());
            throw new IOException("Error al guardar el archivo: " + e.getMessage(), e);
        }
    }
    
    /**
     * Elimina un archivo de comprobante
     * @param rutaArchivo Ruta relativa del archivo a eliminar
     * @return true si se eliminó exitosamente, false en caso contrario
     */
    public boolean eliminarComprobante(String rutaArchivo) {
        if (rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
            return false;
        }
        
        try {
            // Extraer solo el nombre del archivo de la ruta relativa
            String fileName = rutaArchivo.replace("uploads/comprobantes/", "");
            Path filePath = Paths.get(uploadDir, fileName);
            
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                logger.info("Archivo eliminado: {}", fileName);
                return true;
            } else {
                logger.warn("Archivo no encontrado para eliminar: {}", fileName);
                return false;
            }
        } catch (IOException e) {
            logger.error("Error al eliminar archivo {}: {}", rutaArchivo, e.getMessage());
            return false;
        }
    }
    
    /**
     * Verifica si un archivo existe
     * @param rutaArchivo Ruta relativa del archivo
     * @return true si el archivo existe, false en caso contrario
     */
    public boolean existeArchivo(String rutaArchivo) {
        if (rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
            return false;
        }
        
        String fileName = rutaArchivo.replace("uploads/comprobantes/", "");
        Path filePath = Paths.get(uploadDir, fileName);
        return Files.exists(filePath);
    }
    
    /**
     * Obtiene la extensión de un archivo
     */
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1);
    }
    
    /**
     * Clase interna para el resultado del upload
     */
    public static class FileUploadResult {
        private final boolean success;
        private final String message;
        private final String fileName;
        private final String filePath;
        private final String originalName;
        private final long fileSize;
        private final String contentType;
        
        public FileUploadResult(boolean success, String message, String fileName, String filePath, 
                              String originalName, long fileSize, String contentType) {
            this.success = success;
            this.message = message;
            this.fileName = fileName;
            this.filePath = filePath;
            this.originalName = originalName;
            this.fileSize = fileSize;
            this.contentType = contentType;
        }
        
        // Getters
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getFileName() { return fileName; }
        public String getFilePath() { return filePath; }
        public String getOriginalName() { return originalName; }
        public long getFileSize() { return fileSize; }
        public String getContentType() { return contentType; }
    }
}