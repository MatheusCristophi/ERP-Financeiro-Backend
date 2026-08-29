package com.finance.manager.repositorios;

import com.finance.manager.entidades.Empresas;
import com.finance.manager.entidades.UsuarioEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UsuarioEmpresaRepositorio extends JpaRepository<UsuarioEmpresa, UUID> {
    List<UsuarioEmpresa> findAllByUsuarioIdAndEmpresaId(UUID usuarioId, UUID empresaId);
    UsuarioEmpresa findByUsuarioIdAndEmpresaId(UUID usuarioId, UUID empresaId);
    List<UsuarioEmpresa> findAllByEmpresaId(UUID empresaId);
    UsuarioEmpresa findByUsuarioId(UUID usuarioId);
}
