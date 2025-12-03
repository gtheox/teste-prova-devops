package com.skillmatch.repository;

import com.skillmatch.entity.Habilidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface HabilidadeRepository extends JpaRepository<Habilidade, Long> {

    Optional<Habilidade> findByNomeHabilidade(String nomeHabilidade);

    List<Habilidade> findByTipo(String tipo);
}
