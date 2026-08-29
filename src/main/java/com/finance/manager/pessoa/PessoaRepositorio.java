package com.finance.manager.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PessoaRepositorio extends JpaRepository<Pessoa, UUID> {
}
