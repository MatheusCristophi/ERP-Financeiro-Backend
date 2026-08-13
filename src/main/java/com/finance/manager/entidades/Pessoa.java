package com.finance.manager.entidades;

import com.finance.manager.enums.PessoaTipo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pessoa_tabela")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "pessoa_id")
    private UUID id;

    @Column(name = "pessoa_nome", nullable = false)
    private String nome;

    @Column(name = "pessoa_cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "pessoa_cnpj", nullable = false, unique = true)
    private String cnpj;

    @Column(name = "pessoa_tipo", nullable = false)
    @Enumerated(EnumType.STRING)
    private PessoaTipo tipo;

    @OneToMany(mappedBy = "lancamentoPessoa")
    private List<Lancamento> pessoaLancamentos;

    @OneToMany(mappedBy = "usuarioPessoa")
    private List<Usuario> pessoaUsuarios;
}
