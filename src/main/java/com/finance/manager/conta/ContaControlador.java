package com.finance.manager.conta;

import com.finance.manager.conta.dto.ContaRequisicao;
import com.finance.manager.conta.dto.ContaResposta;
import com.finance.manager.seguranca.UsuarioAutenticado;
import com.finance.manager.usuario.Usuario;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("contas")
public class ContaControlador {
    private final ContaServico contaServico;

    public ContaControlador(ContaServico contaServico) {
        this.contaServico = contaServico;
    }

    @GetMapping
    public ResponseEntity<List<ContaResposta>> buscarTodasAsContas(@AuthenticationPrincipal UsuarioAutenticado usuario, @RequestParam UUID empresaId) {
        List<ContaResposta> resposta = this.contaServico.buscarTodasAsContas(usuario.getUsuario().getId(), empresaId);
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("{contaId}")
    public ResponseEntity<ContaResposta> buscarContaPeloId(@AuthenticationPrincipal UsuarioAutenticado usuario, @RequestParam UUID empresaId, @PathVariable UUID contaId) {
        ContaResposta resposta = this.contaServico.buscarConta(usuario.getUsuario().getId(), empresaId, contaId);
        return ResponseEntity.ok(resposta);
    }

    @PostMapping
    public ResponseEntity<ContaResposta> criarConta(@AuthenticationPrincipal UsuarioAutenticado usuario, @RequestParam UUID empresaId,
                                                    @Valid
                                                    @RequestBody ContaRequisicao requisicao) {
        ContaResposta resposta = this.contaServico.criarConta(usuario.getUsuario().getId(), empresaId, requisicao);
        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }

    @PutMapping("{contaId}")
    public ResponseEntity<ContaResposta> atualizarConta(@AuthenticationPrincipal UsuarioAutenticado usuario, @RequestParam UUID empresaId, @PathVariable UUID contaId,
                                                        @Valid
                                                        @RequestBody ContaRequisicao requisicao) {
        ContaResposta resposta = this.contaServico.atualizarConta(usuario.getUsuario().getId(), empresaId, contaId, requisicao);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    @PatchMapping("{contaId}")
    public ResponseEntity<ContaResposta> desativarConta(@AuthenticationPrincipal UsuarioAutenticado usuario, @RequestParam UUID empresaId, @PathVariable UUID contaId) {
        this.contaServico.desativarConta(usuario.getUsuario().getId(), empresaId, contaId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
