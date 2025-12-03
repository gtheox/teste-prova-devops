package com.skillmatch.service;

import com.skillmatch.dto.TrabalhadorDTO;
import com.skillmatch.entity.Trabalhador;
import com.skillmatch.repository.HabilidadeRepository;
import com.skillmatch.repository.TrabalhadorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrabalhadorServiceTest {

    @Mock
    private TrabalhadorRepository trabalhadorRepository;

    @Mock
    private HabilidadeRepository habilidadeRepository;

    @InjectMocks
    private TrabalhadorService trabalhadorService;

    private TrabalhadorDTO trabalhadorDTO;
    private Trabalhador trabalhador;

    @BeforeEach 
    void setUp() {
        trabalhadorDTO = new TrabalhadorDTO();
        trabalhadorDTO.setNome("João Silva");
        trabalhadorDTO.setEmail("joao@example.com");
        trabalhadorDTO.setCpf("123.456.789-00");
        trabalhadorDTO.setStatusConta("ATIVA");

        trabalhador = new Trabalhador();
        trabalhador.setIdTrabalhador(1L);
        trabalhador.setNome("João Silva");
        trabalhador.setEmail("joao@example.com");
        trabalhador.setCpf("123.456.789-00");
        trabalhador.setDtCadastro(LocalDate.now());
        trabalhador.setStatusConta("ATIVA");
    }

    @Test
    void testCriarTrabalhador() {
        when(trabalhadorRepository.save(any(Trabalhador.class))).thenReturn(trabalhador);

        TrabalhadorDTO resultado = trabalhadorService.criar(trabalhadorDTO);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        assertEquals("joao@example.com", resultado.getEmail());
        verify(trabalhadorRepository, times(1)).save(any(Trabalhador.class));
    }

    @Test
    void testObterTrabalhadorPorId() {
        when(trabalhadorRepository.findById(1L)).thenReturn(Optional.of(trabalhador));

        TrabalhadorDTO resultado = trabalhadorService.obterPorId(1L);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        verify(trabalhadorRepository, times(1)).findById(1L);
    }

    @Test
    void testObterTrabalhadorPorIdNaoEncontrado() {
        when(trabalhadorRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> trabalhadorService.obterPorId(999L));
        verify(trabalhadorRepository, times(1)).findById(999L);
    }

    @Test
    void testDeletarTrabalhador() {
        when(trabalhadorRepository.existsById(1L)).thenReturn(true);

        trabalhadorService.deletar(1L);

        verify(trabalhadorRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletarTrabalhadorNaoEncontrado() {
        when(trabalhadorRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> trabalhadorService.deletar(999L));
        verify(trabalhadorRepository, never()).deleteById(any());
    }
}
