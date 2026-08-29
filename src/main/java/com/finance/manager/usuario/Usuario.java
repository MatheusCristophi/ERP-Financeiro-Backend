package com.finance.manager.usuario;

import com.finance.manager.lancamento.Lancamento;
import com.finance.manager.pessoa.Pessoa;
import com.finance.manager.usuarioempresa.UsuarioEmpresa;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "usuario_tabela")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "usuario_id")
    private UUID usuarioId;

    @Column(name = "usuario_nome", nullable = false)
    private String nome;

    @Column(name = "usuario_email", unique = true, nullable = false)
    private String email;

    @Column(name = "usuario_senha", nullable = false)
    private String senha;

    @OneToMany(mappedBy = "lancamentoUsuario", fetch = FetchType.LAZY)
    private List<Lancamento> usuarioLancamentos;

    @Column(name = "usuario_ativo", nullable = false)
    private boolean ativo = true;

    @OneToMany(mappedBy = "usuarioId", fetch = FetchType.LAZY)
    private List<UsuarioEmpresa> usuarioEmpresas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_pessoas")
    private Pessoa usuarioPessoa;
}
