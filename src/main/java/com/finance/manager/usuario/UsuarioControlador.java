package com.finance.manager.usuario;

import com.finance.manager.usuario.dto.UsuarioRequisicao;
import com.finance.manager.usuario.dto.UsuarioResposta;
import com.finance.manager.usuarioempresa.UsuarioRoles;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<UsuarioResposta>> buscarUsuarios(@AuthenticationPrincipal Usuario usuario,
                                                                @PathVariable UUID empresaId) {
        List<UsuarioResposta> resposta = this.usuarioServico.buscarUsuarios(usuario.getUsuarioId(), empresaId);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    @GetMapping("/{empresaId}/{usuarioId}")
    public ResponseEntity<UsuarioResposta> buscarUsuario(@AuthenticationPrincipal Usuario usuario,
                                                         @RequestParam UUID empresaId,
                                                         @RequestParam UUID usuarioId) {
        UsuarioResposta resposta = this.usuarioServico.buscarUsuario(usuario.getUsuarioId(), usuarioId, empresaId);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UsuarioResposta> criarUsuario(@AuthenticationPrincipal Usuario usuario,
                                                        @RequestBody UsuarioRequisicao requisicao,
                                                        @RequestParam UsuarioRoles roles,
                                                        @RequestParam UUID empresaId) {
        UsuarioResposta resposta = this.usuarioServico.criarUsuario(usuario.getUsuarioId(), requisicao, roles, empresaId);
        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }
}
