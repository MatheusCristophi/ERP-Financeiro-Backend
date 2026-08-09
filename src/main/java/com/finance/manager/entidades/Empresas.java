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

@Entity()
@Table(name = "empresa_tabela")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Empresas {
    @Id()
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "empresa_id")
    private String id;


    @Column(name = "empresa_descricao",
            nullable = false,
            unique = true
    )
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
    @JoinColumn(name = "empresa_id",
            nullable = false,
            updatable = false)
    private Usuario empresaDono;


    @ManyToMany(mappedBy = "empresa", fetch = FetchType.LAZY)
    @JoinColumn(name = "empresaUsuarios")
    private List<Usuario> empresaUsuarios;
}
