package com.finance.manager.pessoa;

import com.finance.manager.pessoa.dto.PessoaRequisicao;
import com.finance.manager.pessoa.dto.PessoaResposta;
import com.finance.manager.seguranca.UsuarioAutenticado;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("pessoas")
public class PessoaControlador {

    private final PessoaServico pessoaServico;

    public PessoaControlador(PessoaServico pessoaServico) {
        this.pessoaServico = pessoaServico;
    }

    @PostMapping
    public ResponseEntity<PessoaResposta> criarPessoa(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                      @Valid @RequestBody PessoaRequisicao requisicao) {
        PessoaResposta resposta = pessoaServico.criarPessoa(usuario.getUsuario().getId(), usuario.getEmpresaId(), requisicao);
        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PessoaResposta>> buscarTodasPessoas(@AuthenticationPrincipal UsuarioAutenticado usuario) {
        List<PessoaResposta> resposta = pessoaServico.buscarTodasAsPessoas(usuario.getUsuario().getId(), usuario.getEmpresaId());

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{pessoaId}")
    public ResponseEntity<PessoaResposta> buscarPessoaPorId(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                            @PathVariable UUID pessoaId) {
        PessoaResposta resposta = pessoaServico.buscarPessoaPeloId(usuario.getUsuario().getId(), usuario.getEmpresaId(), pessoaId);

        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/{pessoaId}")
    public ResponseEntity<PessoaResposta> atualizarPessoa(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                          @PathVariable UUID pessoaId,
                                                          @Valid @RequestBody PessoaRequisicao requisicao) {
        PessoaResposta resposta = pessoaServico.atualizarDadosDaPessoa(usuario.getUsuario().getId(), usuario.getEmpresaId(), pessoaId, requisicao);

        return ResponseEntity.ok(resposta);
    }

    @PatchMapping("/{pessoaId}")
    public ResponseEntity<Void> desativarPessoa(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                @PathVariable UUID pessoaId) {
        pessoaServico.desativarPessoa(usuario.getUsuario().getId(), usuario.getEmpresaId(), pessoaId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
