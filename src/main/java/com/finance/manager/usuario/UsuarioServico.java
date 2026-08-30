package com.finance.manager.usuario;

import com.finance.manager.usuario.dto.UsuarioRequisicao;
import com.finance.manager.usuario.dto.UsuarioResposta;
import com.finance.manager.empresa.Empresas;
import com.finance.manager.seguranca.UsuarioAutenticado;
import com.finance.manager.usuarioempresa.UsuarioEmpresa;
import com.finance.manager.excecoes.EmailJaExisteException;
import com.finance.manager.excecoes.NaoEncontradoException;
import com.finance.manager.excecoes.SemPermissaoException;
import com.finance.manager.empresa.EmpresaRepositorio;
import com.finance.manager.usuarioempresa.UsuarioEmpresaRepositorio;
import com.finance.manager.usuarioempresa.UsuarioRoles;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioServico implements UserDetailsService {

    private final UsuarioRepositorio usuarioRepositorio;
    private final EmpresaRepositorio empresaRepositorio;
    private final UsuarioEmpresaRepositorio usuarioEmpresaRepositorio;
    private final PasswordEncoder encoder;

    public UsuarioServico(UsuarioRepositorio usuarioRepositorio, EmpresaRepositorio empresaRepositorio,
                          UsuarioEmpresaRepositorio usuarioEmpresaRepositorio, PasswordEncoder encoder) {
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
    public List<UsuarioResposta> buscarUsuarios(UUID usuarioId, UUID empresaId) {

        Usuario usuarioLogado = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuário"));

        boolean pertenceAEmpresa = usuarioEmpresaRepositorio.existsByUsuarioIdAndEmpresaId(usuarioId, empresaId);

        if(!pertenceAEmpresa) {
            throw new SemPermissaoException(usuarioLogado.getNome());
        }

        List<UsuarioEmpresa> empresaFuncionarios = usuarioEmpresaRepositorio.findAllByEmpresaId(empresaId);

        return UsuarioResposta.of(empresaFuncionarios.stream()
                .map(UsuarioEmpresa::getUsuarioId)
                .filter(usuario -> usuario.isAtivo())
                .toList()
        );
    }

    @Transactional(readOnly = true)
    public UsuarioResposta buscarUsuario(UUID funcionarioId, UUID usuarioId, UUID empresaId) {
        Usuario funcionario = usuarioRepositorio.findById(funcionarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuário"));

        boolean pertenceAEmpresa = usuarioEmpresaRepositorio.existsByUsuarioIdAndEmpresaId(funcionarioId, empresaId);

        if(!pertenceAEmpresa) {
            throw new SemPermissaoException(funcionario.getNome());
        }

        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuario"));

        return UsuarioResposta.from(usuario);
    }

    @Transactional
    public UsuarioResposta criarUsuario(UUID usuarioId,
                                        UsuarioRequisicao requisicao,
                                        UsuarioRoles role,
                                        UUID empresaId
    ) {
        Usuario funcionario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuário"));

        UsuarioEmpresa vinculo = usuarioEmpresaRepositorio.findByUsuarioIdAndEmpresaId(usuarioId, empresaId);

        if(vinculo == null) {
            throw new NaoEncontradoException("o Usuário");
        }

        if(vinculo.getRole().equals(UsuarioRoles.DONO) ||
                vinculo.getRole().equals(UsuarioRoles.ADMINISTRADOR_DO_SISTEMA)
        ) throw new SemPermissaoException(funcionario.getNome());

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

    @Transactional
    public UsuarioResposta atualizarUsuario(UUID funcionarioId,
                                            UUID usuarioId,
                                            UsuarioRequisicao requisicao,
                                            UsuarioRoles role,
                                            UUID empresaId
    ) {
        Usuario funcionario = usuarioRepositorio.findById(funcionarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuário"));

        UsuarioEmpresa vinculo = usuarioEmpresaRepositorio.findByUsuarioIdAndEmpresaId(funcionarioId, empresaId);

        if(vinculo == null) {
            throw new NaoEncontradoException("o Usuário");
        }

        if(vinculo.getRole().equals(UsuarioRoles.DONO) ||
                vinculo.getRole().equals(UsuarioRoles.ADMINISTRADOR_DO_SISTEMA)
        ) throw new SemPermissaoException(funcionario.getNome());

        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuário"));

        if(requisicao.email() != null) usuario.setEmail(requisicao.email());

        if(requisicao.nome() != null) usuario.setNome(requisicao.nome());

        if(requisicao.senha() != null) usuario.setSenha(encoder.encode(requisicao.senha()));

        return UsuarioResposta.from(usuario);
    }

    @Transactional
    public UsuarioResposta desativarUsuario(UUID funcionarioId, UUID usuarioId, UUID empresaId) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuário"));

        List<UsuarioEmpresa> usuarioEmpresa = usuarioEmpresaRepositorio.findAllByUsuarioIdAndEmpresaId(funcionarioId, empresaId);

        Usuario funcionario = usuarioRepositorio.findById(funcionarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Funcionário"));

        if (!funcionario.getUsuarioEmpresas().equals(usuarioEmpresa)) throw new SemPermissaoException(usuario.getEmail());

        if(usuarioEmpresa.isEmpty()) throw new SemPermissaoException(funcionario.getEmail());

        usuario.setAtivo(false);

        return UsuarioResposta.from(usuario);
    }
}