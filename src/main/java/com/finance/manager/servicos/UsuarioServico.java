package com.finance.manager.servicos;

import com.finance.manager.entidades.Usuario;
import com.finance.manager.entidades.UsuarioAutenticado;
import com.finance.manager.excecoes.NaoEncontradoException;
import com.finance.manager.repositorios.UsuarioRepositorio;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServico implements UserDetailsService {
    private final UsuarioRepositorio usuarioRepositorio;

    public UsuarioServico(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> new NaoEncontradoException("O Usuário"));

        return new UsuarioAutenticado(usuario);
    }
}
