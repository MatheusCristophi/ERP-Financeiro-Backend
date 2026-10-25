package com.finance.manager.seguranca;

import com.finance.manager.empresa.EmpresaRepositorio;
import com.finance.manager.empresa.Empresas;
import com.finance.manager.seguranca.dtos.SecurityRequisicao;
import com.finance.manager.usuarioempresa.UsuarioEmpresa;
import com.finance.manager.usuarioempresa.UsuarioEmpresaRepositorio;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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

    public void logar(SecurityRequisicao requisicao, HttpServletResponse response) {
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

        ResponseCookie cookie = ResponseCookie.from("cookieToken", token)
                .httpOnly(true)
                .secure(true) //True por conta do backend estar hospedado no Render, que é HTTPS. Se for para localhost, mudar para false
                .sameSite("None") //temporário para funcionar no localhost, depois mudar para "Lax"
                .path("/")
                .maxAge(86400)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void logout(HttpServletResponse response) {
        ResponseCookie apagar = ResponseCookie.from("cookieToken", "")
                .httpOnly(true)
                .secure(true) //True por conta do backend estar hospedado no Render, que é HTTPS. Se for para localhost, mudar para false
                .sameSite("None") //temporário para funcionar no localhost, depois mudar para "Lax"
                .path("/").maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, apagar.toString());
    }
}