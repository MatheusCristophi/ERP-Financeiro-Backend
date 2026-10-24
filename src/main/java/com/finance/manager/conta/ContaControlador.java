package com.finance.manager.conta;

import com.finance.manager.conta.dto.ContaRequisicao;
import com.finance.manager.conta.dto.ContaResposta;
import com.finance.manager.seguranca.UsuarioAutenticado;
import com.finance.manager.usuario.Usuario;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<List<ContaResposta>> buscarTodasAsContas(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        List<ContaResposta> resposta = this.contaServico.buscarTodasAsContas(usuario.getUsuario().getId(), usuario.getEmpresaId());
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("{contaId}")
    public ResponseEntity<ContaResposta> buscarContaPeloId(@AuthenticationPrincipal UsuarioAutenticado usuario, @PathVariable UUID contaId) {
        ContaResposta resposta = this.contaServico.buscarConta(usuario.getUsuario().getId(), usuario.getEmpresaId(), contaId);
        return ResponseEntity.ok(resposta);
    }

    @PostMapping
    @PreAuthorize("@buscarRoleService.ehDonoOuAdmin(#usuario.usuario.id, #empresaId)")
    public ResponseEntity<ContaResposta> criarConta(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                    @Valid @RequestBody ContaRequisicao requisicao) {
        ContaResposta resposta = this.contaServico.criarConta(usuario.getUsuario().getId(), usuario.getEmpresaId(), requisicao);
        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }

    @PutMapping("{contaId}")
    @PreAuthorize("@buscarRoleService.ehDonoOuAdmin(#usuario.usuario.id, #empresaId)")
    public ResponseEntity<ContaResposta> atualizarConta(@AuthenticationPrincipal UsuarioAutenticado usuario, @PathVariable UUID contaId,
                                                        @Valid @RequestBody ContaRequisicao requisicao) {
        ContaResposta resposta = this.contaServico.atualizarConta(usuario.getUsuario().getId(), usuario.getEmpresaId(), contaId, requisicao);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    @PatchMapping("{contaId}")
    @PreAuthorize("@buscarRoleService.ehDonoOuAdmin(#usuario.usuario.id, #empresaId)")
    public ResponseEntity<ContaResposta> desativarConta(@AuthenticationPrincipal UsuarioAutenticado usuario, @PathVariable UUID contaId) {
        this.contaServico.desativarConta(usuario.getUsuario().getId(), usuario.getEmpresaId(), contaId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
