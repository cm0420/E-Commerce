<div align="center">

<img src="https://capsule-render.vercel.app/api?type=rect&color=0:0d1117,100:0f2a2e&height=120&section=header&text=E-Commerce%20API&fontSize=40&fontColor=e6edf3&fontAlign=50&fontAlignY=42&desc=Backend%20de%20loja%20de%20bicicletas%20%E2%80%94%20do%20cadastro%20ao%20pagamento&descSize=14&descAlignY=72&descAlign=50" alt="E-Commerce API" width="100%" />

![Java](https://img.shields.io/badge/Java_21-0d1117?style=flat-square&logo=openjdk&logoColor=ED8B00)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.5-0d1117?style=flat-square&logo=springboot&logoColor=6DB33F)
![Spring Security](https://img.shields.io/badge/Spring_Security_+_JWT-0d1117?style=flat-square&logo=springsecurity&logoColor=6DB33F)
![MySQL](https://img.shields.io/badge/MySQL_8.4-0d1117?style=flat-square&logo=mysql&logoColor=4479A1)
![Flyway](https://img.shields.io/badge/Flyway-0d1117?style=flat-square&logo=flyway&logoColor=CC0200)
![Testcontainers](https://img.shields.io/badge/Testcontainers-0d1117?style=flat-square&logo=testcontainers&logoColor=5EEAD4)
![Swagger](https://img.shields.io/badge/OpenAPI-0d1117?style=flat-square&logo=swagger&logoColor=85EA2D)
![Status](https://img.shields.io/badge/status-concluído-0d1117?style=flat-square&color=1f6f5c)

</div>

<br />

API REST de um e-commerce para uma loja de bikes, cobrindo o fluxo inteiro: cadastro e autenticação, catálogo, estoque, carrinho, pedido, pagamento e relatório financeiro. O foco foi em boas práticas, segurança e testabilidade, com testes de integração rodando contra um MySQL real.

### `> destaques`

- **Pagamento com Strategy Pattern:** PIX, cartão de crédito e boleto implementados como estratégias intercambiáveis. Um novo método é uma nova classe, sem mexer no fluxo do pedido.
- **Estoque consistente:** validação antes de adicionar ao carrinho e devolução automática quando o pedido é cancelado.
- **Preço congelado:** o carrinho guarda o preço do momento da adição e o pedido guarda o custo, o que permite calcular lucro bruto no relatório financeiro.
- **Segurança:** JWT stateless, três perfis (`CLIENT`, `WORKER`, `ADMIN`) e cartão salvo só com os 4 últimos dígitos (PCI DSS).
- **Validações reais:** CPF pelo algoritmo oficial e telefone brasileiro, com validators customizados do Bean Validation.
- **Soft delete** em usuários, categorias e produtos.
- **26 testes de integração** com Testcontainers + RestAssured.

### `> stack`

| | |
|---|---|
| **Core** | Java 21 · Spring Boot 3.5.12 · Spring Security 6.5 · Spring Data JPA / Hibernate 6.6 |
| **Dados** | MySQL 8.4 · Flyway (11 migrations) |
| **Produtividade** | MapStruct 1.6.3 · Lombok · JJWT 0.12.6 · SpringDoc OpenAPI 2.8 |
| **Testes** | Testcontainers 1.20.4 · RestAssured 5.5 · JUnit 5 |
| **Infra** | Docker Compose (MySQL em dev via Spring Boot Docker Compose Support) |

### `> arquitetura`

Organizada por domínio, e cada domínio tem as próprias camadas:

```
com.miguel.ecommerce
├── auth/          login e emissão de JWT
├── user/          cadastro, perfis, soft delete
├── address/       endereços do cliente
├── category/      categorias
├── product/       produtos (SKU único, preço de custo)
├── stock/         quantidade por produto
├── cart/          carrinho persistido
├── order/         pedidos e status
├── payment/       Strategy: PIX, cartão, boleto
├── financial/     relatório de receita, custo e lucro
├── validator/     CPF e telefone
└── config/        segurança e OpenAPI
```

```
<domínio>/
├── controller/    endpoints REST
├── service/impl/  regras de negócio
├── repository/    Spring Data
├── entity/        JPA
├── dto/           request / response
└── mapper/        MapStruct
```

**Ciclo do pedido:** `PENDING` → `CONFIRMED` (pagamento aprovado) → `SHIPPED` → `DELIVERED`. `CANCELLED` devolve os itens ao estoque. O frete é fixo em R$ 15,00 e o endereço de entrega fica imutável depois do pedido.

### `> como rodar`

Requisitos: **Java 21+** e **Docker**.

```bash
git clone https://github.com/cm0420/E-Commerce.git
cd E-Commerce
cp projeto/.env.example .env        # credenciais do MySQL usadas pelo compose.yaml
```

```env
DB_NAME=ecommerce_db
DB_USER=ecommerce_user
DB_PASSWORD=sua_senha
DB_ROOT_PASSWORD=sua_senha_root
```

Crie `projeto/src/main/resources/application-dev.yaml` (profile ativo por padrão):

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecommerce_db
    username: ecommerce_user
    password: sua_senha
jwt:
  secret: chave_base64_256_bits    # ex: openssl rand -base64 32
  expiration: 86400000
```

```bash
./projeto/mvnw -f projeto/pom.xml spring-boot:run
```

O Spring Boot sobe o container do MySQL pelo `compose.yaml` e o Flyway aplica as migrations na inicialização.

Swagger UI: **http://localhost:8080/swagger-ui/index.html**

### `> autenticação`

```http
POST /api/v1/auth/login
Content-Type: application/json

{ "email": "usuario@email.com", "password": "senha123" }
```

Envie o token retornado em todas as rotas protegidas: `Authorization: Bearer <token>`.

| Perfil | Acesso |
|---|---|
| `CLIENT` | compras, carrinho, pedidos, endereços e cartões |
| `WORKER` | gestão de produtos e estoque |
| `ADMIN` | tudo, incluindo usuários e relatório financeiro |

### `> endpoints`

<details>
<summary><b>Auth e usuários</b></summary>

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| POST | `/api/v1/auth/login` | público | login |
| POST | `/api/v1/users` | público | cadastrar usuário |
| GET | `/api/v1/users` | ADMIN | listar usuários |
| GET | `/api/v1/users/{id}` | ADMIN | buscar por ID |
| PUT | `/api/v1/users/{id}` | ADMIN | atualizar |
| PATCH | `/api/v1/users/{id}/deactivate` | ADMIN | desativar |

</details>

<details>
<summary><b>Endereços</b></summary>

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| POST | `/api/v1/addresses` | CLIENT, ADMIN | criar |
| GET | `/api/v1/addresses/{id}` | CLIENT, ADMIN | buscar por ID |
| GET | `/api/v1/addresses/user/{userId}` | CLIENT, ADMIN | listar por usuário |
| PUT | `/api/v1/addresses/{id}` | CLIENT, ADMIN | atualizar |
| DELETE | `/api/v1/addresses/{id}` | CLIENT, ADMIN | remover |

</details>

<details>
<summary><b>Categorias e produtos</b></summary>

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| POST | `/api/v1/categories` | ADMIN | criar categoria |
| GET | `/api/v1/categories` | público | listar ativas |
| GET | `/api/v1/categories/{id}` | público | buscar por ID |
| PUT | `/api/v1/categories/{id}` | ADMIN | atualizar |
| PATCH | `/api/v1/categories/{id}/deactivate` | ADMIN | desativar |
| POST | `/api/v1/products` | ADMIN, WORKER | criar produto |
| GET | `/api/v1/products` | público | listar ativos |
| GET | `/api/v1/products/{id}` | público | buscar por ID |
| GET | `/api/v1/products/sku/{sku}` | público | buscar por SKU |
| GET | `/api/v1/products/category/{id}` | público | listar por categoria |
| PUT | `/api/v1/products/{id}` | ADMIN, WORKER | atualizar |
| PATCH | `/api/v1/products/{id}/deactivate` | ADMIN, WORKER | desativar |

</details>

<details>
<summary><b>Estoque</b></summary>

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| GET | `/api/v1/stock/product/{id}` | público | consultar |
| PATCH | `/api/v1/stock/product/{id}/add` | ADMIN, WORKER | adicionar quantidade |
| PATCH | `/api/v1/stock/product/{id}/remove` | ADMIN, WORKER | remover quantidade |

</details>

<details>
<summary><b>Carrinho e pedidos</b></summary>

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| GET | `/api/v1/cart` | CLIENT, ADMIN | ver carrinho |
| POST | `/api/v1/cart/items` | CLIENT, ADMIN | adicionar item |
| PATCH | `/api/v1/cart/items/{productId}` | CLIENT, ADMIN | atualizar quantidade |
| DELETE | `/api/v1/cart/items/{productId}` | CLIENT, ADMIN | remover item |
| POST | `/api/v1/orders` | CLIENT, ADMIN | criar pedido a partir do carrinho |
| GET | `/api/v1/orders` | CLIENT, ADMIN | listar meus pedidos |
| GET | `/api/v1/orders/{id}` | CLIENT, ADMIN | buscar por ID |
| PATCH | `/api/v1/orders/{id}/cancel` | CLIENT, ADMIN | cancelar |

</details>

<details>
<summary><b>Pagamento e financeiro</b></summary>

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| POST | `/api/v1/payments` | CLIENT, ADMIN | processar pagamento |
| GET | `/api/v1/payments/cards` | CLIENT, ADMIN | listar cartões |
| POST | `/api/v1/payments/cards` | CLIENT, ADMIN | cadastrar cartão |
| DELETE | `/api/v1/payments/cards/{id}` | CLIENT, ADMIN | remover cartão |
| GET | `/api/v1/financial/report` | ADMIN | receita, custo e lucro bruto por período |

</details>

### `> testes`

Os testes de integração sobem um **MySQL real em Docker** (Testcontainers) e chamam a API por HTTP (RestAssured).

```bash
./projeto/mvnw -f projeto/pom.xml test
./projeto/mvnw -f projeto/pom.xml test -Dtest=OrderControllerTest   # uma classe
```

| Domínio | O que é coberto |
|---|---|
| Auth | login, credenciais inválidas |
| Usuários | CRUD, validações, duplicidade |
| Categorias | CRUD, permissões, duplicidade |
| Produtos | CRUD, SKU duplicado, permissões |
| Estoque | consulta, adição, remoção inválida |
| Carrinho | adição, remoção, estoque insuficiente |
| Pedidos | criação, cancelamento, carrinho vazio |
| Financeiro | relatório, controle de acesso |

### `> produção`

Ative o profile `prod` e defina as variáveis no servidor:

```bash
SPRING_PROFILES_ACTIVE=prod
DB_HOST=...  DB_NAME=...  DB_USER=...  DB_PASSWORD=...
JWT_SECRET=...  JWT_EXPIRATION=86400000
```

<div align="center">
<br />
<sub>Desenvolvido por <a href="https://github.com/cm0420">Miguel Costa</a></sub>
</div>
