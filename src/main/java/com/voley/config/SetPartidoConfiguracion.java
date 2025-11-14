package com.voley.config;

import com.voley.adapter.SetPartidoJpaRepository;
import com.voley.adapter.PartidoJpaRepository;
import com.voley.application.setpartido.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de beans para SetPartido
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Configuration
public class SetPartidoConfiguracion {
    
    @Bean
    public CrearSetPartidoUseCase crearSetPartidoUseCase(
            SetPartidoJpaRepository setPartidoRepository,
            PartidoJpaRepository partidoRepository) {
        return new CrearSetPartidoUseCase(setPartidoRepository, partidoRepository);
    }
    
    @Bean
    public ObtenerTodosSetPartidoUseCase obtenerTodosSetPartidoUseCase(
            SetPartidoJpaRepository setPartidoRepository) {
        return new ObtenerTodosSetPartidoUseCase(setPartidoRepository);
    }
    
    @Bean
    public ObtenerSetPartidoPorIdUseCase obtenerSetPartidoPorIdUseCase(
            SetPartidoJpaRepository setPartidoRepository) {
        return new ObtenerSetPartidoPorIdUseCase(setPartidoRepository);
    }
    
    @Bean
    public ObtenerSetsPorPartidoUseCase obtenerSetsPorPartidoUseCase(
            SetPartidoJpaRepository setPartidoRepository) {
        return new ObtenerSetsPorPartidoUseCase(setPartidoRepository);
    }
    
    @Bean
    public ActualizarSetPartidoUseCase actualizarSetPartidoUseCase(
            SetPartidoJpaRepository setPartidoRepository) {
        return new ActualizarSetPartidoUseCase(setPartidoRepository);
    }
    
    @Bean
    public EliminarSetPartidoUseCase eliminarSetPartidoUseCase(
            SetPartidoJpaRepository setPartidoRepository) {
        return new EliminarSetPartidoUseCase(setPartidoRepository);
    }
}
