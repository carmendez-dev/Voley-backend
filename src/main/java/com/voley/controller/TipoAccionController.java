package com.voley.controller;

import com.voley.domain.TipoAccion;
import com.voley.repository.TipoAccionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para catálogo de Tipos de Acción
 */
@RestController
@RequestMapping("/api/tipos-accion")
public class TipoAccionController {
    
    private static final Logger logger = LoggerFactory.getLogger(TipoAccionController.class);
    
    @Autowired
    private TipoAccionRepository tipoAccionRepository;
    
    /**
     * Obtiene todos los tipos de acción
     */
    @GetMapping
    public ResponseEntity<List<TipoAccion>> obtenerTodos() {
        logger.info("GET /api/tipos-accion - Obteniendo todos los tipos de acción");
        List<TipoAccion> tipos = tipoAccionRepository.findAll();
        return ResponseEntity.ok(tipos);
    }
    
    /**
     * Obtiene un tipo de acción por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoAccion> obtenerPorId(@PathVariable Integer id) {
        logger.info("GET /api/tipos-accion/{} - Obteniendo tipo de acción", id);
        return tipoAccionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
