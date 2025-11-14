package com.voley.application.setpartido;

import com.voley.adapter.SetPartidoJpaRepository;
import com.voley.domain.SetPartido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso: Eliminar SetPartido
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Component
public class EliminarSetPartidoUseCase {
    
    private final SetPartidoJpaRepository setPartidoRepository;
    
    @Autowired
    public EliminarSetPartidoUseCase(SetPartidoJpaRepository setPartidoRepository) {
        this.setPartidoRepository = setPartidoRepository;
    }
    
    @Transactional
    public void ejecutar(Long id) {
        SetPartido setPartido = setPartidoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No existe set con ID: " + id));
        
        setPartidoRepository.delete(setPartido);
    }
}
