package com.finance.manager.usuarioempresa;

import com.finance.manager.empresa.Empresas;
import com.finance.manager.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;

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
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresas empresa;

    @Enumerated(EnumType.STRING)
    @Column(name = "usuario_role", nullable = false)
    @ColumnTransformer(write = "?::usuarios_roles")
    private UsuarioRoles role;
}
