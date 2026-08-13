package com.finance.manager.servicos;

import com.finance.manager.controladores.DTOs.UsuarioRequisicao;
import com.finance.manager.controladores.DTOs.UsuarioResposta;
import com.finance.manager.entidades.Usuario;
import com.finance.manager.excecoes.EmailJaExisteException;
import com.finance.manager.repositorios.UsuarioRepositorio;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServico {
    private final UsuarioRepositorio usuarioRepositorio;

    public UsuarioServico(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public UsuarioResposta criarUsuario(UsuarioRequisicao requisicao) {
        Usuario usuario = new Usuario();
        usuario.setNome(requisicao.nome());
        usuario.setEmail(requisicao.email());
        usuario.setSenha(requisicao.senha());

        try {
            usuarioRepositorio.save(usuario);
        } catch (DataIntegrityViolationException e) {
            throw new EmailJaExisteException(requisicao.email());
        }
        return UsuarioResposta.from(usuario);
    }
}
