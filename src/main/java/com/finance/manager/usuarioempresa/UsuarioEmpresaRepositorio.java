package com.finance.manager.usuarioempresa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioEmpresaRepositorio extends JpaRepository<UsuarioEmpresa, UUID> {
    boolean existsByUsuarioIdAndEmpresaId(UUID usuarioId, UUID empresaId);
    Optional<UsuarioEmpresa> findByUsuarioIdAndEmpresaId(UUID usuarioId, UUID empresaId);
    List<UsuarioEmpresa> findAllByEmpresaId(UUID empresaId);
}
