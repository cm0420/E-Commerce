# 🛒 E-Commerce API

API RESTful completa para e-commerce, desenvolvida com **Spring Boot 3.5**, **Java 21** e arquitetura em camadas seguindo as melhores práticas do mercado.

---

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Funcionalidades](#funcionalidades)
- [Pré-requisitos](#pré-requisitos)
- [Configuração do Ambiente](#configuração-do-ambiente)
- [Como Rodar](#como-rodar)
- [Documentação da API](#documentação-da-api)
- [Endpoints](#endpoints)
- [Autenticação](#autenticação)
- [Testes](#testes)
- [Estrutura do Projeto](#estrutura-do-projeto)

---

## 📌 Sobre o Projeto

API backend de um e-commerce completo para uma loja de bikes, cobrindo todo o fluxo desde o cadastro de usuários até o pagamento de pedidos. Desenvolvida com foco em boas práticas, segurança e testabilidade.

---

## 🚀 Tecnologias

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 21 | Linguagem principal |
| Spring Boot | 3.5.12 | Framework principal |
| Spring Security | 6.5 | Autenticação e autorização |
| Spring Data JPA | 3.5 | Persistência de dados |
| Hibernate | 6.6 | ORM |
| MySQL | 8.4 | Banco de dados |
| Flyway | 11.7 | Migrations de banco |
| MapStruct | 1.6.3 | Mapeamento Entity ↔ DTO |
| Lombok | 1.18 | Redução de boilerplate |
| JJWT | 0.12.6 | Geração e validação de tokens JWT |
| SpringDoc OpenAPI | 2.8.16 | Documentação Swagger |
| Docker Compose | - | Orquestração do banco em desenvolvimento |
| Testcontainers | 1.20.4 | Testes de integração |
| RestAssured | 5.5.0 | Testes de API |

---

## 🏗️ Arquitetura

O projeto segue a **arquitetura em camadas por domínio**:

```
com.miguel.ecommerce
├── address/
├── auth/
├── cart/
├── category/
├── config/
│   └── security/
├── exception/
├── financial/
├── order/
├── payment/
├── product/
├── stock/
├── user/
└── validator/
```

Cada domínio contém suas próprias camadas:

```
dominio/
├── controller/     → Endpoints REST
├── service/
│   └── impl/       → Regras de negócio
├── repository/     → Acesso ao banco
├── entity/         → Mapeamento das tabelas
├── dto/
│   ├── request/    → Dados de entrada
│   └── response/   → Dados de saída
└── mapper/         → Conversão Entity ↔ DTO
```

### Padrões utilizados

- **Strategy Pattern** — processamento de pagamentos (PIX, Cartão, Boleto)
- **DTO Pattern** — isolamento da camada de apresentação
- **Repository Pattern** — abstração do acesso a dados
- **Bean Validation** — validação de entrada com validators customizados

---

## ✅ Funcionalidades

### Usuários
- Cadastro com validação de CPF (algoritmo real) e telefone brasileiro
- Autenticação via JWT
- Roles: `CLIENT`, `WORKER`, `ADMIN`
- Soft delete (desativação sem remoção do banco)

### Produtos e Categorias
- CRUD completo de produtos com SKU único
- Categorias com soft delete
- Listagem pública sem autenticação

### Estoque
- Controle de quantidade por produto
- Validação de estoque suficiente antes de adicionar ao carrinho
- Criação automática do estoque ao cadastrar produto

### Carrinho
- Carrinho persistido no banco de dados
- Adição, remoção e atualização de itens
- Cálculo automático de subtotal e total
- Preço salvo no momento da adição

### Pedidos
- Criação a partir do carrinho
- Frete fixo de R$ 15,00
- Status: `PENDING`, `CONFIRMED`, `SHIPPED`, `DELIVERED`, `CANCELLED`
- Cancelamento com devolução automática de estoque
- Endereço de entrega imutável após o pedido

### Pagamento
- Strategy Pattern para múltiplos métodos
- PIX, Cartão de Crédito e Boleto
- Cadastro de cartões (somente últimos 4 dígitos — PCI DSS)
- Atualização automática do status do pedido após aprovação

### Financeiro
- Relatório de receita, custo e lucro bruto por período
- Acesso restrito a ADMIN

---

## 📦 Pré-requisitos

- Java 21+
- Docker Desktop
- Maven (ou usar o `./mvnw` incluído)

---

## ⚙️ Configuração do Ambiente

### 1 — Clone o repositório

```bash
git clone https://github.com/seu-usuario/ecommerce-api.git
cd ecommerce-api
```

### 2 — Configure o arquivo `.env`

Crie um arquivo `.env` na raiz do projeto baseado no `.env.example`:

```bash
cp .env.example .env
```

Preencha as variáveis:

```env
# Banco de Dados
DB_NAME=ecommerce_db
DB_USER=ecommerce_user
DB_PASSWORD=sua_senha
DB_ROOT_PASSWORD=sua_senha_root
```

### 3 — Configure o `application-dev.yaml`

Crie o arquivo `src/main/resources/application-dev.yaml` baseado no exemplo:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecommerce_db
    username: ecommerce_user
    password: sua_senha
    driver-class-name: com.mysql.cj.jdbc.Driver

jwt:
  secret: sua_chave_secreta_base64_256bits
  expiration: 86400000
```

> **Gerar chave JWT segura (PowerShell):**
> ```powershell
> $bytes = New-Object Byte[] 32
> [System.Security.Cryptography.RNGCryptoServiceProvider]::Create().GetBytes($bytes)
> [System.Convert]::ToBase64String($bytes)
> ```

---

## ▶️ Como Rodar

```bash
# Com Maven Wrapper
./mvnw spring-boot:run

# Ou com Maven instalado
mvn spring-boot:run
```

O Spring Boot irá automaticamente subir o container MySQL via Docker Compose antes de iniciar.

As **migrations do Flyway** rodam automaticamente na inicialização.

---

## 📖 Documentação da API

Com a aplicação rodando, acesse o Swagger:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 🔗 Endpoints

### Auth
| Método | Rota | Auth | Descrição |
|---|---|---|---|
| POST | `/api/v1/auth/login` | Público | Login |

### Usuários
| Método | Rota | Auth | Descrição |
|---|---|---|---|
| POST | `/api/v1/users` | Público | Cadastrar usuário |
| GET | `/api/v1/users` | ADMIN | Listar usuários |
| GET | `/api/v1/users/{id}` | ADMIN | Buscar por ID |
| PUT | `/api/v1/users/{id}` | ADMIN | Atualizar |
| PATCH | `/api/v1/users/{id}/deactivate` | ADMIN | Desativar |

### Endereços
| Método | Rota | Auth | Descrição |
|---|---|---|---|
| POST | `/api/v1/addresses` | CLIENT, ADMIN | Criar endereço |
| GET | `/api/v1/addresses/{id}` | CLIENT, ADMIN | Buscar por ID |
| GET | `/api/v1/addresses/user/{userId}` | CLIENT, ADMIN | Listar por usuário |
| PUT | `/api/v1/addresses/{id}` | CLIENT, ADMIN | Atualizar |
| DELETE | `/api/v1/addresses/{id}` | CLIENT, ADMIN | Deletar |

### Categorias
| Método | Rota | Auth | Descrição |
|---|---|---|---|
| POST | `/api/v1/categories` | ADMIN | Criar categoria |
| GET | `/api/v1/categories` | Público | Listar ativas |
| GET | `/api/v1/categories/{id}` | Público | Buscar por ID |
| PUT | `/api/v1/categories/{id}` | ADMIN | Atualizar |
| PATCH | `/api/v1/categories/{id}/deactivate` | ADMIN | Desativar |

### Produtos
| Método | Rota | Auth | Descrição |
|---|---|---|---|
| POST | `/api/v1/products` | ADMIN, WORKER | Criar produto |
| GET | `/api/v1/products` | Público | Listar ativos |
| GET | `/api/v1/products/{id}` | Público | Buscar por ID |
| GET | `/api/v1/products/sku/{sku}` | Público | Buscar por SKU |
| GET | `/api/v1/products/category/{id}` | Público | Listar por categoria |
| PUT | `/api/v1/products/{id}` | ADMIN, WORKER | Atualizar |
| PATCH | `/api/v1/products/{id}/deactivate` | ADMIN, WORKER | Desativar |

### Estoque
| Método | Rota | Auth | Descrição |
|---|---|---|---|
| GET | `/api/v1/stock/product/{id}` | Público | Consultar estoque |
| PATCH | `/api/v1/stock/product/{id}/add` | ADMIN, WORKER | Adicionar quantidade |
| PATCH | `/api/v1/stock/product/{id}/remove` | ADMIN, WORKER | Remover quantidade |

### Carrinho
| Método | Rota | Auth | Descrição |
|---|---|---|---|
| GET | `/api/v1/cart` | CLIENT, ADMIN | Ver carrinho |
| POST | `/api/v1/cart/items` | CLIENT, ADMIN | Adicionar item |
| PATCH | `/api/v1/cart/items/{productId}` | CLIENT, ADMIN | Atualizar quantidade |
| DELETE | `/api/v1/cart/items/{productId}` | CLIENT, ADMIN | Remover item |

### Pedidos
| Método | Rota | Auth | Descrição |
|---|---|---|---|
| POST | `/api/v1/orders` | CLIENT, ADMIN | Criar pedido |
| GET | `/api/v1/orders` | CLIENT, ADMIN | Listar meus pedidos |
| GET | `/api/v1/orders/{id}` | CLIENT, ADMIN | Buscar por ID |
| PATCH | `/api/v1/orders/{id}/cancel` | CLIENT, ADMIN | Cancelar pedido |

### Pagamento
| Método | Rota | Auth | Descrição |
|---|---|---|---|
| POST | `/api/v1/payments` | CLIENT, ADMIN | Processar pagamento |
| GET | `/api/v1/payments/cards` | CLIENT, ADMIN | Listar cartões |
| POST | `/api/v1/payments/cards` | CLIENT, ADMIN | Cadastrar cartão |
| DELETE | `/api/v1/payments/cards/{id}` | CLIENT, ADMIN | Remover cartão |

### Financeiro
| Método | Rota | Auth | Descrição |
|---|---|---|---|
| GET | `/api/v1/financial/report` | ADMIN | Relatório financeiro |

---

## 🔐 Autenticação

A API utiliza **JWT (JSON Web Token)** para autenticação stateless.

### Como usar

**1 — Faça login:**
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "usuario@email.com",
  "password": "senha123"
}
```

**2 — Use o token retornado no header de cada requisição:**
```http
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### Roles disponíveis

| Role | Descrição |
|---|---|
| `CLIENT` | Cliente da loja — compras e perfil |
| `WORKER` | Colaborador — gerencia produtos e estoque |
| `ADMIN` | Administrador — acesso completo |

---

## 🧪 Testes

Os testes de integração utilizam **Testcontainers** para subir um MySQL real em Docker e **RestAssured** para testar os endpoints HTTP.

```bash
# Rodar todos os testes
./mvnw test

# Rodar uma classe específica
./mvnw test -Dtest=UserControllerTest
```

### Cobertura de testes

| Domínio | Testes |
|---|---|
| Auth | Login, credenciais inválidas |
| Usuários | CRUD, validações, duplicidade |
| Categorias | CRUD, permissões, duplicidade |
| Produtos | CRUD, SKU duplicado, permissões |
| Estoque | Consulta, adição, remoção com erro |
| Carrinho | Adição, remoção, estoque insuficiente |
| Pedidos | Criação, cancelamento, carrinho vazio |
| Financeiro | Relatório, controle de acesso |

---

## 📁 Estrutura do Projeto

```
projeto/
├── src/
│   ├── main/
│   │   ├── java/com/miguel/ecommerce/
│   │   │   ├── address/
│   │   │   ├── auth/
│   │   │   ├── cart/
│   │   │   ├── category/
│   │   │   ├── config/
│   │   │   │   └── security/
│   │   │   ├── exception/
│   │   │   ├── financial/
│   │   │   ├── order/
│   │   │   ├── payment/
│   │   │   ├── product/
│   │   │   ├── stock/
│   │   │   ├── user/
│   │   │   └── validator/
│   │   └── resources/
│   │       ├── db/migration/
│   │       │   ├── V1__Create_Table_Users.sql
│   │       │   ├── V2__Create_Table_Addresses.sql
│   │       │   ├── V3__create_categories_table.sql
│   │       │   ├── V4__Create_Table_Products.sql
│   │       │   ├── V5__create_table_stock.sql
│   │       │   ├── V6__create_cart_table.sql
│   │       │   ├── V7__create_order_table.sql
│   │       │   ├── V8__add_shipping_cost_to_orders.sql
│   │       │   ├── V9__create_payment_table.sql
│   │       │   ├── V10__add_cost_price_to_products.sql
│   │       │   └── V11__add_cost_price_to_order_items.sql
│   │       ├── application.yaml
│   │       └── application-prod.yaml
│   └── test/
│       ├── java/com/miguel/ecommerce/
│       │   ├── BaseIntegrationTest.java
│       │   ├── auth/
│       │   ├── category/
│       │   ├── financial/
│       │   ├── order/
│       │   ├── product/
│       │   ├── stock/
│       │   └── user/
│       └── resources/
│           └── application-test.yaml
├── compose.yaml
├── .env.example
├── application-dev.yaml.example
└── pom.xml
```

---

## 🌍 Variáveis de Ambiente em Produção

Em produção defina as variáveis de ambiente no servidor e ative o profile `prod`:

```bash
SPRING_PROFILES_ACTIVE=prod
DB_HOST=seu-host-mysql
DB_NAME=ecommerce_db
DB_USER=usuario
DB_PASSWORD=senha_forte
JWT_SECRET=chave_base64_256bits
JWT_EXPIRATION=86400000
```

---

## 📄 Licença

Este projeto está sob a licença MIT.
