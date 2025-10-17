package com.voley.application.categorias;

import com.voley.domain.Categoria;
import com.voley.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Caso de uso para obtener todas las categorías
 */
@Component
public class ObtenerTodasLasCategoriasUseCase {
    
    private final CategoriaService categoriaService;
    
    @Autowired
    public ObtenerTodasLasCategoriasUseCase(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }
    
    /**
     * Ejecuta la obtención de todas las categorías
     */
    public List<Categoria> ejecutar() {
        return categoriaService.obtenerTodasLasCategorias();
    }
}