package com.skillmatch.controller;

import com.skillmatch.dto.VagaDTO;
import com.skillmatch.service.VagaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/vagas")
@Tag(name = "Vagas", description = "API para gerenciar vagas de emprego")
@Slf4j
public class VagaController {

    @Autowired
    private VagaService vagaService;

    @PostMapping
    @Operation(summary = "Criar nova vaga", description = "Cria um novo registro de vaga de emprego")
    public ResponseEntity<VagaDTO> criar(@Valid @RequestBody VagaDTO dto) {
        log.info("POST /v1/vagas - Criando vaga: {}", dto.getTitulo());
        VagaDTO criada = vagaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter vaga por ID", description = "Retorna os detalhes de uma vaga específica")
    public ResponseEntity<VagaDTO> obterPorId(@PathVariable Long id) {
        log.info("GET /v1/vagas/{} - Buscando vaga", id);
        VagaDTO dto = vagaService.obterPorId(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @Operation(summary = "Listar todas as vagas", description = "Retorna uma lista de todas as vagas cadastradas")
    public ResponseEntity<List<VagaDTO>> listarTodas() {
        log.info("GET /v1/vagas - Listando todas as vagas");
        List<VagaDTO> lista = vagaService.listarTodas();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/empresa/{idEmpresa}")
    @Operation(summary = "Listar vagas por empresa", description = "Retorna todas as vagas de uma empresa específica")
    public ResponseEntity<List<VagaDTO>> listarPorEmpresa(@PathVariable Long idEmpresa) {
        log.info("GET /v1/vagas/empresa/{} - Listando vagas da empresa", idEmpresa);
        List<VagaDTO> lista = vagaService.listarPorEmpresa(idEmpresa);
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar vaga", description = "Atualiza os dados de uma vaga existente")
    public ResponseEntity<VagaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody VagaDTO dto) {
        log.info("PUT /v1/vagas/{} - Atualizando vaga", id);
        VagaDTO atualizada = vagaService.atualizar(id, dto);
        return ResponseEntity.ok(atualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar vaga", description = "Remove uma vaga do sistema")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("DELETE /v1/vagas/{} - Deletando vaga", id);
        vagaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
