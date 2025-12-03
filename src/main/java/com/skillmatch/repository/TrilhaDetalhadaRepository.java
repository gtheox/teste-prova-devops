package com.skillmatch.repository;

import com.skillmatch.entity.TrilhaDetalhada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface TrilhaDetalhadaRepository extends JpaRepository<TrilhaDetalhada, Long> {

    Optional<TrilhaDetalhada> findByIdTrilhaRelacional(Long idTrilhaRelacional);

    List<TrilhaDetalhada> findByStatus(String status);

    List<TrilhaDetalhada> findByTituloTrilhaContainingIgnoreCase(String titulo);
}
