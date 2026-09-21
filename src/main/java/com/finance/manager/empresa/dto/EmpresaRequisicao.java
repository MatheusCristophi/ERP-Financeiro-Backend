package com.finance.manager.empresa.dto;

import com.finance.manager.empresa.EmpresaNaturezaPessoa;
import com.finance.manager.empresa.EmpresaRegimeTributario;
import com.finance.manager.empresa.EmpresaTiposAtividade;

public record EmpresaRequisicao(
        String descricao,
        String cnpj,
        EmpresaTiposAtividade empresaTiposAtividade,
        EmpresaRegimeTributario empresaRegimeTributario,
        EmpresaNaturezaPessoa empresaNaturezaPessoa
) {
}
