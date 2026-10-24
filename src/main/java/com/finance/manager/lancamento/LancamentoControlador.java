package com.finance.manager.lancamento;

import com.finance.manager.lancamento.dto.LancamentoRequisicao;
import com.finance.manager.lancamento.dto.LancamentoResposta;
import com.finance.manager.seguranca.UsuarioAutenticado;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("lancamentos")
public class LancamentoControlador {

    private final LancamentoServico lancamentoServico;

    public LancamentoControlador(LancamentoServico lancamentoServico) {
        this.lancamentoServico = lancamentoServico;
    }

    @GetMapping
    public ResponseEntity<Page<LancamentoResposta>> buscarLancamentos(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                                      @PageableDefault(page = 0, size = 10, sort = "data") Pageable pageable) {
        Page<LancamentoResposta> resposta = lancamentoServico.buscarTodosLancamentos(usuario.getUsuario().getId(), usuario.getEmpresaId(), pageable);

        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    @GetMapping("pessoa/{pessoaId}")
    public ResponseEntity<List<LancamentoResposta>> buscarLancamentosPorPessoa(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                                               @PathVariable UUID pessoaId) {
        List<LancamentoResposta> resposta = lancamentoServico.buscarLancamentosPorPessoa(usuario.getUsuario().getId(), usuario.getEmpresaId(), pessoaId);
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("{lancamentoId}")
    public ResponseEntity<LancamentoResposta> buscarLancamentoPorId(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                                    @PathVariable UUID lancamentoId) {
        LancamentoResposta resposta = lancamentoServico.buscarLancamentoPorId(usuario.getUsuario().getId(), usuario.getEmpresaId(), lancamentoId);
        return ResponseEntity.ok(resposta);
    }

    @PostMapping
    public ResponseEntity<LancamentoResposta> criarLancamento(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                              @RequestParam UUID categoriaId,
                                                              @RequestParam UUID pessoaId,
                                                              @RequestBody LancamentoRequisicao requisicao) throws BadRequestException {
        LancamentoResposta resposta = lancamentoServico.criarLancamentos(usuario.getUsuario().getId(), usuario.getEmpresaId(), categoriaId, pessoaId, requisicao);
        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }

    @PutMapping("{lancamentoId}")
    public ResponseEntity<LancamentoResposta> atualizarLancamento(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                                  @PathVariable UUID lancamentoId,
                                                                  @RequestParam UUID categoriaId,
                                                                  @RequestParam UUID pessoaId,
                                                                  @RequestBody LancamentoRequisicao requisicao) throws BadRequestException {
        LancamentoResposta resposta = lancamentoServico.atualizarLancamentos(usuario.getUsuario().getId(), usuario.getEmpresaId(), lancamentoId, categoriaId, pessoaId, requisicao);
        return ResponseEntity.ok(resposta);
    }

    @DeleteMapping
    public ResponseEntity<Void> deletarLancamento(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                  @RequestParam UUID lancamentoId) {
        lancamentoServico.deletarLancamento(usuario.getUsuario().getId(), usuario.getEmpresaId(), lancamentoId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
