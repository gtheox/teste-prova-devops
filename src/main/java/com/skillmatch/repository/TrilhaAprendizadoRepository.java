package com.skillmatch.repository;

import com.skillmatch.entity.TrilhaAprendizado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TrilhaAprendizadoRepository extends JpaRepository<TrilhaAprendizado, Long> {

    List<TrilhaAprendizado> findByTituloTrilhaContainingIgnoreCase(String titulo);
}
