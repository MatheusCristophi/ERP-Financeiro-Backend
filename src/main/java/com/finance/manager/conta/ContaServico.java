package com.finance.manager.conta;

import com.finance.manager.conta.dto.ContaRequisicao;
import com.finance.manager.conta.dto.ContaResposta;
import com.finance.manager.empresa.EmpresaRepositorio;
import com.finance.manager.empresa.Empresas;
import com.finance.manager.excecoes.NaoEncontradoException;
import com.finance.manager.excecoes.SemPermissaoException;
import com.finance.manager.excecoes.VinculoNaoEncontrado;
import com.finance.manager.usuario.Usuario;
import com.finance.manager.usuario.UsuarioRepositorio;
import com.finance.manager.usuarioempresa.UsuarioEmpresa;
import com.finance.manager.usuarioempresa.UsuarioEmpresaRepositorio;
import com.finance.manager.usuarioempresa.UsuarioRoles;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ContaServico {

    private final ContaRepositorio contaRepositorio;
    private final EmpresaRepositorio empresaRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final UsuarioEmpresaRepositorio usuarioEmpresaRepositorio;

    public ContaServico(ContaRepositorio contaRepositorio, EmpresaRepositorio empresaRepositorio, UsuarioRepositorio usuarioRepositorio, UsuarioEmpresaRepositorio usuarioEmpresaRepositorio) {
        this.contaRepositorio = contaRepositorio;
        this.empresaRepositorio = empresaRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.usuarioEmpresaRepositorio = usuarioEmpresaRepositorio;
    }

    public ContaResposta criarConta(UUID usuarioId, UUID empresaId, ContaRequisicao requisicao) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuário"));

        Empresas empresa = empresaRepositorio.findById(empresaId)
                .orElseThrow(() -> new NaoEncontradoException("a Empresa"));

        boolean existeVinculo = usuarioEmpresaRepositorio.existsByUsuarioIdAndEmpresaId(usuarioId, empresaId);

        if(!existeVinculo) throw new VinculoNaoEncontrado(usuario.getNome(), empresa.getDescricao());

        UsuarioEmpresa vinculo = usuarioEmpresaRepositorio.findByUsuarioIdAndEmpresaId(usuario.getUsuarioId(), empresa.getEmpresaId())
                .orElseThrow(() -> new VinculoNaoEncontrado(usuario.getNome(), empresa.getDescricao()));

        if(!vinculo.getRole().equals(UsuarioRoles.DONO) || !vinculo.getRole().equals(UsuarioRoles.ADMINISTRADOR_DO_SISTEMA)) throw new SemPermissaoException(usuario.getNome());

        Conta conta = new Conta();

        conta.setDescricao(requisicao.descricao());
        conta.setAgencia(requisicao.agencia());
        conta.setNumero(requisicao.agencia());
        conta.setContaMovimentacao(requisicao.movimentacaoTipo());
        conta.setContaTipo(requisicao.contaTipo());
        conta.setContaEmpresa(empresa);

        return ContaResposta.from(conta);
    }
}