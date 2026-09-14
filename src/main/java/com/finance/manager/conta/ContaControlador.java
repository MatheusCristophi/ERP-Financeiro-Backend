package com.finance.manager.conta;

import com.finance.manager.conta.dto.ContaResposta;
import com.finance.manager.usuario.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("{contaId}")
    public ResponseEntity<ContaResposta> buscarContaPeloId(@AuthenticationPrincipal Usuario usuario, @RequestParam UUID empresaId, @PathVariable UUID contaId) {
        ContaResposta resposta = contaServico.buscarConta(usuario.getUsuarioId(), empresaId, contaId);
        return ResponseEntity.ok(resposta);
    }
}
