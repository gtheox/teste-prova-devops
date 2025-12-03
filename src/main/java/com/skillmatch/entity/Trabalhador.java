package com.skillmatch.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TRABALHADOR")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trabalhador {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_trabalhador")
    @SequenceGenerator(name = "seq_trabalhador", sequenceName = "SEQ_TRABALHADOR", allocationSize = 1)
    @Column(name = "ID_TRABALHADOR")
    private Long idTrabalhador;

    @NotBlank(message = "Nome é obrigatório")
    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email é obrigatório")
    @Column(name = "EMAIL", nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "CPF é obrigatório")
    @Column(name = "CPF", nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(name = "DT_CADASTRO", nullable = false)
    private LocalDate dtCadastro = LocalDate.now();

    @Column(name = "STATUS_CONTA", nullable = false, length = 10)
    private String statusConta = "ATIVA";

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "TRABALHADOR_HABILIDADE",
            joinColumns = @JoinColumn(name = "ID_TRABALHADOR"),
            inverseJoinColumns = @JoinColumn(name = "ID_HABILIDADE")
    )
    private Set<Habilidade> habilidades = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        if (dtCadastro == null) {
            dtCadastro = LocalDate.now();
        }
        if (statusConta == null) {
            statusConta = "ATIVA";
        }
    }
}
