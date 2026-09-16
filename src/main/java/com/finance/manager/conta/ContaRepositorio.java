package com.finance.manager.conta;

import com.finance.manager.empresa.Empresas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContaRepositorio extends JpaRepository<Conta, UUID> {
    List<Conta> findAllByContaEmpresa(Empresas empresa);
}
