package com.finance.manager.categoria.dto;

import com.finance.manager.categoria.Categoria;
import com.finance.manager.categoria.CategoriaTipo;

import java.util.List;
import java.util.UUID;

public record CategoriaResposta(String descricao,
                                CategoriaTipo tipo,
                                boolean status,
                                UUID empresaId){

    public static CategoriaResposta from(Categoria categoria) {
        return new CategoriaResposta(
                categoria.getDescricao(),
                categoria.getTipo(),
                categoria.isStatus(),
                categoria.getCategoriaEmpresa().getId()
        );
    }

    public static List<CategoriaResposta> of(List<Categoria> categorias) {
        return categorias.stream()
                .map(CategoriaResposta::from)
                .toList();
    }
}
