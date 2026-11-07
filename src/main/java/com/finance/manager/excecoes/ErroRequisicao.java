package com.finance.manager.excecoes;

public class ErroRequisicao extends RuntimeException {
    public ErroRequisicao(String message) {
        super(message);
    }
}
