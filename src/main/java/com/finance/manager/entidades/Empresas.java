package com.finance.manager.entidades;

import com.finance.manager.enums.EmpresaNaturezaPessoa;
import com.finance.manager.enums.EmpresaRegimeTributario;
import com.finance.manager.enums.EmpresaTiposAtividade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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


    @Column(name = "empresa_descricao", nullable = false)
    private String descricao;


    @Column(name = "empresa_tipo", nullable = false)
    @Enumerated(EnumType.STRING)
    private EmpresaTiposAtividade empresaTipo;


    @Column(name = "empresa_regime", nullable = false)
    @Enumerated(EnumType.STRING)
    private EmpresaRegimeTributario empresaRegime;


    @Column(name = "empresa_natureza_pessoa", nullable = false)
    @Enumerated(EnumType.STRING)
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
}