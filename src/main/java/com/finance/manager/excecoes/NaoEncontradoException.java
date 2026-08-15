package com.finance.manager.excecoes;

public class NaoEncontradoException extends RuntimeException {
    public NaoEncontradoException(String tipo) {
        super("Não foi possível buscar " + tipo);
    }
}
