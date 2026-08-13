package com.finance.manager.entidades;

import com.finance.manager.enums.ContaTipo;
import com.finance.manager.enums.MovimentacaoTipo;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "conta_tabela")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "conta_id")
    private UUID id;

    @Column(name = "conta_descricao",
            nullable = false,
            unique = true
    )
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "conta_tipo", nullable = false)
    private ContaTipo contaTipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "conta_movimentacao", nullable = false)
    private MovimentacaoTipo contaMovimentacao;

    @Column(name = "conta_numero", nullable = false, unique = true, length = 12)
    private String numero;

    @Column(name = "conta_agencia", nullable = false, length = 5)
    private String agencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresaContas")
    private Empresas contaEmpresa;
}