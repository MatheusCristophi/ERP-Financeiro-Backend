package com.finance.manager.excecoes;

public class EmailJaExisteException extends RuntimeException {
    public EmailJaExisteException(String email) {
        super("O email: "+email+ " Já está sendo utilizado por outro usuário");
    }
}
