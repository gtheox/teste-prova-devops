package com.skillmatch.repository;

import com.skillmatch.entity.Trabalhador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface TrabalhadorRepository extends JpaRepository<Trabalhador, Long> {

    Optional<Trabalhador> findByEmail(String email);

    Optional<Trabalhador> findByCpf(String cpf);

    List<Trabalhador> findByStatusConta(String statusConta);

    @Query("SELECT t FROM Trabalhador t WHERE t.statusConta = 'ATIVA'")
    List<Trabalhador> findAllAtivos();
}
