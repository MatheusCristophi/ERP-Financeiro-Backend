package com.finance.manager.controladores.usuariodtos;

import com.finance.manager.entidades.Usuario;

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
