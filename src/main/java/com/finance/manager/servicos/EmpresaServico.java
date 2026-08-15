package com.finance.manager.servicos;

import com.finance.manager.controladores.empresadto.EmpresaRequisicao;
import com.finance.manager.controladores.empresadto.EmpresaResposta;
import com.finance.manager.entidades.Empresas;
import com.finance.manager.entidades.Usuario;
import com.finance.manager.excecoes.UsuarioNaoEncontradoException;
import com.finance.manager.repositorios.EmpresaRepositorio;
import com.finance.manager.repositorios.UsuarioRepositorio;
import org.springframework.stereotype.Service;

@Service
public class EmpresaServico {

    private final EmpresaRepositorio empresaRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;

    public EmpresaServico(EmpresaRepositorio empresaRepositorio, UsuarioRepositorio usuarioRepositorio) {
        this.empresaRepositorio = empresaRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public EmpresaResposta criarEmpresa(EmpresaRequisicao empresaRequisicao) {
        Usuario dono = this.usuarioRepositorio.findById(empresaRequisicao.empresaDono())
                .orElseThrow(() -> new UsuarioNaoEncontradoException(empresaRequisicao.empresaDono()));

        Empresas empresas = new Empresas();

        empresas.setDescricao(empresaRequisicao.descricao());
        empresas.setEmpresaTipo(empresaRequisicao.empresaTiposAtividade());
        empresas.setEmpresaRegime(empresaRequisicao.empresaRegimeTributario());
        empresas.setEmpresaNaturezaPessoa(empresaRequisicao.empresaNaturezaPessoa());
        empresas.setEmpresaDono(dono);

        this.empresaRepositorio.save(empresas);

        return EmpresaResposta.from(empresas);
    }
}