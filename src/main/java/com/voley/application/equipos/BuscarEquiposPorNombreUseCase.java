package com.voley.application.equipos;

import com.voley.domain.Equipo;
import com.voley.repository.EquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Caso de uso para buscar equipos por nombre
 * 
 * Realiza búsquedas de equipos utilizando el nombre como criterio.
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class BuscarEquiposPorNombreUseCase {
    
    private final EquipoRepository equipoRepository;
    
    @Autowired
    public BuscarEquiposPorNombreUseCase(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }
    
    /**
     * Ejecuta el caso de uso para buscar equipos por nombre
     * @param nombre Nombre o parte del nombre a buscar
     * @return Lista de equipos que coinciden con el criterio
     * @throws IllegalArgumentException si el nombre es inválido
     */
    public List<Equipo> ejecutar(String nombre) {
        // Validar nombre
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de búsqueda es requerido");
        }
        
        // Buscar equipos
        return equipoRepository.buscarPorNombre(nombre.trim());
    }
}