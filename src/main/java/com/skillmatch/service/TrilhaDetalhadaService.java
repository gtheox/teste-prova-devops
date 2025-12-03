package com.skillmatch.service;

import com.skillmatch.entity.TrilhaDetalhada;
import com.skillmatch.repository.TrilhaDetalhadaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TrilhaDetalhadaService {

    @Autowired
    private TrilhaDetalhadaRepository trilhaRepository;

    @Transactional
    public TrilhaDetalhada criar(TrilhaDetalhada trilha) {
        log.info("Criando nova trilha detalhada: {}", trilha.getTituloTrilha());
        return trilhaRepository.save(trilha);
    }

    public Optional<TrilhaDetalhada> obterPorId(Long id) {
        log.info("Buscando trilha detalhada com ID: {}", id);
        return trilhaRepository.findById(id);
    }

    public Optional<TrilhaDetalhada> obterPorIdRelacional(Long idTrilhaRelacional) {
        log.info("Buscando trilha detalhada por ID relacional: {}", idTrilhaRelacional);
        return trilhaRepository.findByIdTrilhaRelacional(idTrilhaRelacional);
    }

    public List<TrilhaDetalhada> listarTodas() {
        log.info("Listando todas as trilhas detalhadas");
        return trilhaRepository.findAll();
    }

    public List<TrilhaDetalhada> listarAtivas() {
        log.info("Listando trilhas detalhadas ativas");
        return trilhaRepository.findByStatus("Ativa");
    }

    @Transactional
    public TrilhaDetalhada atualizar(Long id, TrilhaDetalhada trilha) {
        log.info("Atualizando trilha detalhada com ID: {}", id);
        trilha.setId(id);
        return trilhaRepository.save(trilha);
    }

    @Transactional
    public void deletar(Long id) {
        log.info("Deletando trilha detalhada com ID: {}", id);
        trilhaRepository.deleteById(id);
    }

    public List<TrilhaDetalhada> buscarPorTitulo(String titulo) {
        log.info("Buscando trilhas por título: {}", titulo);
        return trilhaRepository.findByTituloTrilhaContainingIgnoreCase(titulo);
    }
}
