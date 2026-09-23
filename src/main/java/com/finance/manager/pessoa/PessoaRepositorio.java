package com.finance.manager.pessoa;

import com.finance.manager.empresa.Empresas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PessoaRepositorio extends JpaRepository<Pessoa, UUID> {
    List<Pessoa> findAllByPessoaEmpresa(Empresas empresa);
}
