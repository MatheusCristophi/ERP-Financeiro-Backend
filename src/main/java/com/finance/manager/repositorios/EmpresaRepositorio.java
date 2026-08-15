package com.finance.manager.repositorios;

import com.finance.manager.entidades.Empresas;
import com.finance.manager.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmpresaRepositorio extends JpaRepository<Empresas, UUID> {
    List<Empresas> findAllByEmpresaDono(Usuario usuario);
}
