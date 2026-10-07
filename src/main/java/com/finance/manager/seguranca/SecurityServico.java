package com.finance.manager.seguranca;

import com.finance.manager.empresa.EmpresaRepositorio;
import com.finance.manager.empresa.Empresas;
import com.finance.manager.seguranca.dtos.SecurityRequisicao;
import com.finance.manager.seguranca.dtos.SecurityResposta;
import com.finance.manager.usuarioempresa.UsuarioEmpresa;
import com.finance.manager.usuarioempresa.UsuarioEmpresaRepositorio;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SecurityServico {

    private final ObjectProvider<AuthenticationManager> authenticationManager;
    private final TokenServico tokenServico;
    private final EmpresaRepositorio empresaRepositorio;
    private final UsuarioEmpresaRepositorio usuarioEmpresaRepositorio;

    public SecurityServico(ObjectProvider<AuthenticationManager> authenticationManager, TokenServico tokenServico, EmpresaRepositorio empresaRepositorio, UsuarioEmpresaRepositorio usuarioEmpresaRepositorio) {
        this.authenticationManager = authenticationManager;
        this.tokenServico = tokenServico;
        this.empresaRepositorio = empresaRepositorio;
        this.usuarioEmpresaRepositorio = usuarioEmpresaRepositorio;
    }

    public SecurityResposta logar(SecurityRequisicao requisicao) {
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(requisicao.email(), requisicao.senha());
        Authentication auth = authenticationManager.getObject().authenticate(userAndPass);
        UserDetails details = (UserDetails) auth.getPrincipal();
        UsuarioAutenticado usuario = (UsuarioAutenticado) auth.getPrincipal();

        Empresas empresa = empresaRepositorio.findByCnpj(requisicao.cnpj())
                .filter(Empresas::isStatus)
                .orElseThrow(() -> new BadCredentialsException("Dados inválidos"));

        UsuarioEmpresa vinculo = usuarioEmpresaRepositorio
                .findByUsuarioIdAndEmpresaId(usuario.getUsuario().getId(), empresa.getId())
                .orElseThrow(() -> new BadCredentialsException("Dados inválidos"));

        String token = tokenServico.gerarToken(details, empresa.getId());
        return new SecurityResposta(token, empresa.getId(), vinculo.getRole());
    }
}