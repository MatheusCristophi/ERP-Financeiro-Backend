package com.finance.manager.usuario.dto;

import com.finance.manager.usuario.Usuario;

import java.util.List;

public record UsuarioResposta(
        String nome,
        String email,
        boolean ativo
) {

    public static UsuarioResposta from(Usuario usuario) {
        return new UsuarioResposta(
            usuario.getNome(),
            usuario.getEmail(),
            usuario.isAtivo()
        );
    }

    public static List<UsuarioResposta> of(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(UsuarioResposta::from)
                .toList();
    }
}
