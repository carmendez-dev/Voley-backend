package com.voley.application.categorias;

import com.voley.domain.Categoria;
import com.voley.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para actualizar una categoría existente
 */
@Component
public class ActualizarCategoriaUseCase {
    
    private final CategoriaService categoriaService;
    
    @Autowired
    public ActualizarCategoriaUseCase(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }
    
    /**
     * Ejecuta la actualización de una categoría
     */
    public Categoria ejecutar(Long id, Categoria categoria) {
        return categoriaService.actualizarCategoria(id, categoria);
    }
}