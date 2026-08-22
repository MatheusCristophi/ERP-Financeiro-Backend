package com.finance.manager.servicos;

import com.finance.manager.controladores.usuariodtos.UsuarioRequisicao;
import com.finance.manager.controladores.usuariodtos.UsuarioResposta;
import com.finance.manager.entidades.Empresas;
import com.finance.manager.entidades.Usuario;
import com.finance.manager.entidades.UsuarioAutenticado;
import com.finance.manager.entidades.UsuarioEmpresa;
import com.finance.manager.enums.UsuarioRoles;
import com.finance.manager.excecoes.EmailJaExisteException;
import com.finance.manager.excecoes.NaoEncontradoException;
import com.finance.manager.excecoes.SemPermissaoException;
import com.finance.manager.repositorios.EmpresaRepositorio;
import com.finance.manager.repositorios.UsuarioEmpresaRepositorio;
import com.finance.manager.repositorios.UsuarioRepositorio;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioServico implements UserDetailsService {

    private final UsuarioRepositorio usuarioRepositorio;
    private final EmpresaRepositorio empresaRepositorio;
    private final UsuarioEmpresaRepositorio usuarioEmpresaRepositorio;
    private final BCryptPasswordEncoder encoder;

    public UsuarioServico(UsuarioRepositorio usuarioRepositorio, EmpresaRepositorio empresaRepositorio, UsuarioEmpresaRepositorio usuarioEmpresaRepositorio, BCryptPasswordEncoder encoder) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.empresaRepositorio = empresaRepositorio;
        this.usuarioEmpresaRepositorio = usuarioEmpresaRepositorio;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> new NaoEncontradoException("O Usuário"));

        return new UsuarioAutenticado(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResposta> buscarUsuarios(UUID usuarioId){
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new SemPermissaoException(usuarioId.toString()));

        UsuarioEmpresa usuarioEmpresa = usuarioEmpresaRepositorio.findByUsuarioId(usuario);

        if(usuarioEmpresa.getRole() != UsuarioRoles.DONO || usuarioEmpresa.getRole() != UsuarioRoles.ADMINISTRADOR_DO_SISTEMA)
            throw new SemPermissaoException(usuario.getEmail());

        List<Usuario> usuarios = usuarioRepositorio.findAll();
        return UsuarioResposta.of(usuarios);
    }

    @Transactional
    public UsuarioResposta criarUsuario(UsuarioRequisicao requisicao,
                                        UsuarioRoles role,
                                        UUID empresaId
    ) {
        Usuario usuario = new Usuario();

        if(usuarioRepositorio.findByEmail(requisicao.email()).isPresent()) {
            throw new EmailJaExisteException(requisicao.email());
        }

        usuario.setNome(requisicao.nome());
        usuario.setEmail(requisicao.email().toLowerCase());
        usuario.setSenha(encoder.encode(requisicao.senha()));
        usuarioRepositorio.save(usuario);

        Empresas empresa = empresaRepositorio.findById(empresaId)
                .orElseThrow(() -> new NaoEncontradoException("a Empresa"));

        UsuarioEmpresa usuarioEmpresa = new UsuarioEmpresa();

        usuarioEmpresa.setUsuarioId(usuario);
        usuarioEmpresa.setEmpresaId(empresa);
        usuarioEmpresa.setRole(role);
        usuarioEmpresaRepositorio.save(usuarioEmpresa);

        return UsuarioResposta.from(usuario);
    }
}