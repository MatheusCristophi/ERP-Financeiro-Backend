package com.finance.manager.empresa;

import com.finance.manager.empresa.dto.EmpresaRequisicao;
import com.finance.manager.empresa.dto.EmpresaResposta;
import com.finance.manager.usuario.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("empresas")
public class EmpresaControlador {
    private final EmpresaServico empresaServico;

    public EmpresaControlador(EmpresaServico empresaServico) {
        this.empresaServico = empresaServico;
    }

    @GetMapping
    public ResponseEntity<List<EmpresaResposta>> todasEmpresas(@AuthenticationPrincipal Usuario usuario) {
        List<EmpresaResposta> resposta = this.empresaServico.buscarTodasEmpresas(usuario.getUsuarioId());
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("{empresaId}")
    public ResponseEntity<EmpresaResposta> buscarEmpresa(@AuthenticationPrincipal Usuario usuario, @PathVariable UUID empresaId) {
        EmpresaResposta resposta = this.empresaServico.buscarEmpresaPorId(usuario.getUsuarioId(), empresaId);
        return ResponseEntity.ok(resposta);
    }

    @PostMapping
    public ResponseEntity<EmpresaResposta> criarEmpresa(@RequestBody EmpresaRequisicao requisicao,
                                                        @AuthenticationPrincipal Usuario usuario){
        EmpresaResposta resposta = this.empresaServico.criarEmpresa(requisicao, usuario.getUsuarioId());
        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }

    @PutMapping("{empresaId}")
    public ResponseEntity<EmpresaResposta> atualizarEmpresa(@PathVariable UUID empresaId,
                                                            @RequestBody EmpresaRequisicao requisicao,
                                                            @AuthenticationPrincipal Usuario usuario) {
        EmpresaResposta resposta = this.empresaServico.atualizarEmpresaCompleta(empresaId, usuario.getUsuarioId(), requisicao);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    @PatchMapping("{empresaId}")
    public ResponseEntity<Void> desativarEmpresa(@PathVariable UUID empresaId,
                                                 @AuthenticationPrincipal Usuario usuario) {
        this.empresaServico.desativarEmpresa(usuario.getUsuarioId(), empresaId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}