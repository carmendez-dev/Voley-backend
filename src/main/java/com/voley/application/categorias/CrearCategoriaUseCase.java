package com.voley.application.categorias;

import com.voley.domain.Categoria;
import com.voley.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para crear una nueva categoría
 */
@Component
public class CrearCategoriaUseCase {
    
    private final CategoriaService categoriaService;
    
    @Autowired
    public CrearCategoriaUseCase(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }
    
    /**
     * Ejecuta la creación de una nueva categoría
     */
    public Categoria ejecutar(Categoria categoria) {
        return categoriaService.crearCategoria(categoria);
    }
}