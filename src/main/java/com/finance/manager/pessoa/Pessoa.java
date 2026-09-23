package com.finance.manager.pessoa;

import com.finance.manager.empresa.Empresas;
import com.finance.manager.lancamento.Lancamento;
import com.finance.manager.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pessoa_tabela",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_pessoa_cpf_empresa", columnNames = {"pessoa_cpf", "pessoa_empresa"}),
                @UniqueConstraint(name = "uk_pessoa_cnpj_empresa", columnNames = {"pessoa_cnpj", "pessoa_empresa"})
        })
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

    @Column(name = "pessoa_cpf", unique = true)
    private String cpf;

    @Column(name = "pessoa_cnpj", unique = true)
    private String cnpj;

    @Column(name = "pessoa_tipo", nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write = "?::pessoa_tipo")
    private PessoaTipo tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_empresa",
            nullable = false,
            updatable = false
    )
    private Empresas pessoaEmpresa;

    @OneToMany(mappedBy = "lancamentoPessoa", fetch = FetchType.LAZY)
    private List<Lancamento> pessoaLancamentos;

    @OneToMany(mappedBy = "usuarioPessoa", fetch = FetchType.LAZY)
    private List<Usuario> pessoaUsuarios = new ArrayList<>();

    @Column(name = "pessoa_status", nullable = false)
    private boolean status = true;
}
