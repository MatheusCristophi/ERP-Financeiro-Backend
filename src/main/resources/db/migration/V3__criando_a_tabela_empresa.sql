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

CREATE TABLE empresa_tabela (
    empresa_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    empresa_descricao VARCHAR NOT NULL,
    empresa_cnpj VARCHAR NOT NULL,
    empresa_saldo NUMERIC(13, 2) DEFAULT 0,
    empresa_tipo empresa_tipo_atividade NOT NULL,
    empresa_regime empresa_regime_tibutario NOT NULL,
    empresa_natureza_pessoa empresa_natureza NOT NULL,
    empresa_dono UUID NOT NULL REFERENCES usuario_tabela(usuario_id),
    empresa_status BOOLEAN DEFAULT true,
    CONSTRAINT uk_empresa_dono_empresa_descricao UNIQUE(empresa_descricao, empresa_dono)
);