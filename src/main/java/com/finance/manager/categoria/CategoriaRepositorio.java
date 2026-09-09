package com.finance.manager.categoria;

import com.finance.manager.empresa.Empresas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoriaRepositorio extends JpaRepository<Categoria, UUID> {
    List<Categoria> findAllByCategoriaEmpresa(Empresas empresa);
}
