package com.voley.repository;

import com.voley.domain.Partido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Partido
 */
@Repository
public interface PartidoRepository extends JpaRepository<Partido, Long> {
}
