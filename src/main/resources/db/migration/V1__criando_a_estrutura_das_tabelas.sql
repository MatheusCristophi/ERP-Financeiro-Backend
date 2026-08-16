CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TYPE usuarios_roles AS ENUM('DONO',
    'ADMINISTRADOR_DO_SISTEMA',
    'ANALISTA_DE_CONTAS_A_PAGAR',
    'ANALISTA_DE_CONTAS_A_RECEBER',
    'CONSULTOR'
);

CREATE TYPE lancamento_status AS ENUM('PAGO',
    'CANCELADO',
    'PENDENTE',
    'VENCIDO'
);

CREATE TYPE pessoa_tipo AS ENUM('CLIENTE',
    'FORNECEDOR',
    'AMBOS'
);

CREATE TYPE conta_tipo AS ENUM('CORRENTE',
    'POUPANCA',
    'CAIXA',
    'INVESTIMENTO'
);

CREATE TYPE conta_movimentacao AS ENUM('ENTRADA',
    'SAIDA'
);

CREATE TYPE empresa_tipo_atividade AS ENUM('INDUSTRIAL',
    'SERVICO',
    'COMERCIO',
    'FINANCEIRA',
    'IMOBILIARIA'
);

CREATE TYPE empresa_regime_tibutario AS ENUM('MEI',
    'SIMPLES_NACIONAL',
    'LUCRO_REAL',
    'LUCRO_PRESUMIDO'
);

CREATE TYPE empresa_natureza AS ENUM('EMPRESARIO_INDIVIDUAL',
    'EIRELI',
    'SOCIEDADE_EMPRESARIA_LIMITADA',
    'SOCIEDADE_ANONIMA',
    'SOCIEDADE_SIMPLES'
);

CREATE TYPE categoria_tipo AS ENUM('A_PAGAR',
    'A_RECEBER'
);

CREATE TABLE pessoa_tabela (
    pessoa_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    pessoa_nome VARCHAR(255) NOT NULL,
    pessoa_cpf VARCHAR(11) UNIQUE,
    pessoa_cnpj VARCHAR(14) UNIQUE,
    pessoa_tipo pessoa_tipo NOT NULL
);

CREATE TABLE usuario_tabela (
    usuario_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_nome VARCHAR(255) NOT NULL UNIQUE,
    usuario_email VARCHAR(255) NOT NULL UNIQUE,
    usuario_senha VARCHAR(255) NOT NULL,
    usuario_ativo BOOLEAN NOT NULL DEFAULT true,
    usuario_pessoas UUID REFERENCES pessoa_tabela(pessoa_id)
);

CREATE TABLE empresa_tabela (
    empresa_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    empresa_descricao VARCHAR NOT NULL,
    empresa_tipo empresa_tipo_atividade NOT NULL,
    empresa_regime empresa_regime_tibutario NOT NULL,
    empresa_natureza_pessoa empresa_natureza NOT NULL,
    empresa_dono UUID NOT NULL REFERENCES usuario_tabela(usuario_id),
    empresa_status BOOLEAN DEFAULT true,
    CONSTRAINT uk_empresa_dono_empresa_descricao UNIQUE(empresa_descricao, empresa_dono)
);

CREATE TABLE categoria_tabela (
    categoria_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    categoria_descricao VARCHAR(255) NOT NULL,
    categoria_tipo categoria_tipo NOT NULL,
    categoria_status BOOLEAN NOT NULL DEFAULT true,
    categoria_empresa UUID NOT NULL REFERENCES empresa_tabela(empresa_id)
);

CREATE TABLE empresa_usuario_tabela (
    empresa_usuario_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id UUID REFERENCES usuario_tabela(usuario_id),
    empresa_id UUID REFERENCES empresa_tabela(empresa_id),
    usuario_role usuarios_roles NOT NULL,
    CONSTRAINT uk_usuario_id_empresa_id UNIQUE(usuario_id, empresa_id)
);

CREATE TABLE conta_tabela (
    conta_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    conta_descricao VARCHAR(255) NOT NULL,
    conta_tipo conta_tipo NOT NULL,
    conta_movimentacao conta_movimentacao NOT NULL,
    conta_numero VARCHAR(15) NOT NULL UNIQUE,
    conta_agencia VARCHAR(10) NOT NULL,
    conta_empresa UUID NOT NULL REFERENCES empresa_tabela(empresa_id),
    CONSTRAINT uk_conta_descricao_conta_empresa UNIQUE(conta_descricao, conta_empresa)
);

CREATE TABLE lancamento_tabela(
    lancamento_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    lancamento_descricao VARCHAR(255) NOT NULL,
    lancamento_empresa UUID NOT NULL REFERENCES empresa_tabela(empresa_id),
    lancamento_pessoa UUID REFERENCES pessoa_tabela(pessoa_id),
    lancamento_status lancamento_status DEFAULT 'PENDENTE',
    lancamento_valor DECIMAL(13,2) NOT NULL,
    lancamento_categoria UUID REFERENCES categoria_tabela(categoria_id),
    lancamento_movimentacao conta_movimentacao NOT NULL,
    lancamento_usuario UUID REFERENCES usuario_tabela(usuario_id),
    lancamento_data_emissao TIMESTAMPTZ NOT NULL DEFAULT now(),
    lancamento_data_vencimento TIMESTAMPTZ NOT NULL,
    lancamento_data_pagamento TIMESTAMPTZ,
    lancamento_observacao VARCHAR(255),
    UNIQUE(lancamento_id, lancamento_pessoa)
);