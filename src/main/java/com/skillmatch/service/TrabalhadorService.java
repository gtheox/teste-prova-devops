package com.skillmatch.service;

import com.skillmatch.dto.TrabalhadorDTO;
import com.skillmatch.entity.Habilidade;
import com.skillmatch.entity.Trabalhador;
import com.skillmatch.repository.HabilidadeRepository;
import com.skillmatch.repository.TrabalhadorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class TrabalhadorService {

    @Autowired
    private TrabalhadorRepository trabalhadorRepository;

    @Autowired
    private HabilidadeRepository habilidadeRepository;

    public TrabalhadorDTO criar(TrabalhadorDTO dto) {
        log.info("Criando novo trabalhador: {}", dto.getNome());

        Trabalhador trabalhador = new Trabalhador();
        trabalhador.setNome(dto.getNome());
        trabalhador.setEmail(dto.getEmail());
        trabalhador.setCpf(dto.getCpf());
        trabalhador.setStatusConta("ATIVA");

        if (dto.getHabilidadesIds() != null && !dto.getHabilidadesIds().isEmpty()) {
            Set<Habilidade> habilidades = new HashSet<>(
                    habilidadeRepository.findAllById(dto.getHabilidadesIds())
            );
            trabalhador.setHabilidades(habilidades);
        }

        Trabalhador salvo = trabalhadorRepository.save(trabalhador);
        log.info("Trabalhador criado com sucesso: ID {}", salvo.getIdTrabalhador());

        return converterParaDTO(salvo);
    }

    public TrabalhadorDTO obterPorId(Long id) {
        log.info("Buscando trabalhador com ID: {}", id);
        Optional<Trabalhador> trabalhador = trabalhadorRepository.findById(id);
        return trabalhador.map(this::converterParaDTO)
                .orElseThrow(() -> new RuntimeException("Trabalhador não encontrado"));
    }

    public List<TrabalhadorDTO> listarTodos() {
        log.info("Listando todos os trabalhadores");
        return trabalhadorRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public TrabalhadorDTO atualizar(Long id, TrabalhadorDTO dto) {
        log.info("Atualizando trabalhador com ID: {}", id);

        Trabalhador trabalhador = trabalhadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabalhador não encontrado"));

        trabalhador.setNome(dto.getNome());
        trabalhador.setEmail(dto.getEmail());
        trabalhador.setCpf(dto.getCpf());

        if (dto.getHabilidadesIds() != null && !dto.getHabilidadesIds().isEmpty()) {
            Set<Habilidade> habilidades = new HashSet<>(
                    habilidadeRepository.findAllById(dto.getHabilidadesIds())
            );
            trabalhador.setHabilidades(habilidades);
        }

        Trabalhador atualizado = trabalhadorRepository.save(trabalhador);
        log.info("Trabalhador atualizado com sucesso: ID {}", atualizado.getIdTrabalhador());

        return converterParaDTO(atualizado);
    }

    public void deletar(Long id) {
        log.info("Deletando trabalhador com ID: {}", id);
        if (!trabalhadorRepository.existsById(id)) {
            throw new RuntimeException("Trabalhador não encontrado");
        }
        trabalhadorRepository.deleteById(id);
        log.info("Trabalhador deletado com sucesso: ID {}", id);
    }

    public List<TrabalhadorDTO> listarAtivos() {
        log.info("Listando trabalhadores ativos");
        return trabalhadorRepository.findAllAtivos().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    private TrabalhadorDTO converterParaDTO(Trabalhador trabalhador) {
        TrabalhadorDTO dto = new TrabalhadorDTO();
        dto.setIdTrabalhador(trabalhador.getIdTrabalhador());
        dto.setNome(trabalhador.getNome());
        dto.setEmail(trabalhador.getEmail());
        dto.setCpf(trabalhador.getCpf());
        dto.setDtCadastro(trabalhador.getDtCadastro());
        dto.setStatusConta(trabalhador.getStatusConta());
        
        if (trabalhador.getHabilidades() != null) {
            dto.setHabilidadesIds(
                    trabalhador.getHabilidades().stream()
                            .map(Habilidade::getIdHabilidade)
                            .collect(Collectors.toSet())
            );
        }

        return dto;
    }
}
