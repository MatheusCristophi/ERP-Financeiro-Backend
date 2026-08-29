package com.finance.manager.empresa.dto;

import com.finance.manager.empresa.Empresas;
import com.finance.manager.empresa.EmpresaNaturezaPessoa;
import com.finance.manager.empresa.EmpresaRegimeTributario;
import com.finance.manager.empresa.EmpresaTiposAtividade;

import java.util.List;

public record EmpresaResposta(
        String descricao,
        EmpresaTiposAtividade empresaTiposAtividade,
        EmpresaRegimeTributario empresaRegimeTributario,
        EmpresaNaturezaPessoa empresaNaturezaPessoa,
        Boolean status
) {

    public static EmpresaResposta from(Empresas empresa){
        return new EmpresaResposta(
                empresa.getDescricao(),
                empresa.getEmpresaTipo(),
                empresa.getEmpresaRegime(),
                empresa.getEmpresaNaturezaPessoa(),
                empresa.isStatus()
        );
    }

    public static List<EmpresaResposta> of(List<Empresas> empresas) {
        return empresas.stream()
                .map(EmpresaResposta::from)
                .toList();
    }
}
