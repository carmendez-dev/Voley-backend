package com.voley.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * Configuración para servir archivos estáticos
 * Permite acceder a los comprobantes subidos a través de URLs
 */
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {
    
    @Value("${app.upload.dir:public/uploads/comprobantes}")
    private String uploadDir;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configurar para servir archivos desde /uploads/**
        String uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize().toString();
        
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath + "/")
                .setCachePeriod(3600); // Cache por 1 hora
        
        // También permitir acceso desde /uploads/comprobantes/**
        registry.addResourceHandler("/uploads/comprobantes/**")
                .addResourceLocations("file:" + uploadPath + "/")
                .setCachePeriod(3600);
    }
}