package com.voley.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Configuración específica para el módulo de Equipos
 * 
 * Habilita el escaneo de componentes para todas las clases
 * relacionadas con equipos y sus relaciones.
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Configuration
@ComponentScan(basePackages = {
    "com.voley.adapter",
    "com.voley.application.equipos",
    "com.voley.service",
    "com.voley.controller"
})
public class EquipoConfiguracion {
    
    // Esta clase permite que Spring detecte automáticamente
    // todos los componentes del módulo de equipos:
    // - Adaptadores JPA
    // - Casos de uso
    // - Servicios
    // - Controladores
}