package com.finance.manager.lancamento.dto;

import com.finance.manager.conta.MovimentacaoTipo;
import com.finance.manager.lancamento.Lancamento;
import com.finance.manager.lancamento.LancamentoStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record LancamentoResposta(UUID id,
                                 String descricao,
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
                lancamento.getId(),
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
