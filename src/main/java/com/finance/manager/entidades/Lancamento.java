package com.finance.manager.entidades;

import com.finance.manager.enums.LancamentoStatus;
import com.finance.manager.enums.MovimentacaoTipo;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Lancamento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "lancamento_id")
    private UUID id;

    @Column(name = "lancamento_descricao", nullable = false)
    private String descricao;

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

    @Column(name = "lancamento_usuario", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_lancamentos")
    private Usuario lancamentoUsuario;

    @Column(name = "lancamento_data_emissao", nullable = false)
    private LocalDate dataEmissao;

    @Column(name = "lancamento_data_pagamento", nullable = false)
    private LocalDate dataPagamento;

    @Column(name = "lancamento_observacao")
    private String observacao;
}
