package com.finance.manager.categoria;

import com.finance.manager.categoria.dto.CategoriaRequisicao;
import com.finance.manager.categoria.dto.CategoriaResposta;
import com.finance.manager.empresa.EmpresaRepositorio;
import com.finance.manager.empresa.Empresas;
import com.finance.manager.excecoes.NaoEncontradoException;
import com.finance.manager.excecoes.VinculoNaoEncontrado;
import com.finance.manager.usuario.Usuario;
import com.finance.manager.usuario.UsuarioRepositorio;
import com.finance.manager.usuarioempresa.UsuarioEmpresa;
import com.finance.manager.usuarioempresa.UsuarioEmpresaRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CategoriaServico {
    private final CategoriaRepositorio categoriaRepositorio;
    private final EmpresaRepositorio empresaRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final UsuarioEmpresaRepositorio usuarioEmpresaRepositorio;

    public CategoriaServico(CategoriaRepositorio categoriaRepositorio, EmpresaRepositorio empresaRepositorio, UsuarioRepositorio usuarioRepositorio, UsuarioEmpresaRepositorio usuarioEmpresaRepositorio) {
        this.categoriaRepositorio = categoriaRepositorio;
        this.empresaRepositorio = empresaRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.usuarioEmpresaRepositorio = usuarioEmpresaRepositorio;
    }

    @Transactional
    public CategoriaResposta criarCategoria(UUID empresaId, UUID usuarioId, CategoriaRequisicao requisicao) {
        Empresas empresaAtual = empresaRepositorio.findById(empresaId)
                .orElseThrow(() -> new NaoEncontradoException("a Empresa"));

        Usuario usuarioAtual = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuário"));

        boolean vinculo = usuarioEmpresaRepositorio.existsByUsuarioIdAndEmpresaId(usuarioAtual.getUsuarioId(), empresaAtual.getEmpresaId());

        if(!vinculo) throw new VinculoNaoEncontrado(usuarioAtual.getNome(), empresaAtual.getDescricao());

        Categoria categoria = new Categoria();
        categoria.setDescricao(requisicao.descricao());
        categoria.setStatus(requisicao.status());
        categoria.setTipo(requisicao.tipo());
        categoria.setCategoriaEmpresa(empresaAtual);

        return CategoriaResposta.from(categoria);
    }

    @Transactional(readOnly = true)
    public List<CategoriaResposta> buscarTodasCategorias(UUID empresaId, UUID usuarioId) {
        Empresas empresaAtual = empresaRepositorio.findById(empresaId)
                .orElseThrow(() -> new NaoEncontradoException("a Empresa"));

        Usuario usuarioAtual = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuário"));

        boolean vinculo = usuarioEmpresaRepositorio.existsByUsuarioIdAndEmpresaId(usuarioAtual.getUsuarioId(), empresaAtual.getEmpresaId());

        if(!vinculo) throw new VinculoNaoEncontrado(usuarioAtual.getNome(), empresaAtual.getDescricao());

        List<Categoria> resposta = categoriaRepositorio.findAllByCategoriaEmpresa(empresaAtual);

        return CategoriaResposta.of(resposta);
    }

    @Transactional(readOnly = true)
    public CategoriaResposta buscarCategoria(UUID empresaId, UUID usuarioId, UUID categoriaId) {
        Empresas empresaAtual = empresaRepositorio.findById(empresaId)
                .orElseThrow(() -> new NaoEncontradoException("a Empresa"));

        Usuario usuarioAtual = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuário"));

        boolean vinculo = usuarioEmpresaRepositorio.existsByUsuarioIdAndEmpresaId(usuarioAtual.getUsuarioId(), empresaAtual.getEmpresaId());

        if(!vinculo) throw new RuntimeException("Vinculo não encontrado entre o usuário "+usuarioAtual.getNome()+" e a empresa "+empresaAtual.getDescricao());

        Categoria resposta = categoriaRepositorio.findById(categoriaId)
                .orElseThrow(() -> new NaoEncontradoException("a Categoria"));

        return CategoriaResposta.from(resposta);
    }

    @Transactional
    public CategoriaResposta atualizarCategoria(UUID empresaId, UUID usuarioId, CategoriaRequisicao requisicao, UUID categoriaId) {
        Empresas empresaAtual = empresaRepositorio.findById(empresaId)
                .orElseThrow(() -> new NaoEncontradoException("a Empresa"));

        Usuario usuarioAtual = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuário"));

        boolean vinculo = usuarioEmpresaRepositorio.existsByUsuarioIdAndEmpresaId(usuarioAtual.getUsuarioId(), empresaAtual.getEmpresaId());

        if(!vinculo) throw new RuntimeException("Vinculo não encontrado entre o usuário "+usuarioAtual.getNome()+" e a empresa "+empresaAtual.getDescricao());

        Categoria categoria = categoriaRepositorio.findById(categoriaId)
                .orElseThrow(() -> new NaoEncontradoException("a Categoria"));

        if(!requisicao.descricao().isEmpty()) categoria.setDescricao(requisicao.descricao());

        if(requisicao.tipo() != null) categoria.setTipo(requisicao.tipo());

        if(!requisicao.status()) categoria.setStatus(false);

        return CategoriaResposta.from(categoria);
    }

    @Transactional
    public void desativarCategoria(UUID empresaId, UUID usuarioId, UUID categoriaId) {
        Empresas empresaAtual = empresaRepositorio.findById(empresaId)
                .orElseThrow(() -> new NaoEncontradoException("a Empresa"));

        Usuario usuarioAtual = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuário"));

        boolean vinculo = usuarioEmpresaRepositorio.existsByUsuarioIdAndEmpresaId(usuarioAtual.getUsuarioId(), empresaAtual.getEmpresaId());

        if(!vinculo) throw new RuntimeException("Vinculo não encontrado entre o usuário "+usuarioAtual.getNome()+" e a empresa "+empresaAtual.getDescricao());

        Categoria categoria = categoriaRepositorio.findById(categoriaId)
                .orElseThrow(() -> new NaoEncontradoException("a Categoria"));

        categoria.setStatus(false);
    }
}