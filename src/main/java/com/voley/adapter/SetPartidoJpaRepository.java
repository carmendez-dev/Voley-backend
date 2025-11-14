package com.voley.adapter;

import com.voley.domain.SetPartido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para SetPartido
 * 
 * @author Sistema Voley
 * @version 1.0
 */
@Repository
public interface SetPartidoJpaRepository extends JpaRepository<SetPartido, Long> {
    
    /**
     * Obtener todos los sets de un partido específico
     */
    @Query("SELECT s FROM SetPartido s WHERE s.partido.idPartido = :idPartido ORDER BY s.numeroSet")
    List<SetPartido> findByPartidoId(@Param("idPartido") Long idPartido);
    
    /**
     * Obtener un set específico de un partido
     */
    @Query("SELECT s FROM SetPartido s WHERE s.partido.idPartido = :idPartido AND s.numeroSet = :numeroSet")
    SetPartido findByPartidoIdAndNumeroSet(@Param("idPartido") Long idPartido, @Param("numeroSet") Integer numeroSet);
}
