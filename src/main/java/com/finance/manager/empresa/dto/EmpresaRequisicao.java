package com.finance.manager.empresa.dto;

import com.finance.manager.empresa.EmpresaNaturezaPessoa;
import com.finance.manager.empresa.EmpresaRegimeTributario;
import com.finance.manager.empresa.EmpresaTiposAtividade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmpresaRequisicao(

        @NotBlank(message = "A descrição é obrigatória")
        @Size(min = 2, max = 255, message = "A descrição deve ter entre 2 e 255 caracteres")
        String descricao,

        @NotBlank(message = "O CNPJ é obrigatório")
        @Pattern(regexp = "^\\d{14}$",
                message = "CNPJ deve conter exatamente 14 dígitos numéricos")
        String cnpj,

        @NotNull(message = "O tipo de atividade é obrigatório")
        EmpresaTiposAtividade empresaTiposAtividade,

        @NotNull(message = "O regime tributário é obrigatório")
        EmpresaRegimeTributario empresaRegimeTributario,

        @NotNull(message = "A natureza da pessoa é obrigatória")
        EmpresaNaturezaPessoa empresaNaturezaPessoa
) {
}