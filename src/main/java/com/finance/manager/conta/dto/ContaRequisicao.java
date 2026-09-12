package com.finance.manager.conta.dto;

import com.finance.manager.conta.ContaTipo;
import com.finance.manager.conta.MovimentacaoTipo;

public record ContaRequisicao(
        String descricao,
        ContaTipo contaTipo,
        MovimentacaoTipo movimentacaoTipo,
        String numero,
        String agencia
) {
}
