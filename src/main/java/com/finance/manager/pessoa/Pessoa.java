package com.finance.manager.pessoa;

import com.finance.manager.lancamento.Lancamento;
import com.finance.manager.usuario.Usuario;
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
    private UUID pessoaId;

    @Column(name = "pessoa_nome", nullable = false)
    private String nome;

    @Column(name = "pessoa_cpf", unique = true)
    private String cpf;

    @Column(name = "pessoa_cnpj", unique = true)
    private String cnpj;

    @Column(name = "pessoa_tipo", nullable = false)
    @Enumerated(EnumType.STRING)
    private PessoaTipo tipo;

    @OneToMany(mappedBy = "lancamentoPessoa", fetch = FetchType.LAZY)
    private List<Lancamento> pessoaLancamentos;

    @OneToMany(mappedBy = "usuarioPessoa", fetch = FetchType.LAZY)
    private List<Usuario> pessoaUsuarios;
}
