CREATE TABLE empresa_usuario_tabela (
    empresa_usuario_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id UUID REFERENCES usuario_tabela(usuario_id),
    empresa_id UUID REFERENCES empresa_tabela(empresa_id),
    usuario_role usuarios_roles NOT NULL,
    CONSTRAINT uk_usuario_id_empresa_id UNIQUE(usuario_id, empresa_id)
);