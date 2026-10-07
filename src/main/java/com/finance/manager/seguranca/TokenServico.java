package com.finance.manager.seguranca;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class TokenServico {

    @Value("${JWT_SEGREDO}")
    private String chaveSecreta;

    public String gerarToken(UserDetails user, UUID empresaId) {
        Algorithm algorithm = Algorithm.HMAC256(chaveSecreta);
        String token = JWT.create()
                .withIssuer("erp-financeiro")
                .withSubject(user.getUsername())
                .withClaim("empresaId", empresaId.toString())
                .withExpiresAt(expiracaoToken())
                .sign(algorithm);
        return token;
    }

    public DecodedJWT validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(chaveSecreta);
            return JWT.require(algorithm)
                    .withIssuer("erp-financeiro")
                    .build()
                    .verify(token);
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    private Instant expiracaoToken() {
        return Instant.now().plusSeconds(3600);
    }

}