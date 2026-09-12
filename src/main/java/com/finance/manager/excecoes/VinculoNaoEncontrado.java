package com.finance.manager.excecoes;

public class VinculoNaoEncontrado extends RuntimeException{
    public VinculoNaoEncontrado(String usuario, String empresa){
        super("Vinculo não encontrado entre o usuário "+usuario+" e a empresa "+empresa);
    }
}
