package com.finance.manager.controladores;

import com.finance.manager.controladores.empresadto.EmpresaResposta;
import com.finance.manager.servicos.EmpresaServico;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("empresa")
public class EmpresaControlador {
    private final EmpresaServico empresaServico;

    public EmpresaControlador(EmpresaServico empresaServico) {
        this.empresaServico = empresaServico;
    }

    @GetMapping("/buscartodasempresas/{usuarioId}")
    public ResponseEntity<List<EmpresaResposta>> todasEmpresas(@PathVariable UUID usuarioId) {
        List<EmpresaResposta> response = this.empresaServico.buscarTodasEmpresas(usuarioId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscarempresaid/{empresaId}")
    public ResponseEntity<EmpresaResposta> buscarEmpresa(@RequestParam UUID usuarioId, @PathVariable UUID empresaId) {
        EmpresaResposta response = this.empresaServico.buscarEmpresaPorId(usuarioId, empresaId);
        return ResponseEntity.ok(response);
    }
}