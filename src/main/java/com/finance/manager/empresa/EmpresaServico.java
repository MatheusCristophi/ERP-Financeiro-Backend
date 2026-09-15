package com.finance.manager.empresa;

import com.finance.manager.empresa.dto.EmpresaRequisicao;
import com.finance.manager.empresa.dto.EmpresaResposta;
import com.finance.manager.usuario.Usuario;
import com.finance.manager.usuarioempresa.UsuarioRoles;
import com.finance.manager.excecoes.NaoEncontradoException;
import com.finance.manager.excecoes.SemPermissaoException;
import com.finance.manager.usuarioempresa.UsuarioEmpresaRepositorio;
import com.finance.manager.usuario.UsuarioRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EmpresaServico {

    private final EmpresaRepositorio empresaRepositorio;
    private final UsuarioEmpresaRepositorio usuarioEmpresaRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;

    public EmpresaServico(EmpresaRepositorio empresaRepositorio, UsuarioEmpresaRepositorio usuarioEmpresaRepositorio, UsuarioRepositorio usuarioRepositorio) {
        this.empresaRepositorio = empresaRepositorio;
        this.usuarioEmpresaRepositorio = usuarioEmpresaRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Transactional
    public EmpresaResposta criarEmpresa(EmpresaRequisicao empresaRequisicao, UUID usuarioId) {
        Usuario dono = this.usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Email"));

        if(usuarioEmpresaRepositorio.findByUsuarioId(dono.getUsuarioId()).getRole() != UsuarioRoles.DONO)
            throw new SemPermissaoException(dono.getNome());

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
        Usuario usuario = this.usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("as Empresas"));

        List<Empresas> empresas = this.empresaRepositorio.findAllByEmpresaDono(usuario);

        return EmpresaResposta.of(empresas);
    }

    @Transactional(readOnly = true)
    public EmpresaResposta buscarEmpresaPorId(UUID usuarioId, UUID empresaId) {
        Usuario usuario = this.usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("as empresas"));

        Empresas empresa = this.empresaRepositorio.findById(empresaId)
                .orElseThrow(() -> new NaoEncontradoException("as empresas"));

        boolean possuiAcesso = usuario.getUsuarioEmpresas().stream()
                .anyMatch(vinculo -> vinculo.getEmpresaId().getEmpresaId().equals(empresa.getEmpresaId()));

        if (!possuiAcesso) throw new SemPermissaoException(usuario.getNome());

        return EmpresaResposta.from(empresa);
    }

    @Transactional
    public EmpresaResposta atualizarEmpresaCompleta(UUID empresaId, UUID usuarioId, EmpresaRequisicao empresaRequisicao){
        Usuario usuario = this.usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuário"));

        Empresas empresa = this.empresaRepositorio.findById(empresaId)
                .orElseThrow(() -> new NaoEncontradoException("a empresa"));

        if(usuarioEmpresaRepositorio.findByUsuarioId(usuario.getUsuarioId()).getRole() != UsuarioRoles.DONO)
            throw new SemPermissaoException(usuario.getNome());

        if(!empresa.getEmpresaDono().getUsuarioId().equals(usuario.getUsuarioId())) throw new SemPermissaoException(usuario.getNome());

        empresa.setDescricao(empresaRequisicao.descricao());
        empresa.setEmpresaTipo(empresaRequisicao.empresaTiposAtividade());
        empresa.setEmpresaRegime(empresaRequisicao.empresaRegimeTributario());
        empresa.setEmpresaNaturezaPessoa(empresaRequisicao.empresaNaturezaPessoa());
        empresa.setEmpresaDono(usuario);

        return EmpresaResposta.from(empresa);
    }

    @Transactional
    public void desativarEmpresa(UUID usuarioId, UUID empresaId){
        Usuario usuario = this.usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuário"));

        Empresas empresas = this.empresaRepositorio.findById(empresaId)
                .orElseThrow(() -> new NaoEncontradoException("a empresa"));

        if(usuarioEmpresaRepositorio.findByUsuarioId(usuario.getUsuarioId()).getRole() != UsuarioRoles.DONO)
            throw new SemPermissaoException(usuario.getNome());

        if (!empresas.getEmpresaDono().getUsuarioId().equals(usuario.getUsuarioId())) throw new SemPermissaoException(usuario.getNome());

        empresas.setStatus(false);
    }
}