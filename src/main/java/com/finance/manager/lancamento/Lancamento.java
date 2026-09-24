package com.finance.manager.lancamento;

import com.finance.manager.empresa.Empresas;
import com.finance.manager.categoria.Categoria;
import com.finance.manager.pessoa.Pessoa;
import com.finance.manager.conta.MovimentacaoTipo;
import com.finance.manager.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "lancamento_tabela")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Lancamento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "lancamento_id")
    private UUID id;

    @Column(name = "lancamento_descricao", nullable = false)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lancamento_empresa")
    private Empresas lancamentoEmpresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lancamento_pessoa")
    private Pessoa lancamentoPessoa;

    @Enumerated(EnumType.STRING)
    @Column(name = "lancamento_status", nullable = false)
    @ColumnTransformer(write = "?::lancamento_status")
    private LancamentoStatus status;

    @Column(name = "lancamento_valor", nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lancamento_categoria")
    private Categoria lancamentoCategoria;

    @Column(name = "lancamento_movimentacao", nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write = "?::conta_movimentacao")
    private MovimentacaoTipo movimentacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lancamento_usuario")
    private Usuario lancamentoUsuario;

    @Column(name = "lancamento_data_emissao", nullable = false)
    private LocalDateTime dataEmissao;

    @Column(name = "lancamento_data_vencimento", nullable = false)
    private LocalDateTime dataVencimento;

    @Column(name = "lancamento_data_pagamento")
    private LocalDateTime dataPagamento;

    @Column(name = "lancamento_observacao")
    private String observacao;
}
