package com.finance.manager.usuario;

import com.finance.manager.usuario.dto.UsuarioResposta;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        List<UsuarioResposta> resposta = usuarioServico.buscarUsuarios(usuario.getUsuarioId(), empresaId);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    @GetMapping("/{usuarioId}/{empresaId}")
    public ResponseEntity<UsuarioResposta> buscarUsuario(@AuthenticationPrincipal Usuario usuario,
                                                         @PathVariable UUID usuarioId,
                                                         @PathVariable UUID empresaId) {
        UsuarioResposta resposta = usuarioServico.buscarUsuario(usuario.getUsuarioId(), usuarioId, empresaId);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }
}
