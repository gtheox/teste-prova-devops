package com.skillmatch.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TRILHA_APRENDIZADO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrilhaAprendizado {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_trilha")
    @SequenceGenerator(name = "seq_trilha", sequenceName = "SEQ_TRILHA", allocationSize = 1)
    @Column(name = "ID_TRILHA")
    private Long idTrilha;

    @NotBlank(message = "Título da trilha é obrigatório")
    @Column(name = "TITULO_TRILHA", nullable = false, length = 100)
    private String tituloTrilha;

    @Positive(message = "Carga horária deve ser positiva")
    @Column(name = "CARGA_HORARIA", nullable = false)
    private Integer cargaHoraria;

    @Column(name = "URL_CURSO", length = 200)
    private String urlCurso;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "TRILHA_HABILIDADE",
            joinColumns = @JoinColumn(name = "ID_TRILHA"),
            inverseJoinColumns = @JoinColumn(name = "ID_HABILIDADE")
    )
    private Set<Habilidade> habilidades = new HashSet<>();
}
