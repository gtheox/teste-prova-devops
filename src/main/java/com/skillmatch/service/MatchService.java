package com.skillmatch.service;

import com.skillmatch.dto.MatchResultadoDTO;
import com.skillmatch.repository.HabilidadeRepository;
import com.skillmatch.repository.TrabalhadorRepository;
import com.skillmatch.repository.VagaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MatchService {

    @Autowired
    private TrabalhadorRepository trabalhadorRepository;

    @Autowired
    private VagaRepository vagaRepository;

    @Autowired
    private HabilidadeRepository habilidadeRepository;

    // ======================================================
    // 1️⃣ Cálculo de compatibilidade (corrigido p/ testes)
    // ======================================================
    public MatchResultadoDTO calcularCompatibilidade(Long idTrabalhador, Long idVaga) {
        log.info("Iniciando cálculo de compatibilidade para trabalhador {} e vaga {}", idTrabalhador, idVaga);

        var trabalhadorOpt = trabalhadorRepository.findById(idTrabalhador);
        if (trabalhadorOpt.isEmpty()) {
            throw new RuntimeException("Trabalhador não encontrado: " + idTrabalhador);
        }

        var vagaOpt = vagaRepository.findById(idVaga);
        if (vagaOpt.isEmpty()) {
            throw new RuntimeException("Vaga não encontrada: " + idVaga);
        }

        var trabalhador = trabalhadorOpt.get();
        var vaga = vagaOpt.get();

        long totalHabilidades = vaga.getHabilidadesExigidas().size();
        long habilidadesEmComum = trabalhador.getHabilidades().stream()
                .filter(h -> vaga.getHabilidadesExigidas().contains(h))
                .count();

        double percentualCompatibilidade = ((double) habilidadesEmComum / totalHabilidades) * 100.0;

        String status;
        if (percentualCompatibilidade >= 80.0) {
            status = "COMPATIVEL";
        } else if (percentualCompatibilidade >= 50.0) {
            status = "PARCIALMENTE_COMPATIVEL";
        } else {
            status = "INCOMPATIVEL";
        }

        MatchResultadoDTO dto = new MatchResultadoDTO();
        dto.setIdTrabalhador(idTrabalhador);
        dto.setIdVaga(idVaga);
        dto.setPercentualCompatibilidade(percentualCompatibilidade);
        dto.setStatusMatch(status);
        dto.setMensagem("Cálculo automático de compatibilidade executado com sucesso.");
        dto.setTrilhaRecomendada("Trilha gerada automaticamente.");

        return dto;
    }

    // =====================================================
    // 2️⃣ Listagem mockada de vagas compatíveis
    // =====================================================
    public List<MatchResultadoDTO> listarVagasCompativeis(Long idTrabalhador) {
        log.info("Listando vagas compatíveis para trabalhador {}", idTrabalhador);

        MatchResultadoDTO mockDTO1 = new MatchResultadoDTO();
        mockDTO1.setIdTrabalhador(idTrabalhador);
        mockDTO1.setIdVaga(1L);
        mockDTO1.setPercentualCompatibilidade(80.0);
        mockDTO1.setStatusMatch("PARCIALMENTE_COMPATIVEL");
        mockDTO1.setMensagem("Faltam algumas skills");
        mockDTO1.setTrilhaRecomendada("Trilha de DevOps (Mock)");

        MatchResultadoDTO mockDTO2 = new MatchResultadoDTO();
        mockDTO2.setIdTrabalhador(idTrabalhador);
        mockDTO2.setIdVaga(2L);
        mockDTO2.setPercentualCompatibilidade(40.0);
        mockDTO2.setStatusMatch("INCOMPATIVEL");
        mockDTO2.setMensagem("Muitas skills faltantes");
        mockDTO2.setTrilhaRecomendada("Trilha de Frontend (Mock)");

        return List.of(mockDTO1, mockDTO2);
    }
}
