package com.finance.manager.repositorios;

import com.finance.manager.entidades.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoriaRepositorio extends JpaRepository<Categoria, UUID> {
}
