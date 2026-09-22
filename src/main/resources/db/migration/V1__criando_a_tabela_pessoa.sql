CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TYPE pessoa_tipo AS ENUM('CLIENTE',
    'FORNECEDOR',
    'AMBOS'
);

CREATE TABLE pessoa_tabela (
    pessoa_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    pessoa_nome VARCHAR(255) NOT NULL,
    pessoa_cpf VARCHAR(11) UNIQUE,
    pessoa_cnpj VARCHAR(14) UNIQUE,
    pessoa_tipo pessoa_tipo NOT NULL,
    pessoa_status BOOLEAN NOT NULL DEFAULT true
);