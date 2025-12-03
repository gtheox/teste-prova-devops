package com.skillmatch.service;

import com.skillmatch.dto.MatchResultadoDTO;
import com.skillmatch.entity.Empresa;
import com.skillmatch.entity.Habilidade;
import com.skillmatch.entity.Trabalhador;
import com.skillmatch.entity.Vaga;
import com.skillmatch.repository.TrabalhadorRepository;
import com.skillmatch.repository.VagaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock
    private TrabalhadorRepository trabalhadorRepository;

    @Mock
    private VagaRepository vagaRepository;

    @InjectMocks
    private MatchService matchService;

    private Trabalhador trabalhador;
    private Vaga vaga;
    private Empresa empresa;
    private Habilidade habilidade1;
    private Habilidade habilidade2;

    @BeforeEach
    void setUp() {
        // Criar habilidades
        habilidade1 = new Habilidade();
        habilidade1.setIdHabilidade(1L);
        habilidade1.setNomeHabilidade("Java");
        habilidade1.setTipo("TECNICA");

        habilidade2 = new Habilidade();
        habilidade2.setIdHabilidade(2L);
        habilidade2.setNomeHabilidade("Spring Boot");
        habilidade2.setTipo("TECNICA");

        // Criar trabalhador
        trabalhador = new Trabalhador();
        trabalhador.setIdTrabalhador(1L);
        trabalhador.setNome("João Silva");
        trabalhador.setEmail("joao@example.com");
        trabalhador.setCpf("123.456.789-00");
        trabalhador.setDtCadastro(LocalDate.now());
        trabalhador.setStatusConta("ATIVA");
        
        Set<Habilidade> habilidades = new HashSet<>();
        habilidades.add(habilidade1);
        trabalhador.setHabilidades(habilidades);

        // Criar empresa
        empresa = new Empresa();
        empresa.setIdEmpresa(1L);
        empresa.setNomeEmpresa("Tech Company");
        empresa.setCnpj("12.345.678/0001-00");
        empresa.setSetor("Tecnologia");

        // Criar vaga
        vaga = new Vaga();
        vaga.setIdVaga(1L);
        vaga.setEmpresa(empresa);
        vaga.setTitulo("Desenvolvedor Java");
        vaga.setDescricao("Procuramos um desenvolvedor Java experiente");
        vaga.setDtPublicacao(LocalDate.now());
        
        Set<Habilidade> habilidadesExigidas = new HashSet<>();
        habilidadesExigidas.add(habilidade1);
        habilidadesExigidas.add(habilidade2);
        vaga.setHabilidadesExigidas(habilidadesExigidas);
    }

    @Test
    void testCalcularCompatibilidadeAlta() {
        when(trabalhadorRepository.findById(1L)).thenReturn(Optional.of(trabalhador));
        when(vagaRepository.findById(1L)).thenReturn(Optional.of(vaga));

        MatchResultadoDTO resultado = matchService.calcularCompatibilidade(1L, 1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdTrabalhador());
        assertEquals(1L, resultado.getIdVaga());
        assertEquals(50.0, resultado.getPercentualCompatibilidade());
        assertEquals("PARCIALMENTE_COMPATIVEL", resultado.getStatusMatch());
    }

    @Test
    void testCalcularCompatibilidadeTrabalhadorNaoEncontrado() {
        when(trabalhadorRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> matchService.calcularCompatibilidade(999L, 1L));
    }

    @Test
    void testCalcularCompatibilidadeVagaNaoEncontrada() {
        when(trabalhadorRepository.findById(1L)).thenReturn(Optional.of(trabalhador));
        when(vagaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> matchService.calcularCompatibilidade(1L, 999L));
    }
}
