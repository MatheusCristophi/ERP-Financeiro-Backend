package com.finance.manager.categoria.dto;

import com.finance.manager.categoria.CategoriaTipo;

public record CategoriaRequisicao(String descricao,
                                  CategoriaTipo tipo,
                                  boolean status){
}
