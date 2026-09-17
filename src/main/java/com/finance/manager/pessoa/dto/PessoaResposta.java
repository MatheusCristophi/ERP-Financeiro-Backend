package com.finance.manager.pessoa.dto;

import com.finance.manager.pessoa.Pessoa;
import com.finance.manager.pessoa.PessoaTipo;

import java.util.List;
import java.util.Objects;

public record PessoaResposta (
        String nome,
        String cpf,
        String cnpj,
        PessoaTipo tipo
) {
    public static PessoaResposta from(Pessoa pessoa) {
        return new PessoaResposta(
                pessoa.getNome(),
                Objects.toString(pessoa.getCpf(), ""),
                Objects.toString(pessoa.getCnpj(), ""),
                pessoa.getTipo()
        );
    }

    public static List<PessoaResposta> of(List<Pessoa> pessoas) {
        return pessoas
                .stream()
                .map(PessoaResposta::from)
                .toList();
    }
}
