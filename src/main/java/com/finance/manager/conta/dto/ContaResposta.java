package com.finance.manager.conta.dto;

import com.finance.manager.conta.Conta;
import com.finance.manager.conta.ContaTipo;
import com.finance.manager.conta.MovimentacaoTipo;

import java.util.List;
import java.util.UUID;

public record ContaResposta(
        UUID id,
        String descricao,
        ContaTipo contaTipo,
        MovimentacaoTipo movimentacaoTipo,
        String numero,
        String agencia,
        Boolean status
){

    public static ContaResposta from(Conta conta) {
        return new ContaResposta(
                conta.getId(),
                conta.getDescricao(),
                conta.getContaTipo(),
                conta.getContaMovimentacao(),
                conta.getNumero(),
                conta.getAgencia(),
                conta.isStatus()
        );
    }

    public static List<ContaResposta> of(List<Conta> contas){
        return contas
                .stream()
                .map(ContaResposta::from)
                .toList();
    }
}
