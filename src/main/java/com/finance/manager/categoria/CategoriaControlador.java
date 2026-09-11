package com.finance.manager.categoria;

import com.finance.manager.categoria.dto.CategoriaRequisicao;
import com.finance.manager.categoria.dto.CategoriaResposta;
import com.finance.manager.usuario.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("categoria")
public class CategoriaControlador {

    private final CategoriaServico categoriaServico;

    public CategoriaControlador(CategoriaServico categoriaServico) {
        this.categoriaServico = categoriaServico;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResposta>> buscarTodasCategorias(@AuthenticationPrincipal Usuario usuario, @RequestParam UUID empresaId) {
        List<CategoriaResposta> resposta = categoriaServico.buscarTodasCategorias(empresaId, usuario.getUsuarioId());
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("{categoriaId}")
    public ResponseEntity<CategoriaResposta> buscarCategoria(@AuthenticationPrincipal Usuario usuario, @RequestParam UUID empresaId, @PathVariable UUID categoriaId) {
        CategoriaResposta resposta = categoriaServico.buscarCategoria(empresaId, usuario.getUsuarioId(), categoriaId);
        return ResponseEntity.ok(resposta);
    }

    @PostMapping()
    public ResponseEntity<CategoriaResposta> criarCategoria(@AuthenticationPrincipal Usuario usuario, @RequestParam UUID empresaId, @RequestBody CategoriaRequisicao requisicao) {
        CategoriaResposta resposta = categoriaServico.criarCategoria(empresaId, usuario.getUsuarioId(), requisicao);
        return ResponseEntity.ok(resposta);
    }
}
