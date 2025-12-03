package com.skillmatch.controller;

import com.skillmatch.dto.TrabalhadorDTO;
import com.skillmatch.service.TrabalhadorService;
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
@RequestMapping("/v1/trabalhadores")
@Tag(name = "Trabalhadores", description = "API para gerenciar trabalhadores")
@Slf4j
public class TrabalhadorController {

    @Autowired
    private TrabalhadorService trabalhadorService;

    @PostMapping
    @Operation(summary = "Criar novo trabalhador", description = "Cria um novo registro de trabalhador")
    public ResponseEntity<TrabalhadorDTO> criar(@Valid @RequestBody TrabalhadorDTO dto) {
        log.info("POST /v1/trabalhadores - Criando trabalhador: {}", dto.getNome());
        TrabalhadorDTO criado = trabalhadorService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter trabalhador por ID", description = "Retorna os detalhes de um trabalhador específico")
    public ResponseEntity<TrabalhadorDTO> obterPorId(@PathVariable Long id) {
        log.info("GET /v1/trabalhadores/{} - Buscando trabalhador", id);
        TrabalhadorDTO dto = trabalhadorService.obterPorId(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @Operation(summary = "Listar todos os trabalhadores", description = "Retorna uma lista de todos os trabalhadores cadastrados")
    public ResponseEntity<List<TrabalhadorDTO>> listarTodos() {
        log.info("GET /v1/trabalhadores - Listando todos os trabalhadores");
        List<TrabalhadorDTO> lista = trabalhadorService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar trabalhadores ativos", description = "Retorna uma lista de trabalhadores com status ATIVA")
    public ResponseEntity<List<TrabalhadorDTO>> listarAtivos() {
        log.info("GET /v1/trabalhadores/ativos - Listando trabalhadores ativos");
        List<TrabalhadorDTO> lista = trabalhadorService.listarAtivos();
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar trabalhador", description = "Atualiza os dados de um trabalhador existente")
    public ResponseEntity<TrabalhadorDTO> atualizar(@PathVariable Long id, @Valid @RequestBody TrabalhadorDTO dto) {
        log.info("PUT /v1/trabalhadores/{} - Atualizando trabalhador", id);
        TrabalhadorDTO atualizado = trabalhadorService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar trabalhador", description = "Remove um trabalhador do sistema")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("DELETE /v1/trabalhadores/{} - Deletando trabalhador", id);
        trabalhadorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
