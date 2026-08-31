package com.finance.manager.seguranca;

import com.finance.manager.seguranca.dtos.SecurityRequisicao;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityServico {

    private final ObjectProvider<AuthenticationManager> authenticationManager;
    private final TokenServico tokenServico;

    public SecurityServico(ObjectProvider<AuthenticationManager> authenticationManager, TokenServico tokenServico) {
        this.authenticationManager = authenticationManager;
        this.tokenServico = tokenServico;
    }

    public String logar(SecurityRequisicao requisicao) {
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(requisicao.email(), requisicao.senha());
        Authentication auth = authenticationManager.getObject().authenticate(userAndPass);
        UserDetails details = (UserDetails) auth;
        var token = tokenServico.gerarToken(details);

        return token;
    }
}