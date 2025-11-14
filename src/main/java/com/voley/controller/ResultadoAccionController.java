package com.voley.controller;

import com.voley.domain.ResultadoAccion;
import com.voley.repository.ResultadoAccionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para catálogo de Resultados de Acción
 */
@RestController
@RequestMapping("/api/resultados-accion")
public class ResultadoAccionController {
    
    private static final Logger logger = LoggerFactory.getLogger(ResultadoAccionController.class);
    
    @Autowired
    private ResultadoAccionRepository resultadoAccionRepository;
    
    /**
     * Obtiene todos los resultados de acción
     */
    @GetMapping
    public ResponseEntity<List<ResultadoAccion>> obtenerTodos() {
        logger.info("GET /api/resultados-accion - Obteniendo todos los resultados de acción");
        List<ResultadoAccion> resultados = resultadoAccionRepository.findAll();
        return ResponseEntity.ok(resultados);
    }
    
    /**
     * Obtiene un resultado de acción por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResultadoAccion> obtenerPorId(@PathVariable Integer id) {
        logger.info("GET /api/resultados-accion/{} - Obteniendo resultado de acción", id);
        return resultadoAccionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
