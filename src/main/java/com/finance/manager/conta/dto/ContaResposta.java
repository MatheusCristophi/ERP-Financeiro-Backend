package com.finance.manager.conta.dto;

import com.finance.manager.conta.Conta;
import com.finance.manager.conta.ContaTipo;
import com.finance.manager.conta.MovimentacaoTipo;

import java.util.List;

public record ContaResposta(
        String descricao,
        ContaTipo contaTipo,
        MovimentacaoTipo movimentacaoTipo,
        String numero,
        String agencia
){

    public static ContaResposta from(Conta conta) {
        return new ContaResposta(
                conta.getDescricao(),
                conta.getContaTipo(),
                conta.getContaMovimentacao(),
                conta.getNumero(),
                conta.getAgencia()
        );
    }

    public static List<ContaResposta> of(List<Conta> contas){
        return contas
                .stream()
                .map(ContaResposta::from)
                .toList();
    }
}
