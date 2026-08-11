package com.finance.manager.repositorios;

import com.finance.manager.entidades.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContaRepositorio extends JpaRepository<Conta, UUID> {
}
