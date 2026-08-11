package com.finance.manager.entidades;

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
    private UUID id;

    @Column(name = "usuario_nome", unique = true, nullable = false)
    private String nome;

    @Column(name = "usuario_email", unique = true, nullable = false)
    private String email;

    @Column(name = "usuario_senha", nullable = false)
    private String senha;

    @OneToMany(mappedBy = "lancamento_usuario", fetch = FetchType.LAZY)
    @Column(name = "usuario_lancamentos")
    private List<Lancamento> usuarioLancamentos;

    @Column(name = "usuario_ativo", nullable = false)
    private boolean ativo = true;

    @OneToMany(mappedBy = "", fetch = FetchType.LAZY)
    @Column(name = "usuario_id")
    private List<Empresas> empresasId;
}
