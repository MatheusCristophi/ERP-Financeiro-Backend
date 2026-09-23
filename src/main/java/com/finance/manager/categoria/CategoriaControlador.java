package com.finance.manager.categoria;

import com.finance.manager.categoria.dto.CategoriaRequisicao;
import com.finance.manager.categoria.dto.CategoriaResposta;
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
@RequestMapping("categorias")
public class CategoriaControlador {

    private final CategoriaServico categoriaServico;

    public CategoriaControlador(CategoriaServico categoriaServico) {
        this.categoriaServico = categoriaServico;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResposta>> buscarTodasCategorias(@AuthenticationPrincipal UsuarioAutenticado usuario, @RequestParam UUID empresaId) {
        List<CategoriaResposta> resposta = this.categoriaServico.buscarTodasCategorias(empresaId, usuario.getUsuario().getId());
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("{categoriaId}")
    public ResponseEntity<CategoriaResposta> buscarCategoria(@AuthenticationPrincipal UsuarioAutenticado usuario, @RequestParam UUID empresaId, @PathVariable UUID categoriaId) {
        CategoriaResposta resposta = this.categoriaServico.buscarCategoria(empresaId, usuario.getUsuario().getId(), categoriaId);
        return ResponseEntity.ok(resposta);
    }

    @PostMapping
    public ResponseEntity<CategoriaResposta> criarCategoria(@AuthenticationPrincipal UsuarioAutenticado usuario, @RequestParam UUID empresaId,
                                                            @Valid
                                                            @RequestBody CategoriaRequisicao requisicao) {
        CategoriaResposta resposta = this.categoriaServico.criarCategoria(empresaId, usuario.getUsuario().getId(), requisicao);
        return new ResponseEntity<>(resposta,HttpStatus.CREATED);
    }

    @PutMapping("{categoriaId}")
    public ResponseEntity<CategoriaResposta> atualizarCategoria(@AuthenticationPrincipal UsuarioAutenticado usuario, @RequestParam UUID empresaId,
                                                                @Valid
                                                                @RequestBody CategoriaRequisicao requisicao, @PathVariable UUID categoriaId) {
        CategoriaResposta resposta = this.categoriaServico.atualizarCategoria(empresaId, usuario.getUsuario().getId(), requisicao, categoriaId);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    @PatchMapping("/{categoriaId}")
    public ResponseEntity<Void> desativarCategoria(@AuthenticationPrincipal UsuarioAutenticado usuario, @RequestParam UUID empresaId, @PathVariable UUID categoriaId) {
        this.categoriaServico.desativarCategoria(empresaId, usuario.getUsuario().getId(), categoriaId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
