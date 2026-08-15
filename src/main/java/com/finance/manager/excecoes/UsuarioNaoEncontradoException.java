package com.finance.manager.excecoes;

import java.util.UUID;

public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException(UUID id) {
        super("Usuário com Id: "+ id +" Não foi encontrado");
    }
}