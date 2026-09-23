package com.finance.manager.conta.dto;

import com.finance.manager.conta.ContaTipo;
import com.finance.manager.conta.MovimentacaoTipo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ContaRequisicao(

        @NotNull(message = "A descrição é obrigatória")
        @Size(min = 2, max = 255, message = "A descrição deve ter entre 2 e 255 caracteres")
        String descricao,

        @NotNull(message = "O tipo de conta é obrigatório")
        ContaTipo contaTipo,

        @NotNull(message = "O tipo de movimentação é obrigatório")
        MovimentacaoTipo movimentacaoTipo,

        @Pattern(regexp = "^\\d{1,20}(-\\d)?$",
                message = "Número da conta inválido")
        String numero,

        @Pattern(regexp = "^\\d{1,5}(-\\d)?$",
                message = "Número da agência inválido")
        String agencia
) {
}