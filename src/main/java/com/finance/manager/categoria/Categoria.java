package com.finance.manager.categoria;

import com.finance.manager.empresa.Empresas;
import com.finance.manager.lancamento.LancamentoTipo;
import com.finance.manager.lancamento.Lancamento;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "categoria_tabela")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "categoria_id")
    private UUID categoriaId;

    @Column(name = "categoria_descricao", nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria_tipo", nullable = false)
    private LancamentoTipo tipo;

    @OneToMany(mappedBy = "lancamentoCategoria", fetch = FetchType.LAZY)
    private List<Lancamento> categoriaLancamentos;

    @Column(name = "categoria_status", nullable = false)
    private boolean status = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_empresa", nullable = false)
    private Empresas categoriaEmpresa;
}