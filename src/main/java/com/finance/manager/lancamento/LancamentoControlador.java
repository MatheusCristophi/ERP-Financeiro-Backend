package com.finance.manager.lancamento;

import com.finance.manager.lancamento.dto.LancamentoResposta;
import com.finance.manager.seguranca.UsuarioAutenticado;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController("lancamentos")
public class LancamentoControlador {

    private final LancamentoServico lancamentoServico;

    public LancamentoControlador(LancamentoServico lancamentoServico) {
        this.lancamentoServico = lancamentoServico;
    }

    @GetMapping
    public ResponseEntity<List<LancamentoResposta>> buscarLancamentos(@AuthenticationPrincipal UsuarioAutenticado usuario, @RequestParam UUID empresaId) {
        List<LancamentoResposta> resposta = lancamentoServico.buscarTodosLancamentos(usuario.getUsuario().getId(), empresaId);
        return ResponseEntity.ok(resposta);
    }
}
