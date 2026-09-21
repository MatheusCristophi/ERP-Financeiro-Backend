package com.finance.manager.usuario;

import com.finance.manager.excecoes.VinculoNaoEncontrado;
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
                .map(UsuarioEmpresa::getUsuario)
                .filter(usuario -> usuario.isAtivo())
                .toList()
        );
    }

    @Transactional(readOnly = true)
    public UsuarioResposta buscarUsuario(UUID funcionarioId, UUID usuarioId, UUID empresaId) {
        Usuario funcionario = usuarioRepositorio.findById(funcionarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuário"));

        boolean pertenceAEmpresa = usuarioEmpresaRepositorio.existsByUsuarioIdAndEmpresaId(funcionario.getId(), empresaId);

        if(!pertenceAEmpresa) throw new SemPermissaoException(funcionario.getNome());

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

        Empresas empresa = empresaRepositorio.findById(empresaId)
                .orElseThrow(() -> new NaoEncontradoException("a Empresa"));

        UsuarioEmpresa vinculo = usuarioEmpresaRepositorio.findByUsuarioIdAndEmpresaId(funcionario.getId(), empresa.getId())
                .orElseThrow(() -> new VinculoNaoEncontrado(funcionario.getId(), empresa.getId()));

        boolean ehDonoOuAdmin = vinculo.getRole() == UsuarioRoles.DONO || vinculo.getRole() == UsuarioRoles.ADMINISTRADOR_DO_SISTEMA;

        if(!ehDonoOuAdmin) throw new SemPermissaoException(funcionario.getNome());

        Usuario usuario = new Usuario();

        if(usuarioRepositorio.findByEmail(requisicao.email()).isPresent()) {
            throw new EmailJaExisteException(requisicao.email());
        }

        usuario.setNome(requisicao.nome());
        usuario.setEmail(requisicao.email().toLowerCase());
        usuario.setSenha(encoder.encode(requisicao.senha()));
        usuarioRepositorio.save(usuario);

        UsuarioEmpresa usuarioEmpresa = new UsuarioEmpresa();

        usuarioEmpresa.setUsuario(usuario);
        usuarioEmpresa.setEmpresa(empresa);
        usuarioEmpresa.setRole(role);
        usuarioEmpresaRepositorio.save(usuarioEmpresa);

        return UsuarioResposta.from(usuario);
    }

    @Transactional
    public UsuarioResposta atualizarUsuario(UUID funcionarioId,
                                            UUID usuarioId,
                                            UsuarioRoles roles,
                                            UsuarioRequisicao requisicao,
                                            UUID empresaId
    ) {
        Usuario funcionario = usuarioRepositorio.findById(funcionarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuário"));

        Empresas empresa = empresaRepositorio.findById(empresaId)
                .orElseThrow(() -> new NaoEncontradoException("a Empresa"));

        UsuarioEmpresa vinculo = usuarioEmpresaRepositorio.findByUsuarioIdAndEmpresaId(funcionario.getId(), empresa.getId())
                .orElseThrow(() -> new VinculoNaoEncontrado(funcionario.getId(), empresa.getId()));

        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuário"));

        boolean usuarioProprio = funcionario.getId().equals(usuario.getId());

        boolean ehDonoOuAdmin = vinculo.getRole() == UsuarioRoles.DONO || vinculo.getRole() == UsuarioRoles.ADMINISTRADOR_DO_SISTEMA;

        if(!usuarioProprio && !ehDonoOuAdmin) throw new SemPermissaoException(funcionario.getNome());

        if(requisicao.email() != null) usuario.setEmail(requisicao.email());

        if(requisicao.nome() != null) usuario.setNome(requisicao.nome());

        if(requisicao.senha() != null) usuario.setSenha(encoder.encode(requisicao.senha()));

        if(!usuarioProprio && ehDonoOuAdmin && roles != null) vinculo.setRole(roles);

        return UsuarioResposta.from(usuario);
    }

    @Transactional
    public UsuarioResposta desativarUsuario(UUID funcionarioId, UUID usuarioId, UUID empresaId) {
        Usuario funcionario = usuarioRepositorio.findById(funcionarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Funcionário"));

        Empresas empresa = empresaRepositorio.findById(empresaId)
                .orElseThrow(() -> new NaoEncontradoException("a Empresa"));

        UsuarioEmpresa vinculo = usuarioEmpresaRepositorio.findByUsuarioIdAndEmpresaId(funcionario.getId(), empresa.getId())
                .orElseThrow(() -> new VinculoNaoEncontrado(funcionario.getId(), empresa.getId()));

        boolean ehDonoOuAdmin = vinculo.getRole() == UsuarioRoles.DONO || vinculo.getRole() == UsuarioRoles.ADMINISTRADOR_DO_SISTEMA;

        if(!ehDonoOuAdmin) throw new SemPermissaoException(funcionario.getNome());

        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("o Usuário"));

        usuario.setAtivo(false);

        return UsuarioResposta.from(usuario);
    }
}