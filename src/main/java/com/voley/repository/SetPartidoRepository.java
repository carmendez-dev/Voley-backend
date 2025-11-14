package com.voley.repository;

import com.voley.domain.SetPartido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad SetPartido
 */
@Repository
public interface SetPartidoRepository extends JpaRepository<SetPartido, Long> {
}
