package com.finance.manager.conta;

import com.finance.manager.conta.dto.ContaRequisicao;
import com.finance.manager.conta.dto.ContaResposta;
import com.finance.manager.usuario.Usuario;
import org.springframework.http.HttpStatus;
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
        List<ContaResposta> resposta = this.contaServico.buscarTodasAsContas(usuario.getUsuarioId(), empresaId);
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("{contaId}")
    public ResponseEntity<ContaResposta> buscarContaPeloId(@AuthenticationPrincipal Usuario usuario, @RequestParam UUID empresaId, @PathVariable UUID contaId) {
        ContaResposta resposta = this.contaServico.buscarConta(usuario.getUsuarioId(), empresaId, contaId);
        return ResponseEntity.ok(resposta);
    }

    @PostMapping
    public ResponseEntity<ContaResposta> criarConta(@AuthenticationPrincipal Usuario usuario, @RequestParam UUID empresaId, @RequestBody ContaRequisicao requisicao) {
        ContaResposta resposta = this.contaServico.criarConta(usuario.getUsuarioId(), empresaId, requisicao);
        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }

    @PutMapping("{contaId}")
    public ResponseEntity<ContaResposta> atualizarConta(@AuthenticationPrincipal Usuario usuario, @RequestParam UUID empresaId, @PathVariable UUID contaId, @RequestBody ContaRequisicao requisicao) {
        ContaResposta resposta = this.contaServico.atualizarConta(usuario.getUsuarioId(), empresaId, contaId, requisicao);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    @PatchMapping("contaId")
    public ResponseEntity<ContaResposta> desativarConta(@AuthenticationPrincipal Usuario usuario, @RequestParam UUID empresaId, @PathVariable UUID contaId) {
        this.contaServico.desativarConta(usuario.getUsuarioId(), empresaId, contaId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
