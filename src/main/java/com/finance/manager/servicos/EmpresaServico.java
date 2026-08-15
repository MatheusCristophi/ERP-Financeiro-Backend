package com.finance.manager.servicos;

import com.finance.manager.controladores.empresadto.EmpresaRequisicao;
import com.finance.manager.controladores.empresadto.EmpresaResposta;
import com.finance.manager.entidades.Empresas;
import com.finance.manager.entidades.Usuario;
import com.finance.manager.excecoes.NaoEncontradoException;
import com.finance.manager.repositorios.EmpresaRepositorio;
import com.finance.manager.repositorios.UsuarioRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmpresaServico {

    private final EmpresaRepositorio empresaRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;

    public EmpresaServico(EmpresaRepositorio empresaRepositorio, UsuarioRepositorio usuarioRepositorio) {
        this.empresaRepositorio = empresaRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public EmpresaResposta criarEmpresa(EmpresaRequisicao empresaRequisicao, UUID id) {
        Usuario dono = this.usuarioRepositorio.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("o Email"));

        Empresas empresas = new Empresas();

        empresas.setDescricao(empresaRequisicao.descricao());
        empresas.setEmpresaTipo(empresaRequisicao.empresaTiposAtividade());
        empresas.setEmpresaRegime(empresaRequisicao.empresaRegimeTributario());
        empresas.setEmpresaNaturezaPessoa(empresaRequisicao.empresaNaturezaPessoa());
        empresas.setEmpresaDono(dono);

        this.empresaRepositorio.save(empresas);

        return EmpresaResposta.from(empresas);
    }

    public List<EmpresaResposta> buscarTodasEmpresas(UUID id){
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("as Empresas"));

        List<Empresas> empresas = empresaRepositorio.findAllByEmpresaDono(usuario);

        if(empresas.isEmpty()) throw new NaoEncontradoException("as Empresas");

        return EmpresaResposta.of(empresas);
    }
}