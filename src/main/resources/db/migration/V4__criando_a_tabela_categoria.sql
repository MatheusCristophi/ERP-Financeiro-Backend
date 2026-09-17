CREATE TYPE categoria_tipo AS ENUM('A_PAGAR',
    'A_RECEBER'
);

CREATE TABLE categoria_tabela (
    categoria_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    categoria_descricao VARCHAR(255) NOT NULL,
    categoria_tipo categoria_tipo NOT NULL,
    categoria_status BOOLEAN NOT NULL DEFAULT true,
    categoria_empresa UUID NOT NULL REFERENCES empresa_tabela(empresa_id)
);