package com.skillmatch.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_TRILHA_AULA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrilhaAula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aula")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_modulo", nullable = false)
    private TrilhaModulo modulo;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "url_video", length = 500)
    private String urlVideo;

    @Column(name = "ordem")
    private Integer ordem;
}

