package com.finance.manager.seguranca;

import com.finance.manager.excecoes.SemPermissaoException;
import com.finance.manager.seguranca.dtos.SecurityRequisicao;
import com.finance.manager.seguranca.dtos.SessaoResposta;
import com.finance.manager.usuario.Usuario;
import com.finance.manager.usuarioempresa.UsuarioEmpresa;
import com.finance.manager.usuarioempresa.UsuarioEmpresaRepositorio;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@PreAuthorize("permitAll()")
public class SecurityControlador {

    private final SecurityServico securityServico;
    private final UsuarioEmpresaRepositorio usuarioEmpresaRepositorio;

    public SecurityControlador(SecurityServico securityServico, UsuarioEmpresaRepositorio usuarioEmpresaRepositorio) {
        this.securityServico = securityServico;
        this.usuarioEmpresaRepositorio = usuarioEmpresaRepositorio;
    }

    @PostMapping
    public ResponseEntity<Void> logar(@Valid @RequestBody SecurityRequisicao requisicao, HttpServletResponse response) {
        securityServico.logar(requisicao, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public SessaoResposta me(@AuthenticationPrincipal UsuarioAutenticado principal) {
        Usuario usuario = principal.getUsuario();

        UsuarioEmpresa vinculo = usuarioEmpresaRepositorio
                .findByUsuarioIdAndEmpresaId(usuario.getId(), principal.getEmpresaId())
                .orElseThrow(() -> new SemPermissaoException(usuario.getEmail()));

        return new SessaoResposta(usuario.getNome(), usuario.getEmail(), principal.getEmpresaId(), vinculo.getRole());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        securityServico.logout(response);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
