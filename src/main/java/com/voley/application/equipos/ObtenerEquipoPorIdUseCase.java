package com.voley.application.equipos;

import com.voley.domain.Equipo;
import com.voley.repository.EquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para obtener un equipo por ID
 * 
 * Busca y retorna un equipo específico por su identificador.
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class ObtenerEquipoPorIdUseCase {
    
    private final EquipoRepository equipoRepository;
    
    @Autowired
    public ObtenerEquipoPorIdUseCase(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }
    
    /**
     * Ejecuta el caso de uso para obtener un equipo por ID
     * @param id ID del equipo a buscar
     * @return Equipo encontrado
     * @throws IllegalArgumentException si el ID es inválido
     * @throws RuntimeException si no se encuentra el equipo
     */
    public Equipo ejecutar(Long id) {
        // Validar ID
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del equipo debe ser un número positivo");
        }
        
        // Buscar equipo
        return equipoRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("No existe equipo con ID: " + id));
    }
}