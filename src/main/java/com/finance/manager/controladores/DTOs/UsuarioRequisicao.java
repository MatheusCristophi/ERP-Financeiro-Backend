package com.finance.manager.controladores.DTOs;

import com.finance.manager.enums.UsuarioRoles;

public record UsuarioRequisicao(
        String nome,
        String email,
        String senha,
        UsuarioRoles role
) {
}
