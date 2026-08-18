package com.finance.manager.controladores;

import com.finance.manager.controladores.empresadto.EmpresaRequisicao;
import com.finance.manager.controladores.empresadto.EmpresaResposta;
import com.finance.manager.servicos.EmpresaServico;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("empresa")
public class EmpresaControlador {
    private final EmpresaServico empresaServico;

    public EmpresaControlador(EmpresaServico empresaServico) {
        this.empresaServico = empresaServico;
    }

    @GetMapping("/buscartodasempresas/{usuarioId}")
    public ResponseEntity<List<EmpresaResposta>> todasEmpresas(@PathVariable UUID usuarioId) {
        List<EmpresaResposta> resposta = this.empresaServico.buscarTodasEmpresas(usuarioId);
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/buscarempresaid/{empresaId}")
    public ResponseEntity<EmpresaResposta> buscarEmpresa(@RequestParam UUID usuarioId, @PathVariable UUID empresaId) {
        EmpresaResposta resposta = this.empresaServico.buscarEmpresaPorId(usuarioId, empresaId);
        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/criarempresa/{usuarioId}")
    public ResponseEntity<EmpresaResposta> criarEmpresa(@RequestBody EmpresaRequisicao requisicao, @PathVariable UUID usuarioId){
        EmpresaResposta resposta = this.empresaServico.criarEmpresa(requisicao, usuarioId);
        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }

    @PutMapping("/atualizarempresa/{empresaId}")
    public ResponseEntity<EmpresaResposta> atualizarEmpresa(@PathVariable UUID empresaId, @RequestBody EmpresaRequisicao requisicao, @RequestParam UUID usuarioId) {
        EmpresaResposta resposta = this.empresaServico.atualizarEmpresaCompleta(empresaId, usuarioId, requisicao);
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }

    @PatchMapping("/desativarempresa/{empresaId}")
    public ResponseEntity<Void> desativarEmpresa(@PathVariable UUID empresaId, @RequestParam UUID usuarioId) {
        this.empresaServico.desativarEmpresa(usuarioId, empresaId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}