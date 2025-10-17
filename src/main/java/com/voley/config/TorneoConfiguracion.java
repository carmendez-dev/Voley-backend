package com.voley.config;

import com.voley.adapter.TorneoJpaAdapter;
import com.voley.adapter.TorneoJpaRepository;
import com.voley.service.TorneoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de beans para el módulo de torneos
 * Define la estructura de dependencias para Clean Architecture
 */
@Configuration
public class TorneoConfiguracion {
    
    /**
     * Bean para el adaptador JPA de torneos
     */
    @Bean
    public TorneoJpaAdapter torneoJpaAdapter(TorneoJpaRepository torneoJpaRepository) {
        return new TorneoJpaAdapter(torneoJpaRepository);
    }
    
    /**
     * Bean para el servicio de torneos
     */
    @Bean
    public TorneoService torneoService(TorneoJpaAdapter torneoJpaAdapter) {
        return new TorneoService(torneoJpaAdapter);
    }
}