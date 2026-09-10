package com.finance.manager.categoria;

import com.finance.manager.categoria.dto.CategoriaResposta;
import com.finance.manager.usuario.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }
}
