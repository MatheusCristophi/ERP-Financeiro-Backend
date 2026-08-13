package com.finance.manager.entidades;

import com.finance.manager.enums.LancamentoTipo;
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
    private UUID id;

    @Column(name = "categoria_descricao", nullable = false)
    private String descricao;

    @Column(name = "categoria_tipo", nullable = false)
    private LancamentoTipo tipo;

    @OneToMany(mappedBy = "lancamentoCategoria", fetch = FetchType.LAZY)
    private List<Lancamento> lancamentos;

    @Column(name = "categoria_status", nullable = false)
    private boolean status = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Empresas categoriaEmpresa;
}