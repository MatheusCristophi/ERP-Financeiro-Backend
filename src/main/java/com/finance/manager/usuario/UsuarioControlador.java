package com.finance.manager.usuario;

import com.finance.manager.seguranca.UsuarioAutenticado;
import com.finance.manager.usuario.dto.UsuarioRequisicao;
import com.finance.manager.usuario.dto.UsuarioResposta;
import com.finance.manager.usuarioempresa.UsuarioRoles;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioControlador {

    private final UsuarioServico usuarioServico;

    public UsuarioControlador(UsuarioServico usuarioServico) {
        this.usuarioServico = usuarioServico;
    }

    @GetMapping("/{empresaId}")
    public ResponseEntity<List<UsuarioResposta>> buscarUsuarios(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                                @PathVariable UUID empresaId) {
        List<UsuarioResposta> resposta = this.usuarioServico.buscarUsuarios(usuario.getUsuario().getId(), empresaId);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UsuarioResposta> buscarUsuario(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                         @RequestParam UUID empresaId,
                                                         @RequestParam UUID usuarioId) {
        UsuarioResposta resposta = this.usuarioServico.buscarUsuario(usuario.getUsuario().getId(), usuarioId, empresaId);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("@buscarRoleService.ehDonoOuAdmin(#usuario.usuario.id, #empresaId)")
    public ResponseEntity<UsuarioResposta> criarUsuario(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                        @Valid
                                                        @RequestBody UsuarioRequisicao requisicao,
                                                        @RequestParam UsuarioRoles roles,
                                                        @RequestParam UUID empresaId) {
        UsuarioResposta resposta = this.usuarioServico.criarUsuario(usuario.getUsuario().getId(), requisicao, roles, empresaId);
        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }

    @PutMapping("/{roles}")
    @PreAuthorize("@buscarRoleService.ehDonoOuAdmin(#usuario.usuario.id, #empresaId)")
    public ResponseEntity<UsuarioResposta> atualizarUsuario(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                            @Valid
                                                            @RequestBody UsuarioRequisicao requisicao,
                                                            @PathVariable UsuarioRoles roles,
                                                            @RequestParam UUID usuarioId,
                                                            @RequestParam UUID empresaId){
        UsuarioResposta resposta = this.usuarioServico.atualizarUsuario(usuario.getUsuario().getId(),usuarioId, roles, requisicao, empresaId);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    @PatchMapping
    @PreAuthorize("@buscarRoleService.ehDonoOuAdmin(#usuario.usuario.id, #empresaId)")
    public ResponseEntity<Void> desativarUsuario(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                            @RequestParam UUID usuarioId,
                                                            @RequestParam UUID empresaId) {
        this.usuarioServico.desativarUsuario(usuario.getUsuario().getId(), usuarioId, empresaId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
