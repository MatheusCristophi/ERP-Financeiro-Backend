package com.finance.manager.controladores.empresadto;

import com.finance.manager.enums.EmpresaNaturezaPessoa;
import com.finance.manager.enums.EmpresaRegimeTributario;
import com.finance.manager.enums.EmpresaTiposAtividade;

import java.util.UUID;

public record EmpresaRequisicao(
        String descricao,
        EmpresaTiposAtividade empresaTiposAtividade,
        EmpresaRegimeTributario empresaRegimeTributario,
        EmpresaNaturezaPessoa empresaNaturezaPessoa,
        UUID empresaDono
) {
}
