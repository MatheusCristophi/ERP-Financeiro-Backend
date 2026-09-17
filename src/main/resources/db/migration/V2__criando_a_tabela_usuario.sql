CREATE TYPE usuarios_roles AS ENUM('DONO',
    'ADMINISTRADOR_DO_SISTEMA',
    'ANALISTA_DE_CONTAS_A_PAGAR',
    'ANALISTA_DE_CONTAS_A_RECEBER',
    'CONSULTOR'
);

CREATE TABLE usuario_tabela (
    usuario_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_nome VARCHAR(255) NOT NULL UNIQUE,
    usuario_email VARCHAR(255) NOT NULL UNIQUE,
    usuario_senha VARCHAR(255) NOT NULL,
    usuario_ativo BOOLEAN NOT NULL DEFAULT true,
    usuario_pessoas UUID REFERENCES pessoa_tabela(pessoa_id)
);