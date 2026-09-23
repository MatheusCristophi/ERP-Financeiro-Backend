package com.finance.manager.pessoa.dto;

import com.finance.manager.pessoa.PessoaTipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PessoaRequisicao(

        @NotBlank(message = "O nome é obrigatório")
        @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$",
                message = "O nome deve conter apenas letras")
        String nome,

        @Pattern(regexp = "^\\d{11}$",
                message = "CPF deve conter exatamente 11 dígitos numéricos")
        String cpf,

        @Pattern(regexp = "^\\d{14}$",
                message = "CNPJ deve conter exatamente 14 dígitos numéricos")
        String cnpj,

        @NotNull(message = "O tipo de pessoa é obrigatório")
        PessoaTipo tipo
) {
}