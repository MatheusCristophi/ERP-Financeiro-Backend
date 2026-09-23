package com.finance.manager.lancamento;

import com.finance.manager.categoria.Categoria;
import com.finance.manager.categoria.CategoriaRepositorio;
import com.finance.manager.empresa.EmpresaRepositorio;
import com.finance.manager.empresa.Empresas;
import com.finance.manager.excecoes.NaoEncontradoException;
import com.finance.manager.excecoes.SemPermissaoException;
import com.finance.manager.excecoes.VinculoNaoEncontrado;
import com.finance.manager.lancamento.dto.LancamentoRequisicao;
import com.finance.manager.lancamento.dto.LancamentoResposta;
import com.finance.manager.pessoa.Pessoa;
import com.finance.manager.pessoa.PessoaRepositorio;
import com.finance.manager.usuario.Usuario;
import com.finance.manager.usuario.UsuarioRepositorio;
import com.finance.manager.usuarioempresa.UsuarioEmpresa;
import com.finance.manager.usuarioempresa.UsuarioEmpresaRepositorio;
import com.finance.manager.usuarioempresa.UsuarioRoles;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class LancamentoServico {

    private final LancamentoRepositorio lancamentoRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final EmpresaRepositorio empresaRepositorio;
    private final UsuarioEmpresaRepositorio usuarioEmpresaRepositorio;
    private final CategoriaRepositorio categoriaRepositorio;
    private final PessoaRepositorio pessoaRepositorio;

    public LancamentoServico(LancamentoRepositorio lancamentoRepositorio, UsuarioRepositorio usuarioRepositorio, EmpresaRepositorio empresaRepositorio, UsuarioEmpresaRepositorio usuarioEmpresaRepositorio, CategoriaRepositorio categoriaRepositorio, PessoaRepositorio pessoaRepositorio) {
        this.lancamentoRepositorio = lancamentoRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.empresaRepositorio = empresaRepositorio;
        this.usuarioEmpresaRepositorio = usuarioEmpresaRepositorio;
        this.categoriaRepositorio = categoriaRepositorio;
        this.pessoaRepositorio = pessoaRepositorio;
    }

    public LancamentoResposta criarLancamentos(UUID usuarioId, UUID empresaId, UUID categoriaId, UUID pessoaId, LancamentoRequisicao requisicao) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuário"));

        Empresas empresas = empresaRepositorio.findById(empresaId)
                .orElseThrow(() -> new NaoEncontradoException("a Empresa"));

        UsuarioEmpresa vinculo = usuarioEmpresaRepositorio.findByUsuarioIdAndEmpresaId(usuario.getId(), empresas.getId())
                .orElseThrow(() -> new VinculoNaoEncontrado(usuarioId, empresaId));

        if(vinculo.getRole() == UsuarioRoles.CONSULTOR) throw new SemPermissaoException(usuario.getNome());

        Categoria categoria = categoriaRepositorio.findById(categoriaId)
                .orElseThrow(() -> new NaoEncontradoException("a Categoria"));

        if(!categoria.getCategoriaEmpresa().equals(empresas)) throw new NaoEncontradoException("a Categoria");

        Pessoa pessoa = pessoaRepositorio.findById(pessoaId)
                .orElseThrow(() -> new NaoEncontradoException("a Pessoa"));

        if(!pessoa.getPessoaEmpresa().equals(empresas)) throw new NaoEncontradoException("a Pessoa");

        Lancamento lancamento = new Lancamento();

        lancamento.setDescricao(requisicao.descricao());
        lancamento.setValor(requisicao.valor());
        lancamento.setDataEmissao(requisicao.dataEmissao());
        lancamento.setDataVencimento(requisicao.dataVencimento());
        lancamento.setDataPagamento(requisicao.dataPagamento());
        lancamento.setLancamentoCategoria(categoria);
        lancamento.setLancamentoEmpresa(empresas);
        lancamento.setLancamentoPessoa(pessoa);
        lancamento.setLancamentoUsuario(usuario);
        lancamento.setMovimentacao(requisicao.movimentacaoTipo());
        lancamento.setStatus(requisicao.status());
        lancamento.setObservacao(requisicao.observacao());

        lancamentoRepositorio.save(lancamento);

        return LancamentoResposta.from(lancamento);
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

    public LancamentoResposta atualizarLancamentos(UUID usuarioId, UUID empresaId, UUID lancamentoId, UUID categoriaId, UUID pessoaId, LancamentoRequisicao requisicao) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuário"));

        Empresas empresas = empresaRepositorio.findById(empresaId)
                .orElseThrow(() -> new NaoEncontradoException("a Empresa"));

        UsuarioEmpresa vinculo = usuarioEmpresaRepositorio.findByUsuarioIdAndEmpresaId(usuario.getId(), empresas.getId())
                .orElseThrow(() -> new VinculoNaoEncontrado(usuarioId, empresaId));

        if(vinculo.getRole() == UsuarioRoles.CONSULTOR) throw new SemPermissaoException(usuario.getNome());

        Categoria categoria = categoriaRepositorio.findById(categoriaId)
                .orElseThrow(() -> new NaoEncontradoException("a Categoria"));

        if(!categoria.getCategoriaEmpresa().equals(empresas)) throw new NaoEncontradoException("a Categoria");

        Pessoa pessoa = pessoaRepositorio.findById(pessoaId)
                .orElseThrow(() -> new NaoEncontradoException("a Pessoa"));

        if(!pessoa.getPessoaEmpresa().equals(empresas)) throw new NaoEncontradoException("a Pessoa");

        Lancamento lancamento = lancamentoRepositorio.findById(lancamentoId)
                .orElseThrow(() -> new NaoEncontradoException("o Lançamento"));

        if(!requisicao.descricao().isEmpty()) lancamento.setDescricao(requisicao.descricao());
        if(requisicao.valor() != BigDecimal.ZERO) lancamento.setValor(requisicao.valor());
        lancamento.setDataEmissao(requisicao.dataEmissao());
        lancamento.setDataVencimento(requisicao.dataVencimento());
        lancamento.setDataPagamento(requisicao.dataPagamento());
        lancamento.setLancamentoCategoria(categoria);
        lancamento.setLancamentoEmpresa(empresas);
        lancamento.setLancamentoPessoa(pessoa);
        lancamento.setLancamentoUsuario(usuario);
        if(requisicao.movimentacaoTipo() != null) lancamento.setMovimentacao(requisicao.movimentacaoTipo());
        lancamento.setStatus(requisicao.status());
        if(!requisicao.observacao().isEmpty()) lancamento.setObservacao(requisicao.observacao());

        lancamentoRepositorio.save(lancamento);

        return LancamentoResposta.from(lancamento);
    }

    public void deletarLancamento(UUID usuarioId, UUID empresaId, UUID lancamentoId) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuário"));

        Empresas empresas = empresaRepositorio.findById(empresaId)
                .orElseThrow(() -> new NaoEncontradoException("a Empresa"));

        UsuarioEmpresa vinculo = usuarioEmpresaRepositorio.findByUsuarioIdAndEmpresaId(usuario.getId(), empresas.getId())
                .orElseThrow(() -> new VinculoNaoEncontrado(usuarioId, empresaId));

        if(vinculo.getRole() == UsuarioRoles.CONSULTOR) throw new SemPermissaoException(usuario.getNome());

        Lancamento lancamento = lancamentoRepositorio.findById(lancamentoId)
                .orElseThrow(() -> new NaoEncontradoException("o Lançamento"));

        if(!lancamento.getLancamentoEmpresa().equals(empresas)) throw new NaoEncontradoException("o Lançamento");

        lancamentoRepositorio.delete(lancamento);
    }
}
