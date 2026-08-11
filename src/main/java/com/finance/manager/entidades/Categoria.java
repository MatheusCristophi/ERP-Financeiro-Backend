package com.finance.manager.entidades;

import com.finance.manager.enums.MovimentacaoTipo;
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
    private MovimentacaoTipo tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_usuario")
    private Usuario categoriaUsuario;

    @OneToMany(mappedBy = "lancamento_categoria", fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_lancamentos")
    private List<Lancamento> lancamentos;

    @Column(name = "categoria_status", nullable = false)
    private boolean status = true;
}