package com.skillmatch.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TB_TRILHA_DETALHADA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrilhaDetalhada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_trilha_detalhada")
    private Long id;

    @Column(name = "id_trilha_relacional")
    private Long idTrilhaRelacional;

    @Column(name = "titulo_trilha", nullable = false, length = 200)
    private String tituloTrilha;

    @Column(name = "descricao_completa", columnDefinition = "NVARCHAR(MAX)")
    private String descricaoCompleta;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "status", length = 50)
    private String status; // Ativa, Inativa, Em Desenvolvimento

    @OneToMany(mappedBy = "trilhaDetalhada", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TrilhaModulo> modulos = new ArrayList<>();

    @OneToMany(mappedBy = "trilhaDetalhada", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TrilhaReview> reviews = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
        if (status == null) {
            status = "Ativa";
        }
    }
}

