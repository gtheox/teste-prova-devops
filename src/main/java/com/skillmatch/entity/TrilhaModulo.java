package com.skillmatch.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TB_TRILHA_MODULO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrilhaModulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modulo")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_trilha_detalhada", nullable = false)
    private TrilhaDetalhada trilhaDetalhada;

    @Column(name = "titulo_modulo", nullable = false, length = 200)
    private String tituloModulo;

    @Column(name = "duracao_horas")
    private Integer duracaoHoras;

    @Column(name = "ordem")
    private Integer ordem;

    @OneToMany(mappedBy = "modulo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TrilhaAula> aulas = new ArrayList<>();
}

