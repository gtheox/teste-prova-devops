package com.skillmatch.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "VAGA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vaga {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_vaga")
    @SequenceGenerator(name = "seq_vaga", sequenceName = "SEQ_VAGA", allocationSize = 1)
    @Column(name = "ID_VAGA")
    private Long idVaga;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EMPRESA", nullable = false)
    private Empresa empresa;

    @NotBlank(message = "Título da vaga é obrigatório")
    @Column(name = "TITULO", nullable = false, length = 100)
    private String titulo;

    @NotBlank(message = "Descrição é obrigatória")
    @Column(name = "DESCRICAO", nullable = false, length = 4000)
    private String descricao;

    @Column(name = "DT_PUBLICACAO", nullable = false)
    private LocalDate dtPublicacao = LocalDate.now();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "VAGA_HABILIDADE",
            joinColumns = @JoinColumn(name = "ID_VAGA"),
            inverseJoinColumns = @JoinColumn(name = "ID_HABILIDADE")
    )
    private Set<Habilidade> habilidadesExigidas = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        if (dtPublicacao == null) {
            dtPublicacao = LocalDate.now();
        }
    }
}
