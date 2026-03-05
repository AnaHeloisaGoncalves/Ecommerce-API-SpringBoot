# 🛒 Ecommerce API

API RESTful para gerenciamento de um sistema de e-commerce, desenvolvida com **Java 21** e **Spring Boot 3.5**. O projeto cobre o ciclo completo de um pedido: cadastro de clientes e produtos, criação de pedidos com controle de estoque e cancelamento com estorno automático.

---

## 🚀 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.5**
- **Spring Data JPA** — persistência e mapeamento objeto-relacional
- **Spring Web** — criação da API REST
- **Spring Boot Validation** — validação de dados de entrada com Jakarta Bean Validation
- **Springdoc OpenAPI (Swagger)** — documentação interativa da API
- **MySQL** — banco de dados relacional
- **Maven** — gerenciamento de dependências

---

## 📐 Arquitetura

O projeto segue a arquitetura em camadas padrão do Spring:

```
Controller  →  Service  →  Repository  →  Banco de Dados
```

- **Controller** — recebe as requisições HTTP e retorna as respostas
- **Service** — contém as regras de negócio
- **Repository** — comunicação com o banco de dados via Spring Data JPA
- **Entity** — mapeamento das tabelas do banco
- **DTO** — objetos de transferência de dados para as requisições

---

## ⚙️ Como Rodar o Projeto

### Pré-requisitos

- Java 21+
- Maven
- MySQL rodando localmente

### 1. Clone o repositório

```bash
git clone https://github.com/AnaHeloisaGoncalv/ecommerce-api.git
cd ecommerce-api
```

### 2. Configure o banco de dados

Crie um banco de dados MySQL:

```sql
CREATE DATABASE ecommerce_api;
```

### 3. Configure as variáveis de ambiente

Configure as variáveis de ambiente no seu sistema ou na sua IDE:

```
DB_URL=jdbc:mysql://localhost:3306/ecommerce_api
DB_USERNAME=seu_usuario
DB_PASSWORD=sua_senha
```

> ⚠️ Nunca exponha suas credenciais diretamente no `application.properties`. Use variáveis de ambiente.

### 4. Execute a aplicação

```bash
./mvnw spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

### 5. Acesse a documentação interativa (Swagger)

```
http://localhost:8080/swagger-ui/index.html
```

---

## 📋 Endpoints

### 👤 Clientes `/customers`

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/customers` | Cadastrar novo cliente |
| `GET` | `/customers` | Listar todos os clientes |
| `GET` | `/customers/{id}` | Buscar cliente por ID |
| `PATCH` | `/customers/{id}` | Atualizar parcialmente um cliente |
| `DELETE` | `/customers/{id}` | Remover cliente (bloqueado se tiver pedidos) |

**Exemplo de body — POST /customers:**
```json
{
  "firstName": "Ana",
  "lastName": "Gonçalves",
  "email": "ana@email.com",
  "phone": "71999999999",
  "address": "Rua das Flores, 123 - Salvador, BA"
}
```

---

### 📦 Produtos `/products`

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/products` | Cadastrar novo produto |
| `GET` | `/products` | Listar todos os produtos |
| `GET` | `/products/{id}` | Buscar produto por ID |
| `PATCH` | `/products/{id}` | Atualizar parcialmente um produto |
| `DELETE` | `/products/{id}` | Desativar produto (soft delete) |

**Exemplo de body — POST /products:**
```json
{
  "name": "Tênis Esportivo",
  "description": "Tênis para corrida, solado de borracha",
  "sku": "TEN-ESP-001",
  "price": 199.90,
  "stockQuantity": 50,
  "active": true
}
```

---

### 🧾 Pedidos `/orders`

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/orders` | Criar novo pedido |
| `GET` | `/orders` | Listar todos os pedidos |
| `GET` | `/orders/{id}` | Buscar pedido por ID |
| `PATCH` | `/orders/{id}/cancel` | Cancelar pedido e estornar estoque |

**Exemplo de body — POST /orders:**
```json
{
  "customerId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 3,
      "quantity": 1
    }
  ]
}
```

---

## 🧠 Regras de Negócio

- ❌ Não é possível deletar um cliente que possui pedidos associados
- ❌ Não é possível comprar um produto inativo
- ❌ Não é possível comprar mais unidades do que o estoque disponível
- ❌ Não é possível criar um pedido sem itens
- ✅ Ao criar um pedido, o estoque dos produtos é decrementado automaticamente
- ✅ Ao cancelar um pedido, o estoque dos produtos é estornado automaticamente
- ✅ O valor total do pedido é calculado automaticamente com base nos itens
- ✅ Produtos são desativados (soft delete) em vez de removidos permanentemente

---

## ✅ Validações

A API valida todos os dados de entrada e retorna mensagens de erro claras em caso de dados inválidos.

**Cliente:**
- Nome e sobrenome obrigatórios
- Email obrigatório e com formato válido
- Telefone e endereço obrigatórios

**Produto:**
- Nome, descrição e SKU obrigatórios
- Preço obrigatório e maior que zero
- Quantidade em estoque obrigatória e não negativa

**Pedido:**
- ID do cliente obrigatório
- Lista de itens obrigatória com ao menos um item
- ID do produto e quantidade obrigatórios por item
- Quantidade mínima de 1 por item

**Exemplo de resposta de erro (400 Bad Request):**
```json
{
  "firstName": "First name is required",
  "email": "Invalid email format"
}
```

---

## 📊 Modelo de Dados

```
Customer (1) ──── (N) Order (1) ──── (N) OrderItem (N) ──── (1) Product
```

### Status do Pedido

| Status | Descrição |
|--------|-----------|
| `PENDING` | Pedido criado, aguardando pagamento |
| `PAID` | Pagamento confirmado |
| `SHIPPED` | Pedido enviado |
| `DELIVERED` | Pedido entregue |
| `CANCELED` | Pedido cancelado |

---

## 👩🏻‍💻 Autora

**Ana Heloísa Henrique Gonçalves**  
Estudante de Sistemas de Informação — 3° período

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/anaheloisagoncalves)
