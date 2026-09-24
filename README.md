# Finance Manager — ERP Financeiro

Sistema de gestão financeira multi-empresa (contas a pagar/receber, categorias, contas bancárias e controle de acesso por empresa), desenvolvido em Java/Spring Boot.

Este documento detalha o escopo planejado para a v1 (versão inicial) e o que foi deliberadamente adiado para a v2.

---

## V1 — Escopo atual

### Backend

**Stack:** Java, Spring Boot, JPA/Hibernate, Flyway, PostgreSQL, Spring Security + JWT

**Entidades**
- `Usuario` — conta de acesso ao sistema
- `Empresa` — empresa cadastrada, com dono fixo (`empresa_dono`)
- `UsuarioEmpresa` — tabela de junção explícita entre usuário e empresa, carregando o papel (`role`) do usuário **naquela empresa específica**. O papel não é global: o mesmo usuário pode ser `DONO` em uma empresa e `CONSULTOR` em outra
- `Pessoa` — cliente/fornecedor, compartilhado entre empresas via relação própria (evita duplicar CPF/CNPJ quando a mesma pessoa é cliente de mais de uma empresa do sistema)
- `Conta` — conta bancária/caixa da empresa
- `Categoria` — categoria de lançamento (a pagar/a receber), por empresa
- `Lancamento` — o núcleo do sistema: contas a pagar e a receber, com status (`PENDENTE`, `PAGO`, `VENCIDO`, `CANCELADO`), vinculado a empresa, categoria, conta e opcionalmente pessoa

**API REST**
- CRUD completo para Empresa, Conta, Categoria, Pessoa, Lançamento e Usuário
- Busca de lançamentos por pessoa e por empresa
- Desativação (soft delete) via status, em vez de exclusão física

**Autenticação e autorização**
- Login via JWT
- Permissões avaliadas por vínculo `UsuarioEmpresa`, não por authority fixa no token — o papel do usuário é sempre resolvido no contexto da empresa da requisição
- Autorização declarativa via `@PreAuthorize`, apoiada em um serviço dedicado (`BuscarRoleService`) que resolve o papel do usuário na empresa (dono/administrador, analista, consultor); já protege os endpoints de Conta e Usuário
- Checagem de posse de recurso (IDOR) em endpoints que recebem ID de entidade — garante que um usuário não acesse dado de uma empresa à qual não pertence, mesmo estando autenticado

**Isolamento multi-tenant**
- Toda entidade de negócio (`Conta`, `Categoria`, `Pessoa`, `Lancamento`) carrega `empresa_id`, garantindo que dados de uma empresa nunca vazem para outra

**Segurança**
- CSRF desabilitado (autenticação stateless via JWT, sem cookie de sessão — o vetor de ataque que CSRF protege não se aplica)
- Proteção contra SQL Injection via JPA/Hibernate (queries parametrizadas)
- Validação de entrada nos DTOs (Bean Validation)
- Tratamento de erro centralizado, sem vazar stack trace para o cliente

**Banco de dados**
- Migrations versionadas via Flyway (schema como fonte da verdade, não geração automática pelo Hibernate)
- `ddl-auto: validate` — Hibernate apenas confere consistência, nunca altera o schema

---

## V2 — Escopo adiado

### Funcionalidades de domínio

| Item | Descrição |
|---|---|
| **Custos fixos** | Lançamentos recorrentes gerados automaticamente (aluguel, assinatura, salário) |
| **Numeração sequencial amigável** | Número legível por empresa (ex: "Lançamento #47") em vez de expor o UUID ao usuário; requer contador por empresa com controle de concorrência |
| **Integração com Asaas** | Emissão de boleto/PIX e webhook de confirmação de pagamento |
| **Relatórios e consultas** | Voltado principalmente ao papel `CONSULTOR`, que hoje não tem funcionalidade própria além do acesso de leitura — relatórios financeiros e consultas agregadas sobre os dados da empresa |

### Infraestrutura — escalabilidade horizontal

| Item | Motivo do adiamento |
|---|---|
| **Load balancer** | Só necessário ao rodar múltiplas instâncias da API simultaneamente; add-on separado no Lightsail (~US$18/mês adicional) |
| **Rate limiting distribuído** | Sem múltiplas instâncias, não há necessidade — fica postergado para já nascer na forma distribuída |
| **Redis** | Sustenta o rate limiting distribuído e cache compartilhado entre instâncias. Não é oferecido como serviço gerenciado pelo Lightsail — precisa rodar em instância própria ou serviço externo (Upstash, Redis Cloud, AWS ElastiCache) |

**Critério de adiamento:** os itens de infraestrutura da v2 resolvem problemas que **só existem em escala horizontal**. Implementá-los antes de ter múltiplas instâncias adicionaria custo e complexidade sem benefício correspondente — o gatilho para migrar é o volume de uso justificar escalar além de uma instância única, não uma data ou preferência arbitrária.

---

## Notas de arquitetura

- **Papéis por empresa, não globais** — decisão central do modelo: `UsuarioEmpresa.role` existe porque um usuário pode ter vínculo com várias empresas, cada uma com um papel diferente. Nenhuma entidade guarda um "role" fixo no `Usuario`.
- **Autorização resolvida por serviço dedicado** — `BuscarRoleService` centraliza a lógica de "esse usuário é dono/admin, analista ou consultor nessa empresa", reaproveitada via `@PreAuthorize` em vez de repetir a checagem manualmente em cada service.
- **`Pessoa` desacoplada de `Empresa`** — pessoa física/jurídica é única no sistema; o vínculo com cada empresa (cliente/fornecedor) é modelado à parte, evitando duplicar CPF/CNPJ quando a mesma pessoa transaciona com mais de uma empresa cadastrada.
- **Saldo é sempre calculado, nunca armazenado** — evita dessincronia entre o valor salvo e a realidade dos lançamentos.
