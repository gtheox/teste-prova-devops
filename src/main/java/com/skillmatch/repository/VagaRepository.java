package com.skillmatch.repository;

import com.skillmatch.entity.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VagaRepository extends JpaRepository<Vaga, Long> {

    List<Vaga> findByEmpresaIdEmpresa(Long idEmpresa);

    List<Vaga> findByTituloContainingIgnoreCase(String titulo);
}
