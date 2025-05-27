# 🚚 MovingPack - Sistema de Gestão de Entregas

Este é um projeto simples de backend desenvolvido com foco em demonstrar domínio técnico em diversas tecnologias e boas práticas de desenvolvimento moderno com **Java 21** e **Spring Boot 3.5.0**.

> ⚠️ **Propósito**: Este projeto foi desenvolvido para fins de avaliação técnica, priorizando qualidade de código, organização, testes, integração com APIs externas e uso de containers, ao invés de complexidade de negócio.

---

## 📦 Sobre o Projeto

A ideia é simular uma **plataforma de entregas**. A aplicação possui:

- CRUD de **Motoristas** (`Driver`)
- CRUD de **Entregas** (`Delivery`)
- Relação: _um motorista pode ter muitas entregas, uma entrega pertence a um motorista_
- Consulta de **CEP** via uma **API externa mockada com WireMock**
  - Para cada consulta de CEP deve ser **persistido na base de dados** a data/hora da requisição e os dados retornados
- Validação e persistência dos dados do CEP no banco de dados ao criar entregas

---

## 🔧 Tecnologias Utilizadas

| Tecnologia | Finalidade |
|-----------|------------|
| ☕ **Java 21** | Linguagem principal |
| 🌱 **Spring Boot 3.5.0** | Framework web e de injeção de dependência |
| ✅ **Bean Validation (Jakarta)** | Validações de entrada via anotações |
| 🐘 **PostgreSQL** | Banco de dados relacional |
| 🧬 **Flyway** | Versionamento e execução de migrações SQL |
| 🧪 **JUnit 5 & MockMvc** | Testes de unidade e integração |
| 🧰 **Testcontainers** | Ambientes de testes isolados com PostgreSQL e WireMock |
| 🧱 **WireMock** | Mock de API externa local e durante os testes |
| 🐳 **Docker & Docker Compose** | Containerização da aplicação e ambientes locais |

---

## 🚀 Como Executar Localmente

### 1. ✅ Pré-requisitos

- [Java 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
- [Docker](https://www.docker.com/)
- [Maven](https://maven.apache.org/)
- IDE como IntelliJ ou VSCode

### 2. 🔧 Configuração do ambiente

#### 🔄 Rodando tudo com Docker Compose

##### 🏠 Ambiente de desenvolvimento local:

```bash
docker-compose -f docker-compose.local.yaml up --build
```
- Sobe:
  - Banco PostgreSQL
  - WireMock com mapeamentos de CEP

##### 🧪 Rodando a aplicação via IDE (recomendado para desenvolvimento)

- Rodar a aplicação diretamente com o perfil `local`
- Garantir que o **Docker Compose** está ativo para os serviços `postgres` e `wiremock`
- A aplicação conecta com:
  - `jdbc:postgresql://localhost:5432/moving_pack`
  - `http://localhost:8081` (WireMock)

#### 🧰 Simulação de ambiente produtivo:
```bash
docker-compose -f docker-compose.yaml up --build
```
- Sobe:
  - Banco PostgreSQL
  - WireMock com mapeamentos de CEP
  - Container da aplicação se desejar rodar fora da IDE

---

## 🧪 Testes

A aplicação possui cobertura de:

- ✅ Testes unitários de serviços e repositórios
- 🧪 Testes de integração de endpoints REST usando `MockMvc`
- 🌐 Testes de integração com `WireMock` e `PostgreSQL` via `Testcontainers`

Para rodar os testes:

```bash
./mvnw test
```

---

## 🗂 Estrutura do Projeto

```
src
├── main
│   ├── java/com/movingpack
│   │   ├── config           # Classes de configuração
│   │   ├── delivery         # CRUD de entregas
│   │   ├── driver           # CRUD de motoristas
│   │   ├── exceptionhandler # Handlers para Exceptions
│   │   ├── externalapi      # Client para chamadas externas
│   │   │   └── dto          # DTOs
│   │   ├── postalcode       # Consumo de API externa (WireMock)
│   │       └── history      # Classes para persistencia em banco das consultas
│   └── resources
│       └── db/migration     # Arquivos Flyway (.sql)
└── test
    ├── java/com/movingpack                 
    │   ├── config           # Configuração Testcontainers e WireMock
    │   ├── integration      # Testes com Testcontainers e WireMock
    │   └── unit             # Testes unitários
    └── resources
        └── wiremock         # Arquivos Wiremock
            ├── __files      # Arquivos de response (.json)
            └── mapping      # Mapeamentos (.json)
```

---

## 🧰 Migrações com Flyway

As migrações SQL são automaticamente aplicadas no startup da aplicação.

Estrutura:
```
src/main/resources/db/migration/
├── V1__init.sql
├── V2__create_driver.sql
├── V3__create_delivery.sql
```

Para executar manualmente:

```bash
./mvnw flyway:migrate
```

> Obs: certifique-se que as variáveis de ambiente do banco estão definidas

---

## 🔗 Endpoints Principais

- `GET /api/v1/driver`
- `POST /api/v1/driver`
- `GET /api/v1/delivery`
- `POST /api/v1/delivery` _(realiza consulta automática de CEP)_
- `GET /api/v1/postal-code/consult/{code}` _(consulta manual ao WireMock)_

---

## 🤖 WireMock – Mock da API Externa

A aplicação simula uma chamada real à API de CEP via:

```
/api/v1/postal-code/consult/{cep}
```

O WireMock responde com dados de CEP conforme arquivos `__files` e `mappings` em:

```
src/test/resources/wiremock
```

---

## 📸 Exemplo de Resposta de Consulta de CEP

```json
{
  "success": true,
  "data": {
    "cep": "03255-000",
    "logradouro": "Rua Exemplo",
    "bairro": "Centro",
    "cidade": "São Paulo",
    "uf": "SP"
  },
  "status": 200
}
```

---

## 📚 Conclusão

Este projeto demonstra:

- 🧼 Estrutura limpa com separação de responsabilidades
- ⚙️ Configuração robusta para testes e produção
- 🧪 Cobertura com testes reais e mockados
- 🐳 Deploy local com Docker e WireMock
- 🗃 Controle de versões de banco com Flyway

> 💡 Mesmo simples, o projeto reflete boas práticas e um domínio sólido do ecossistema Spring moderno.

---

**© Desenvolvido por Carlos Santana — 2025**