package com.skillmatch.service;

import com.skillmatch.dto.VagaDTO;
import com.skillmatch.entity.Empresa;
import com.skillmatch.entity.Habilidade;
import com.skillmatch.entity.Vaga;
import com.skillmatch.repository.EmpresaRepository;
import com.skillmatch.repository.HabilidadeRepository;
import com.skillmatch.repository.VagaRepository;
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
public class VagaService {

    @Autowired
    private VagaRepository vagaRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private HabilidadeRepository habilidadeRepository;

    public VagaDTO criar(VagaDTO dto) {
        log.info("Criando nova vaga: {}", dto.getTitulo());

        Empresa empresa = empresaRepository.findById(dto.getIdEmpresa())
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        Vaga vaga = new Vaga();
        vaga.setEmpresa(empresa);
        vaga.setTitulo(dto.getTitulo());
        vaga.setDescricao(dto.getDescricao());

        if (dto.getHabilidadesExigidas() != null && !dto.getHabilidadesExigidas().isEmpty()) {
            Set<Habilidade> habilidades = new HashSet<>(
                    habilidadeRepository.findAllById(dto.getHabilidadesExigidas())
            );
            vaga.setHabilidadesExigidas(habilidades);
        }

        Vaga salva = vagaRepository.save(vaga);
        log.info("Vaga criada com sucesso: ID {}", salva.getIdVaga());

        return converterParaDTO(salva);
    }

    public VagaDTO obterPorId(Long id) {
        log.info("Buscando vaga com ID: {}", id);
        Optional<Vaga> vaga = vagaRepository.findById(id);
        return vaga.map(this::converterParaDTO)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));
    }

    public List<VagaDTO> listarTodas() {
        log.info("Listando todas as vagas");
        return vagaRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public List<VagaDTO> listarPorEmpresa(Long idEmpresa) {
        log.info("Listando vagas da empresa: {}", idEmpresa);
        return vagaRepository.findByEmpresaIdEmpresa(idEmpresa).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public VagaDTO atualizar(Long id, VagaDTO dto) {
        log.info("Atualizando vaga com ID: {}", id);

        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));

        vaga.setTitulo(dto.getTitulo());
        vaga.setDescricao(dto.getDescricao());

        if (dto.getHabilidadesExigidas() != null && !dto.getHabilidadesExigidas().isEmpty()) {
            Set<Habilidade> habilidades = new HashSet<>(
                    habilidadeRepository.findAllById(dto.getHabilidadesExigidas())
            );
            vaga.setHabilidadesExigidas(habilidades);
        }

        Vaga atualizada = vagaRepository.save(vaga);
        log.info("Vaga atualizada com sucesso: ID {}", atualizada.getIdVaga());

        return converterParaDTO(atualizada);
    }

    public void deletar(Long id) {
        log.info("Deletando vaga com ID: {}", id);
        if (!vagaRepository.existsById(id)) {
            throw new RuntimeException("Vaga não encontrada");
        }
        vagaRepository.deleteById(id);
        log.info("Vaga deletada com sucesso: ID {}", id);
    }

    private VagaDTO converterParaDTO(Vaga vaga) {
        VagaDTO dto = new VagaDTO();
        dto.setIdVaga(vaga.getIdVaga());
        dto.setIdEmpresa(vaga.getEmpresa().getIdEmpresa());
        dto.setTitulo(vaga.getTitulo());
        dto.setDescricao(vaga.getDescricao());
        dto.setDtPublicacao(vaga.getDtPublicacao());
        
        if (vaga.getHabilidadesExigidas() != null) {
            dto.setHabilidadesExigidas(
                    vaga.getHabilidadesExigidas().stream()
                            .map(Habilidade::getIdHabilidade)
                            .collect(Collectors.toSet())
            );
        }

        return dto;
    }
}
