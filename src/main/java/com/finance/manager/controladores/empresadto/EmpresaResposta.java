package com.finance.manager.controladores.empresadto;

import com.finance.manager.entidades.Empresas;
import com.finance.manager.enums.EmpresaNaturezaPessoa;
import com.finance.manager.enums.EmpresaRegimeTributario;
import com.finance.manager.enums.EmpresaTiposAtividade;

import java.util.List;
import java.util.UUID;

public record EmpresaResposta(
        String descricao,
        EmpresaTiposAtividade empresaTiposAtividade,
        EmpresaRegimeTributario empresaRegimeTributario,
        EmpresaNaturezaPessoa empresaNaturezaPessoa
) {

    public static EmpresaResposta from(Empresas empresa){
        return new EmpresaResposta(
                empresa.getDescricao(),
                empresa.getEmpresaTipo(),
                empresa.getEmpresaRegime(),
                empresa.getEmpresaNaturezaPessoa()
        );
    }

    public static List<EmpresaResposta> of(List<Empresas> empresas) {
        return empresas.stream()
                .map(EmpresaResposta::from)
                .toList();
    }
}
