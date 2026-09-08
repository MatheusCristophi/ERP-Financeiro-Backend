package com.finance.manager.categoria;

import com.finance.manager.categoria.dto.CategoriaRequisicao;
import com.finance.manager.categoria.dto.CategoriaResposta;
import com.finance.manager.empresa.EmpresaRepositorio;
import com.finance.manager.empresa.Empresas;
import com.finance.manager.excecoes.NaoEncontradoException;
import com.finance.manager.usuario.Usuario;
import com.finance.manager.usuario.UsuarioRepositorio;
import com.finance.manager.usuarioempresa.UsuarioEmpresa;
import com.finance.manager.usuarioempresa.UsuarioEmpresaRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        UsuarioEmpresa vinculo = usuarioEmpresaRepositorio.findByUsuarioIdAndEmpresaId(usuarioAtual.getUsuarioId(), empresaAtual.getEmpresaId());

        if(vinculo.getEmpresaId().getEmpresaId() != empresaId || vinculo.getUsuarioId().getUsuarioId() != usuarioId)
        throw new RuntimeException("Vinculo não encontrado entre o usuário "+usuarioAtual.getNome()+" e a empresa "+empresaAtual.getDescricao());

        Categoria categoria = new Categoria();
        categoria.setDescricao(requisicao.descricao());
        categoria.setStatus(requisicao.status());
        categoria.setTipo(requisicao.Tipo());
        categoria.setCategoriaEmpresa(empresaAtual);

        return CategoriaResposta.from(categoria);
    }


}