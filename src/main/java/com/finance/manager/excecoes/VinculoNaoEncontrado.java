package com.finance.manager.excecoes;

import java.util.UUID;

public class VinculoNaoEncontrado extends RuntimeException{
    public VinculoNaoEncontrado(UUID usuarioId, UUID empresaId){
        super("Vinculo não encontrado entre o usuário "+ usuarioId +" e a empresa "+empresaId);
    }
}
