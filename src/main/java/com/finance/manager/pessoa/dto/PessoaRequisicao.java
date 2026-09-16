package com.finance.manager.pessoa.dto;

import com.finance.manager.pessoa.PessoaTipo;

public record PessoaRequisicao(
        String nome,
        String cpf,
        String cnpj,
        PessoaTipo tipo
) {
}
