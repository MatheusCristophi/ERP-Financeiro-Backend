package com.finance.manager.excecoes;

public class SemPermissaoException extends RuntimeException {
    public SemPermissaoException(String usuario) {
        super("O usuário: " +usuario+ " não possuí permissão para isso");
    }
}
