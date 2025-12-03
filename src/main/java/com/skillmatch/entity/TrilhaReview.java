package com.skillmatch.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_TRILHA_REVIEW")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrilhaReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_review")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_trilha_detalhada", nullable = false)
    private TrilhaDetalhada trilhaDetalhada;

    @Column(name = "id_trabalhador", nullable = false)
    private Long idTrabalhador;

    @Column(name = "nota", nullable = false)
    private Integer nota; // 1 a 5

    @Column(name = "comentario", length = 1000)
    private String comentario;

    @Column(name = "data_review")
    private LocalDateTime dataReview;

    @PrePersist
    protected void onCreate() {
        if (dataReview == null) {
            dataReview = LocalDateTime.now();
        }
    }
}

