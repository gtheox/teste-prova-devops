package com.skillmatch.controller;

import com.skillmatch.dto.MatchResultadoDTO;
import com.skillmatch.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j; // <-- IMPORT NECESSÁRIO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/match")
@Tag(name = "Match", description = "API para calcular compatibilidade entre trabalhadores e vagas")
@Slf4j // <-- ADICIONADO PARA CORRIGIR O ERRO DO 'log'
public class MatchController {

    @Autowired
    private MatchService matchService;

    @GetMapping("/compatibilidade/{idTrabalhador}/{idVaga}")
    @Operation(summary = "Calcular compatibilidade", description = "Calcula o percentual de compatibilidade entre um trabalhador e uma vaga")
    public ResponseEntity<MatchResultadoDTO> calcularCompatibilidade(
            @PathVariable Long idTrabalhador,
            @PathVariable Long idVaga) {
        log.info("GET /v1/match/compatibilidade/{}/{} - Calculando compatibilidade", idTrabalhador, idVaga);
        MatchResultadoDTO resultado = matchService.calcularCompatibilidade(idTrabalhador, idVaga);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/vagas-compativeis/{idTrabalhador}")
    @Operation(summary = "Listar vagas compatíveis", description = "Lista todas as vagas compatíveis para um trabalhador específico")
    public ResponseEntity<List<MatchResultadoDTO>> listarVagasCompativeis(@PathVariable Long idTrabalhador) {
        log.info("GET /v1/match/vagas-compativeis/{} - Listando vagas compatíveis", idTrabalhador);
        List<MatchResultadoDTO> resultados = matchService.listarVagasCompativeis(idTrabalhador);
        return ResponseEntity.ok(resultados);
    }
}