package com.finance.manager.seguranca;

import com.finance.manager.excecoes.VinculoNaoEncontrado;
import com.finance.manager.usuarioempresa.UsuarioEmpresa;
import com.finance.manager.usuarioempresa.UsuarioEmpresaRepositorio;
import com.finance.manager.usuarioempresa.UsuarioRoles;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("buscarRoleService")
public class BuscarRoleService {
    private final UsuarioEmpresaRepositorio usuarioEmpresaRepositorio;

    public BuscarRoleService(UsuarioEmpresaRepositorio usuarioEmpresaRepositorio) {
        this.usuarioEmpresaRepositorio = usuarioEmpresaRepositorio;
    }

    public boolean ehDonoOuAdmin(UUID usuarioId, UUID empresaId) {
        UsuarioEmpresa vinculo = usuarioEmpresaRepositorio.findByUsuarioIdAndEmpresaId(usuarioId, empresaId)
                .orElseThrow(() -> new VinculoNaoEncontrado(usuarioId, empresaId));

        if(vinculo.getRole() == UsuarioRoles.DONO || vinculo.getRole() == UsuarioRoles.ADMINISTRADOR_DO_SISTEMA) {
            return true;
        } else {
            return false;
        }
    }

    public boolean ehAnalista(UUID usuarioId, UUID empresaId) {
        UsuarioEmpresa vinculo = usuarioEmpresaRepositorio.findByUsuarioIdAndEmpresaId(usuarioId, empresaId)
                .orElseThrow(() -> new VinculoNaoEncontrado(usuarioId, empresaId));

        if(vinculo.getRole() == UsuarioRoles.ANALISTA_DE_CONTAS_A_PAGAR || vinculo.getRole() == UsuarioRoles.ANALISTA_DE_CONTAS_A_RECEBER) {
            return true;
        } else {
            return false;
        }
    }

    public boolean ehConsultor(UUID usuarioId, UUID empresaId) {
        UsuarioEmpresa vinculo = usuarioEmpresaRepositorio.findByUsuarioIdAndEmpresaId(usuarioId, empresaId)
                .orElseThrow(() -> new VinculoNaoEncontrado(usuarioId, empresaId));

        if(vinculo.getRole() == UsuarioRoles.CONSULTOR) {
            return true;
        } else {
            return false;
        }
    }
}
