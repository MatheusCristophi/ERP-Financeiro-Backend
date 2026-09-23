package com.finance.manager.lancamento.dto;

import com.finance.manager.conta.MovimentacaoTipo;
import com.finance.manager.lancamento.Lancamento;
import com.finance.manager.lancamento.LancamentoStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record LancamentoResposta(String descricao,
                                 LancamentoStatus status,
                                 BigDecimal valor,
                                 String categoria,
                                 MovimentacaoTipo movimentacaoTipo,
                                 LocalDateTime dataEmissao,
                                 LocalDateTime dataVencimento,
                                 LocalDateTime dataPagamento,
                                 String observacao
                                 ) {

    public static LancamentoResposta from(Lancamento lancamento) {
        return  new LancamentoResposta(
                lancamento.getDescricao(),
                lancamento.getStatus(),
                lancamento.getValor(),
                lancamento.getLancamentoCategoria().getDescricao(),
                lancamento.getMovimentacao(),
                lancamento.getDataEmissao(),
                lancamento.getDataVencimento(),
                lancamento.getDataPagamento(),
                lancamento.getObservacao()
        );
    }

    public static List<LancamentoResposta> of(List<Lancamento> lancamentos) {
        return lancamentos
                .stream()
                .map(LancamentoResposta::from)
                .toList();
    }
}
