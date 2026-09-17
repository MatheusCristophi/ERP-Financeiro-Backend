package com.finance.manager.empresa;

import com.finance.manager.categoria.Categoria;
import com.finance.manager.conta.Conta;
import com.finance.manager.lancamento.Lancamento;
import com.finance.manager.usuario.Usuario;
import com.finance.manager.usuarioempresa.UsuarioEmpresa;
import com.finance.manager.pessoa.Pessoa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "empresa_tabela")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Empresas {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "empresa_id")
    private UUID empresaId;

    @Column(name = "empresa_cnpj" ,nullable = false, unique = true)
    private String cnpj;

    @Column(name = "empresa_descricao", nullable = false)
    private String descricao;


    @Column(name = "empresa_tipo", nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write = "?::empresa_tipo_atividade")
    private EmpresaTiposAtividade empresaTipo;


    @Column(name = "empresa_regime", nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write = "?::empresa_regime_tibutario")
    private EmpresaRegimeTributario empresaRegime;


    @Column(name = "empresa_natureza_pessoa", nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write = "?::empresa_natureza")
    private EmpresaNaturezaPessoa empresaNaturezaPessoa;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_dono",
            nullable = false,
            updatable = false
    )
    private Usuario empresaDono;


    @OneToMany(mappedBy = "contaEmpresa",
               fetch = FetchType.LAZY,
               cascade = CascadeType.ALL,
               orphanRemoval = true
    )
    private List<Conta> empresaContas;

    @OneToMany(mappedBy = "pessoaEmpresa", fetch = FetchType.LAZY)
    private List<Pessoa> empresaPessoa;

    @OneToMany(mappedBy = "categoriaEmpresa",
               fetch = FetchType.LAZY,
               cascade = CascadeType.ALL,
               orphanRemoval = true
    )
    private List<Categoria> empresaCategorias;

    @OneToMany(mappedBy = "lancamentoEmpresa",
               fetch = FetchType.LAZY
    )
    private List<Lancamento> empresaLancamentos;

    @OneToMany(mappedBy = "empresaId",
               fetch = FetchType.LAZY,
               cascade = CascadeType.ALL,
               orphanRemoval = true
    )
    private List<UsuarioEmpresa> empresaFuncionarios;

    @Column(name = "empresa_status", nullable = false)
    private boolean status = true;
}