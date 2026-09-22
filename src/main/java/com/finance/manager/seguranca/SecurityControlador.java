package com.finance.manager.seguranca;

import com.finance.manager.seguranca.dtos.SecurityRequisicao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@PreAuthorize("permitAll()")
public class SecurityControlador {

    private final SecurityServico securityServico;

    public SecurityControlador(SecurityServico securityServico) {
        this.securityServico = securityServico;
    }

    @PostMapping
    public ResponseEntity<String> logar(@RequestBody SecurityRequisicao requisicao) {
        String token = securityServico.logar(requisicao);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
