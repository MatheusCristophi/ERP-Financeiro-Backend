package com.finance.manager.lancamento;

import com.finance.manager.empresa.Empresas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LancamentoRepositorio extends JpaRepository<Lancamento, UUID> {
    List<Lancamento> findAllByLancamentoEmpresa(Empresas empresa);
}
