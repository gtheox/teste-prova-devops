package com.skillmatch.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "HABILIDADE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Habilidade {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_habilidade")
    @SequenceGenerator(name = "seq_habilidade", sequenceName = "SEQ_HABILIDADE", allocationSize = 1)
    @Column(name = "ID_HABILIDADE")
    private Long idHabilidade;

    @NotBlank(message = "Nome da habilidade é obrigatório")
    @Column(name = "NOME_HABILIDADE", nullable = false, unique = true, length = 50)
    private String nomeHabilidade;

    @NotBlank(message = "Tipo de habilidade é obrigatório")
    @Column(name = "TIPO", nullable = false, length = 20)
    private String tipo; // TECNICA ou COMPORTAMENTAL
}
