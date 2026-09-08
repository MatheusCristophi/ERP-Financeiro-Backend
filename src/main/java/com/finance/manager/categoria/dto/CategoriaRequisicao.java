package com.finance.manager.categoria.dto;

import com.finance.manager.lancamento.LancamentoTipo;

public record CategoriaRequisicao(String descricao,
                                  LancamentoTipo Tipo,
                                  boolean status){
}
