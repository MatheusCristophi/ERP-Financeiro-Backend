package com.finance.manager.empresa;

import com.finance.manager.empresa.dto.EmpresaRequisicao;
import com.finance.manager.empresa.dto.EmpresaResposta;
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
@RequestMapping("empresas")
public class EmpresaControlador {
    private final EmpresaServico empresaServico;

    public EmpresaControlador(EmpresaServico empresaServico) {
        this.empresaServico = empresaServico;
    }

    @GetMapping
    public ResponseEntity<List<EmpresaResposta>> todasEmpresas(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        List<EmpresaResposta> resposta = this.empresaServico.buscarTodasEmpresas(usuario.getUsuario().getId());
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("{empresaId}")
    public ResponseEntity<EmpresaResposta> buscarEmpresa(@AuthenticationPrincipal UsuarioAutenticado usuario, @PathVariable UUID empresaId) {
        EmpresaResposta resposta = this.empresaServico.buscarEmpresaPorId(usuario.getUsuario().getId(), empresaId);
        return ResponseEntity.ok(resposta);
    }

    @PostMapping
    public ResponseEntity<EmpresaResposta> criarEmpresa(@Valid @RequestBody EmpresaRequisicao requisicao,
                                                        @AuthenticationPrincipal UsuarioAutenticado usuario
    ){
        EmpresaResposta resposta = this.empresaServico.criarEmpresa(requisicao, usuario.getUsuario().getId());
        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }

    @PutMapping("{empresaId}")
    public ResponseEntity<EmpresaResposta> atualizarEmpresa(@PathVariable UUID empresaId,
                                                            @Valid
                                                            @RequestBody EmpresaRequisicao requisicao,
                                                            @AuthenticationPrincipal UsuarioAutenticado usuario) {
        EmpresaResposta resposta = this.empresaServico.atualizarEmpresaCompleta(empresaId, usuario.getUsuario().getId(), requisicao);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    @PatchMapping("{empresaId}")
    public ResponseEntity<Void> desativarEmpresa(@PathVariable UUID empresaId,
                                                 @AuthenticationPrincipal UsuarioAutenticado usuario) {
        this.empresaServico.desativarEmpresa(usuario.getUsuario().getId(), empresaId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}