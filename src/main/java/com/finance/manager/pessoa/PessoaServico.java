package com.finance.manager.pessoa;

import com.finance.manager.empresa.EmpresaRepositorio;
import com.finance.manager.empresa.Empresas;
import com.finance.manager.excecoes.NaoEncontradoException;
import com.finance.manager.excecoes.SemPermissaoException;
import com.finance.manager.excecoes.VinculoNaoEncontrado;
import com.finance.manager.pessoa.dto.PessoaRequisicao;
import com.finance.manager.pessoa.dto.PessoaResposta;
import com.finance.manager.usuario.Usuario;
import com.finance.manager.usuario.UsuarioRepositorio;
import com.finance.manager.usuarioempresa.UsuarioEmpresa;
import com.finance.manager.usuarioempresa.UsuarioEmpresaRepositorio;
import com.finance.manager.usuarioempresa.UsuarioRoles;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PessoaServico {
    private final PessoaRepositorio pessoaRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final EmpresaRepositorio empresaRepositorio;
    private final UsuarioEmpresaRepositorio usuarioEmpresaRepositorio;

    public PessoaServico(PessoaRepositorio pessoaRepositorio, UsuarioRepositorio usuarioRepositorio, EmpresaRepositorio empresaRepositorio, UsuarioEmpresaRepositorio usuarioEmpresaRepositorio) {
        this.pessoaRepositorio = pessoaRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.empresaRepositorio = empresaRepositorio;
        this.usuarioEmpresaRepositorio = usuarioEmpresaRepositorio;
    }

    @Transactional
    public PessoaResposta criarPessoa(UUID usuarioId, UUID empresaId, PessoaRequisicao requisicao) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuário"));

        Empresas empresa = empresaRepositorio.findById(empresaId)
                .orElseThrow(() -> new NaoEncontradoException("a empresa"));

        UsuarioEmpresa vinculo = usuarioEmpresaRepositorio.findByUsuarioIdAndEmpresaId(usuario.getId(), empresa.getId())
                .orElseThrow(() -> new VinculoNaoEncontrado(usuario.getId(), empresa.getId()));

        if (vinculo.getRole() != UsuarioRoles.DONO && vinculo.getRole() != UsuarioRoles.ADMINISTRADOR_DO_SISTEMA)
            throw new SemPermissaoException(usuario.getNome());

        Pessoa pessoa = new Pessoa();

        pessoa.setNome(requisicao.nome());
        pessoa.setCpf(requisicao.cpf());
        pessoa.setCnpj(requisicao.cnpj());
        pessoa.setTipo(requisicao.tipo());
        pessoa.getPessoaUsuarios().add(usuario);
        pessoa.setPessoaEmpresa(empresa);

        pessoaRepositorio.save(pessoa);

        return PessoaResposta.from(pessoa);
    }

    public List<PessoaResposta> buscarTodasAsPessoas(UUID usuarioId, UUID empresaId) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuário"));

        Empresas empresa = empresaRepositorio.findById(empresaId)
                .orElseThrow(() -> new NaoEncontradoException("a Empresa"));

        UsuarioEmpresa vinculo = usuarioEmpresaRepositorio.findByUsuarioIdAndEmpresaId(usuario.getId(), empresa.getId())
                .orElseThrow(() -> new VinculoNaoEncontrado(usuario.getId(), empresa.getId()));

        List<Pessoa> resposta = pessoaRepositorio.findAllByEmpresaId(empresa.getId());

        return PessoaResposta.of(resposta);
    }
}
