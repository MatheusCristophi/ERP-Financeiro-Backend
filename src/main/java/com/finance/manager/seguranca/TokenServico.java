package com.finance.manager.seguranca;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenServico {

    @Value("${JWT_SEGREDO}")
    private String chaveSecreta;

    public String gerarToken(UserDetails user) {
        Algorithm algorithm = Algorithm.HMAC256(chaveSecreta);
        String token = JWT.create()
                .withIssuer("erp-financeiro")
                .withSubject(user.getUsername())
                .withExpiresAt(expiracaoToken())
                .sign(algorithm);
        return token;
    }

    public String validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(chaveSecreta);
            return JWT.require(algorithm)
                    .withIssuer("erp-financeiro")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    private Instant expiracaoToken() {
        return Instant.now().plusSeconds(3600);
    }

}