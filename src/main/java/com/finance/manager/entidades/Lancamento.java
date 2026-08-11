package com.finance.manager.entidades;

import com.finance.manager.enums.LancamentoStatus;
import com.finance.manager.enums.MovimentacaoTipo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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

    @Column(name = "lancamento_empresa", nullable = false)
    private Empresas empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_lancamentos")
    @Column(name = "lancamento_pessoa")
    private Pessoa pessoa;

    @Enumerated(EnumType.STRING)
    @Column(name = "lancamento_status", nullable = false)
    private LancamentoStatus status;

    @Column(name = "lancamento_valor", nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @OneToMany(mappedBy = "categoria_lancamentos", fetch = FetchType.LAZY)
    @Column(name = "lancamento_categoria", nullable = false)
    private Categoria categoria;

    @Column(name = "lancamento_movimentacao", nullable = false)
    private MovimentacaoTipo movimentacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_lancamentos")
    private Usuario lancamentoUsuario;

    @Column(name = "lancamento_data_emissao", nullable = false)
    private LocalDate dataEmissao;

    @Column(name = "lancamento_data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Column(name = "lancamento_data_pagamento", nullable = false)
    private LocalDate dataPagamento;

    @Column(name = "lancamento_observacao")
    private String observacao;
}
