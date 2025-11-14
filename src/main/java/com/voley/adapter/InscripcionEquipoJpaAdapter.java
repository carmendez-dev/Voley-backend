package com.voley.adapter;

import com.voley.domain.InscripcionEquipo;
import com.voley.domain.InscripcionEquipo.EstadoInscripcion;
import com.voley.repository.InscripcionEquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador JPA que implementa InscripcionEquipoRepository
 * Conecta el dominio con la infraestructura de persistencia
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Component
public class InscripcionEquipoJpaAdapter implements InscripcionEquipoRepository {
    
    private final InscripcionEquipoJpaRepository jpaRepository;
    
    @Autowired
    public InscripcionEquipoJpaAdapter(InscripcionEquipoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public InscripcionEquipo guardar(InscripcionEquipo inscripcion) {
        inscripcion.validar();
        return jpaRepository.save(inscripcion);
    }
    
    @Override
    public Optional<InscripcionEquipo> buscarPorId(Long id) {
        return jpaRepository.findById(id);
    }
    
    @Override
    public List<InscripcionEquipo> buscarTodas() {
        return jpaRepository.findAll();
    }
    
    @Override
    public List<InscripcionEquipo> buscarPorTorneoCategoria(Long idTorneoCategoria) {
        return jpaRepository.findByTorneoCategoriaId(idTorneoCategoria);
    }
    
    @Override
    public List<InscripcionEquipo> buscarPorEquipo(Long idEquipo) {
        return jpaRepository.findByEquipoId(idEquipo);
    }
    
    @Override
    public List<InscripcionEquipo> buscarPorEstado(EstadoInscripcion estado) {
        return jpaRepository.findByEstado(estado);
    }
    
    @Override
    public List<InscripcionEquipo> buscarPorTorneoYCategoria(Long idTorneo, Long idCategoria) {
        return jpaRepository.findByTorneoAndCategoria(idTorneo, idCategoria);
    }
    
    @Override
    public boolean existeInscripcion(Long idTorneoCategoria, Long idEquipo) {
        return jpaRepository.existsByTorneoCategoriaAndEquipo(idTorneoCategoria, idEquipo);
    }
    
    @Override
    public Optional<InscripcionEquipo> buscarPorTorneoCategoriaYEquipo(Long idTorneoCategoria, Long idEquipo) {
        return jpaRepository.findByTorneoCategoriaAndEquipo(idTorneoCategoria, idEquipo);
    }
    
    @Override
    public void eliminar(Long id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existe(Long id) {
        return jpaRepository.existsById(id);
    }
    
    @Override
    public long contarPorTorneoCategoria(Long idTorneoCategoria) {
        return jpaRepository.countByTorneoCategoria(idTorneoCategoria);
    }
    
    @Override
    public long contarPorEstado(EstadoInscripcion estado) {
        return jpaRepository.countByEstado(estado);
    }
}
