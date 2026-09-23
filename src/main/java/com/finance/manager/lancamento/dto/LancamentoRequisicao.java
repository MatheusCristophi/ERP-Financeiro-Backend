package com.finance.manager.lancamento.dto;

import com.finance.manager.conta.MovimentacaoTipo;
import com.finance.manager.lancamento.LancamentoStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record LancamentoRequisicao(

        @NotBlank(message = "A descrição é obrigatória")
        @Size(min = 2, max = 255, message = "A descrição deve ter entre 2 e 255 caracteres")
        String descricao,

        @NotNull(message = "O status é obrigatório")
        LancamentoStatus status,

        @NotNull(message = "O valor é obrigatório")
        @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
        BigDecimal valor,

        @NotNull(message = "A categoria é obrigatória")
        UUID categoria,

        @NotNull(message = "O tipo de movimentação é obrigatório")
        MovimentacaoTipo movimentacaoTipo,

        @NotNull(message = "A data de emissão é obrigatória")
        LocalDateTime dataEmissao,

        @NotNull(message = "A data de vencimento é obrigatória")
        LocalDateTime dataVencimento,

        LocalDateTime dataPagamento,

        @Size(max = 500, message = "A observação deve ter no máximo 500 caracteres")
        String observacao
) {
}