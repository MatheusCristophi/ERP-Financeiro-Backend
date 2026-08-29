package com.finance.manager.conta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContaRepositorio extends JpaRepository<Conta, UUID> {
}
