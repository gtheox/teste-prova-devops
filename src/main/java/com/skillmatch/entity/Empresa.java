package com.skillmatch.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "EMPRESA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_empresa")
    @SequenceGenerator(name = "seq_empresa", sequenceName = "SEQ_EMPRESA", allocationSize = 1)
    @Column(name = "ID_EMPRESA")
    private Long idEmpresa;

    @NotBlank(message = "Nome da empresa é obrigatório")
    @Column(name = "NOME_EMPRESA", nullable = false, length = 100)
    private String nomeEmpresa;

    @NotBlank(message = "CNPJ é obrigatório")
    @Column(name = "CNPJ", nullable = false, unique = true, length = 18)
    private String cnpj;

    @NotBlank(message = "Setor é obrigatório")
    @Column(name = "SETOR", nullable = false, length = 50)
    private String setor;

    @OneToMany(mappedBy = "empresa", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Vaga> vagas = new HashSet<>();
}
