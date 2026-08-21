package com.finance.manager.repositorios;

import com.finance.manager.entidades.Usuario;
import com.finance.manager.entidades.UsuarioEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioEmpresaRepositorio extends JpaRepository<UsuarioEmpresa, UUID> {
    UsuarioEmpresa findByUsuarioId(Usuario usuario);
}
