package com.finance.manager.categoria.dto;

import com.finance.manager.categoria.CategoriaTipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoriaRequisicao(

        @NotBlank(message = "A descrição é obrigatória")
        @Size(min = 2, max = 100, message = "A descrição deve ter entre 2 e 100 caracteres")
        String descricao,

        @NotNull(message = "O tipo da categoria é obrigatório")
        CategoriaTipo tipo,

        boolean status
) {
}