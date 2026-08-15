package com.finance.manager.servicos;

import com.finance.manager.controladores.empresadto.EmpresaRequisicao;
import com.finance.manager.controladores.empresadto.EmpresaResposta;
import com.finance.manager.entidades.Empresas;
import com.finance.manager.entidades.Usuario;
import com.finance.manager.excecoes.NaoEncontradoException;
import com.finance.manager.excecoes.SemPermissaoException;
import com.finance.manager.repositorios.EmpresaRepositorio;
import com.finance.manager.repositorios.UsuarioRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public EmpresaResposta criarEmpresa(EmpresaRequisicao empresaRequisicao, UUID usuarioId) {
        Usuario dono = this.usuarioRepositorio.findById(usuarioId)
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

    @Transactional(readOnly = true)
    public List<EmpresaResposta> buscarTodasEmpresas(UUID usuarioId){
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("as Empresas"));

        List<Empresas> empresas = empresaRepositorio.findAllByEmpresaDono(usuario);

        if(empresas.isEmpty()) throw new NaoEncontradoException("as Empresas");

        return EmpresaResposta.of(empresas);
    }

    @Transactional
    public void desativarEmpresa(UUID usuarioId, UUID empresaId){
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuário"));

        Empresas empresas = empresaRepositorio.findById(empresaId)
                .orElseThrow(() -> new NaoEncontradoException("a empresa"));

        if (!empresas.getEmpresaDono().getUsuarioId().equals(usuario.getUsuarioId())) {
            throw new SemPermissaoException(usuario.getNome());
        }

        empresas.setStatus(false);
    }
}