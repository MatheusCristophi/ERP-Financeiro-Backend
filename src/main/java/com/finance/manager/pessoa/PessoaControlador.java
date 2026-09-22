package com.finance.manager.pessoa;

import com.finance.manager.pessoa.dto.PessoaRequisicao;
import com.finance.manager.pessoa.dto.PessoaResposta;
import com.finance.manager.seguranca.UsuarioAutenticado;
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

    @PostMapping("/{empresaId}")
    public ResponseEntity<PessoaResposta> criarPessoa(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                      @PathVariable UUID empresaId,
                                                      @RequestBody PessoaRequisicao requisicao) {
        PessoaResposta resposta = pessoaServico.criarPessoa(usuario.getUsuario().getId(), empresaId, requisicao);
        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PessoaResposta>> buscarTodasPessoas(@AuthenticationPrincipal UsuarioAutenticado usuario,
                                                                   @RequestParam UUID empresaId) {
        List<PessoaResposta> resposta = pessoaServico.buscarTodasAsPessoas(usuario.getUsuario().getId(), empresaId);

        return ResponseEntity.ok(resposta);
    }
}
