CREATE TYPE lancamento_status AS ENUM('PAGO',
    'CANCELADO',
    'PENDENTE',
    'VENCIDO'
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