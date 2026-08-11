package com.finance.manager.entidades;

import com.finance.manager.enums.UsuarioRoles;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "empresa_usuario_tabela")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioEmpresa {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "empresa_usuario_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuarioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresas empresaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "usuario_role", nullable = false)
    private UsuarioRoles role;
}
