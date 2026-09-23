package com.finance.manager.lancamento;

import com.finance.manager.lancamento.dto.LancamentoRequisicao;
import com.finance.manager.lancamento.dto.LancamentoResposta;
import com.finance.manager.seguranca.UsuarioAutenticado;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController("lancamentos")
public class LancamentoControlador {

    private final LancamentoServico lancamentoServico;

    public LancamentoControlador(LancamentoServico lancamentoServico) {
        this.lancamentoServico = lancamentoServico;
    }

    @GetMapping
    public ResponseEntity<List<LancamentoResposta>> buscarLancamentos(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                                      @RequestParam UUID empresaId) {
        List<LancamentoResposta> resposta = lancamentoServico.buscarTodosLancamentos(usuario.getUsuario().getId(), empresaId);
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("{pessoaId}")
    public ResponseEntity<List<LancamentoResposta>> buscarLancamentosPorPessoa(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                                               @RequestParam UUID empresaId,
                                                                               @PathVariable UUID pessoaId) {
        List<LancamentoResposta> resposta = lancamentoServico.buscarLancamentosPorPessoa(usuario.getUsuario().getId(), empresaId, pessoaId);
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("{lancamentoId}")
    public ResponseEntity<LancamentoResposta> buscarLancamentoPorId(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                                    @RequestParam UUID empresaId,
                                                                    @PathVariable UUID lancamentoId) {
        LancamentoResposta resposta = lancamentoServico.buscarLancamentoPorId(usuario.getUsuario().getId(), empresaId, lancamentoId);
        return ResponseEntity.ok(resposta);
    }

    @PostMapping
    public ResponseEntity<LancamentoResposta> criarLancamento(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                              @RequestParam UUID empresaId,
                                                              @RequestParam UUID categoriaId,
                                                              @RequestParam UUID pessoaId,
                                                              @RequestBody LancamentoRequisicao requisicao) {
        LancamentoResposta resposta = lancamentoServico.criarLancamentos(usuario.getUsuario().getId(), empresaId, categoriaId, pessoaId, requisicao);
        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }
}
