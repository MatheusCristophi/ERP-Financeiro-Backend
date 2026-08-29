package com.finance.manager.empresa;

import com.finance.manager.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmpresaRepositorio extends JpaRepository<Empresas, UUID> {
    List<Empresas> findAllByEmpresaDono(Usuario usuario);
}
