package com.finance.manager.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "conta_tabela")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Conta {

    @Id()
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "conta_id")
    private UUID id;

    @Column(name = "conta_descricao",
            nullable = false,
            unique = true
    )
    private String descricao;

    @Column(name = "conta_numero", nullable = false, unique = true, length = 12)
    private int numero;

    @Column(name = "conta_agencia", nullable = false, length = 5)
    private int agencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_contas")
    private Empresas empresa;
}