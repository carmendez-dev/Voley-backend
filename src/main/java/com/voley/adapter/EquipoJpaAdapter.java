package com.voley.adapter;

import com.voley.domain.Equipo;
import com.voley.repository.EquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador JPA para el repositorio de Equipo
 * 
 * Implementa la interfaz EquipoRepository usando Spring Data JPA
 * Actúa como puente entre la capa de dominio y la persistencia
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Component
public class EquipoJpaAdapter implements EquipoRepository {
    
    private final EquipoJpaRepository equipoJpaRepository;
    
    @Autowired
    public EquipoJpaAdapter(EquipoJpaRepository equipoJpaRepository) {
        this.equipoJpaRepository = equipoJpaRepository;
    }
    
    @Override
    public Equipo guardar(Equipo equipo) {
        return equipoJpaRepository.save(equipo);
    }
    
    @Override
    public Optional<Equipo> buscarPorId(Long id) {
        return equipoJpaRepository.findById(id);
    }
    
    @Override
    public List<Equipo> buscarTodos() {
        return equipoJpaRepository.findAllOrderByNombre();
    }
    
    @Override
    public void eliminar(Long id) {
        equipoJpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existePorId(Long id) {
        return equipoJpaRepository.existsById(id);
    }
    
    @Override
    public List<Equipo> buscarPorNombre(String nombre) {
        return equipoJpaRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    @Override
    public Optional<Equipo> buscarPorNombreExacto(String nombre) {
        return equipoJpaRepository.findByNombreIgnoreCase(nombre);
    }
    
    @Override
    public boolean existePorNombre(String nombre) {
        return equipoJpaRepository.existsByNombreIgnoreCase(nombre);
    }
    
    @Override
    public boolean existePorNombreExcluyendoId(String nombre, Long id) {
        return equipoJpaRepository.existsByNombreIgnoreCaseAndIdEquipoNot(nombre, id);
    }
    
    @Override
    public List<Equipo> buscarPorTexto(String texto) {
        return equipoJpaRepository.findByNombreOrDescripcionContainingIgnoreCase(texto);
    }
    
    @Override
    public long contarTotal() {
        return equipoJpaRepository.countTotalEquipos();
    }
}