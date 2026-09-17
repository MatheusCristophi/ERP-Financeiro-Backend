CREATE TYPE conta_tipo AS ENUM('CORRENTE',
    'POUPANCA',
    'CAIXA',
    'INVESTIMENTO'
);

CREATE TYPE conta_movimentacao AS ENUM('ENTRADA',
    'SAIDA'
);

CREATE TABLE conta_tabela (
    conta_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    conta_descricao VARCHAR(255) NOT NULL,
    conta_tipo conta_tipo NOT NULL,
    conta_movimentacao conta_movimentacao NOT NULL,
    conta_numero VARCHAR(15) NOT NULL UNIQUE,
    conta_agencia VARCHAR(10) NOT NULL,
    conta_status BOOLEAN NOT NULL DEFAULT true,
    conta_empresa UUID NOT NULL REFERENCES empresa_tabela(empresa_id),
    CONSTRAINT uk_conta_descricao_conta_empresa UNIQUE(conta_descricao, conta_empresa)
);