package com.voley.application.categorias;

import com.voley.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para eliminar una categoría
 */
@Component
public class EliminarCategoriaUseCase {
    
    private final CategoriaService categoriaService;
    
    @Autowired
    public EliminarCategoriaUseCase(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }
    
    /**
     * Ejecuta la eliminación de una categoría
     */
    public void ejecutar(Long id) {
        categoriaService.eliminarCategoria(id);
    }
}