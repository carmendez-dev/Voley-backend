package com.voley.application.equipos;

import com.voley.domain.Equipo;
import com.voley.repository.EquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para crear un nuevo equipo
 * 
 * Valida los datos del equipo y verifica que no exista duplicado
 * antes de guardarlo en el repositorio.
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Service
@Transactional
public class CrearEquipoUseCase {
    
    private final EquipoRepository equipoRepository;
    
    @Autowired
    public CrearEquipoUseCase(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }
    
    /**
     * Ejecuta el caso de uso para crear un equipo
     * @param nombre Nombre del equipo
     * @param descripcion Descripción del equipo
     * @return Equipo creado
     * @throws IllegalArgumentException si los datos no son válidos
     */
    public Equipo ejecutar(String nombre, String descripcion) {
        // Validar datos de entrada
        validarDatos(nombre);
        
        // Verificar que no exista un equipo con el mismo nombre
        if (equipoRepository.existePorNombre(nombre)) {
            throw new IllegalArgumentException("Ya existe un equipo con el nombre: " + nombre);
        }
        
        // Crear y guardar el equipo
        Equipo equipo = new Equipo(nombre, descripcion);
        
        return equipoRepository.guardar(equipo);
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