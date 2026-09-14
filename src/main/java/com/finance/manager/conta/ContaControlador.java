package com.finance.manager.conta;

import com.finance.manager.conta.dto.ContaResposta;
import com.finance.manager.usuario.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("conta")
public class ContaControlador {
    private final ContaServico contaServico;

    public ContaControlador(ContaServico contaServico) {
        this.contaServico = contaServico;
    }

    @GetMapping
    public ResponseEntity<List<ContaResposta>> buscarTodasAsContas(@AuthenticationPrincipal Usuario usuario, @RequestParam UUID empresaId) {
        List<ContaResposta> resposta = contaServico.buscarTodasAsContas(usuario.getUsuarioId(), empresaId);
        return ResponseEntity.ok(resposta);
    }
}
