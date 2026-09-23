package com.finance.manager.lancamento;

import com.finance.manager.empresa.EmpresaRepositorio;
import com.finance.manager.empresa.Empresas;
import com.finance.manager.excecoes.NaoEncontradoException;
import com.finance.manager.excecoes.SemPermissaoException;
import com.finance.manager.excecoes.VinculoNaoEncontrado;
import com.finance.manager.lancamento.dto.LancamentoResposta;
import com.finance.manager.usuario.Usuario;
import com.finance.manager.usuario.UsuarioRepositorio;
import com.finance.manager.usuarioempresa.UsuarioEmpresa;
import com.finance.manager.usuarioempresa.UsuarioEmpresaRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LancamentoServico {

    private final LancamentoRepositorio lancamentoRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final EmpresaRepositorio empresaRepositorio;
    private final UsuarioEmpresaRepositorio usuarioEmpresaRepositorio;

    public LancamentoServico(LancamentoRepositorio lancamentoRepositorio, UsuarioRepositorio usuarioRepositorio, EmpresaRepositorio empresaRepositorio, UsuarioEmpresaRepositorio usuarioEmpresaRepositorio) {
        this.lancamentoRepositorio = lancamentoRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.empresaRepositorio = empresaRepositorio;
        this.usuarioEmpresaRepositorio = usuarioEmpresaRepositorio;
    }

    public List<LancamentoResposta> buscarTodosLancamentos(UUID usuarioId, UUID empresaId) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuário"));

        Empresas empresas = empresaRepositorio.findById(empresaId)
                .orElseThrow(() -> new NaoEncontradoException("a Empresa"));

        UsuarioEmpresa vinculo = usuarioEmpresaRepositorio.findByUsuarioIdAndEmpresaId(usuario.getId(), empresas.getId())
                .orElseThrow(() -> new VinculoNaoEncontrado(usuarioId, empresaId));

        List<Lancamento> resposta = lancamentoRepositorio.findAllByLancamentoEmpresa(empresas);

        return LancamentoResposta.of(resposta);
    }

    public LancamentoResposta buscarLancamentoPorId(UUID usuarioId, UUID empresaId, UUID lancamentoId) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuário"));

        Empresas empresas = empresaRepositorio.findById(empresaId)
                .orElseThrow(() -> new NaoEncontradoException("a Empresa"));

        UsuarioEmpresa vinculo = usuarioEmpresaRepositorio.findByUsuarioIdAndEmpresaId(usuario.getId(), empresas.getId())
                .orElseThrow(() -> new VinculoNaoEncontrado(usuarioId, empresaId));

        Lancamento resposta = lancamentoRepositorio.findById(lancamentoId)
                .orElseThrow(() -> new NaoEncontradoException("o Lançamento"));

        if(!resposta.getLancamentoEmpresa().equals(empresas)) throw new NaoEncontradoException("o Lancamento");

        return LancamentoResposta.from(resposta);
    }
}
