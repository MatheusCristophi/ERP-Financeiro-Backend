ALTER TABLE pessoa_tabela
    ADD COLUMN pessoa_empresa UUID NOT NULL REFERENCES empresa_tabela(empresa_id);
ALTER TABLE pessoa_tabela
    ADD CONSTRAINT uk_pessoa_empresa unique(pessoa_cpf, pessoa_cnpj, pessoa_empresa);