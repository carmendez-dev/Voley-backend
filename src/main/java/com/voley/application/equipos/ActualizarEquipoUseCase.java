package com.voley.application.equipos;

import com.voley.domain.Equipo;
import com.voley.repository.EquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para actualizar un equipo existente
 * 
 * Valida los datos y actualiza un equipo en el repositorio.
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Service
@Transactional
public class ActualizarEquipoUseCase {
    
    private final EquipoRepository equipoRepository;
    
    @Autowired
    public ActualizarEquipoUseCase(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }
    
    /**
     * Ejecuta el caso de uso para actualizar un equipo
     * @param id ID del equipo a actualizar
     * @param nombre Nuevo nombre del equipo
     * @param descripcion Nueva descripción del equipo
     * @return Equipo actualizado
     * @throws IllegalArgumentException si los datos no son válidos
     * @throws RuntimeException si no se encuentra el equipo
     */
    public Equipo ejecutar(Long id, String nombre, String descripcion) {
        // Validar ID
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del equipo debe ser un número positivo");
        }
        
        // Validar datos
        validarDatos(nombre);
        
        // Verificar que el equipo existe
        Equipo equipoExistente = equipoRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("No existe equipo con ID: " + id));
        
        // Verificar que no haya otro equipo con el mismo nombre
        if (equipoRepository.existePorNombreExcluyendoId(nombre, id)) {
            throw new IllegalArgumentException("Ya existe otro equipo con el nombre: " + nombre);
        }
        
        // Actualizar datos
        equipoExistente.setNombre(nombre);
        equipoExistente.setDescripcion(descripcion);
        
        // Guardar cambios
        return equipoRepository.guardar(equipoExistente);
    }
    
    /**
     * Valida los datos de entrada
     */
    private void validarDatos(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del equipo es requerido");
        }
        
        if (nombre.length() > 100) {
            throw new IllegalArgumentException("El nombre del equipo no puede tener más de 100 caracteres");
        }
    }
}