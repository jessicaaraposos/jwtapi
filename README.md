# JWT API

## 📌 Visão Geral
O projeto **JWT API** fornece uma API REST para **geração e validação de tokens JWT**, garantindo segurança na autenticação de usuários. Utiliza **Spring Boot**, **Springdoc OpenAPI** para documentação, e boas práticas de engenharia de software, incluindo princípios **SOLID**, **Design de API** e **observabilidade**.

## 🛠️ Tecnologias Utilizadas
- **Java 17**
- **Spring Boot 3.4.2**
- **Spring Security**
- **Springdoc OpenAPI** (Swagger UI)
- **Maven**
- **JUnit 5 & Mockito**
- **SLF4J & Logback** (Observabilidade)

## 🏗️ Arquitetura e Princípios de Engenharia

### 🔹 **Abstração**
A solução encapsula a complexidade da geração e validação de tokens JWT dentro da classe `JwtService`, fornecendo uma interface simples para outras partes da aplicação consumirem sem precisar conhecer detalhes da implementação interna.

### 🔹 **Baixo Acoplamento**
A API foi projetada para minimizar dependências entre classes, favorecendo **injeção de dependências** e **uso de interfaces e enums**, como `UserRoleEnum` para padronizar permissões.

### 🔹 **Alta Coesão**
Cada classe tem uma única responsabilidade bem definida:
- `JwtService` → Geração e validação de tokens
- `JwtController` → Exposição dos endpoints
- `GlobalExceptionHandler` → Tratamento de exceções centralizado
- `PrimeNumberUtil` → Validação de números primos

### 🔹 **Extensibilidade**
A estrutura do projeto facilita novas implementações sem impactar o código existente. Exemplo: podemos adicionar um **mecanismo de refresh tokens** sem modificar `JwtService`, apenas criando um novo serviço específico.

### 🔹 **Design de API**
A API segue boas práticas:
- **Endpoints RESTful** (`/api/jwt/generate` e `/api/jwt/validate`)
- **Utilização do padrão HTTP correto** (`200 OK`, `401 Unauthorized`, `400 Bad Request`)
- **Swagger UI** para documentação interativa
- **Mensagens de erro padronizadas**

### 🔹 **Princípios SOLID**
- **S** (Single Responsibility) → Cada classe tem uma responsabilidade única
- **O** (Open/Closed) → Facilmente extensível sem modificar o código base
- **L** (Liskov Substitution) → Uso adequado de herança e contratos de interfaces
- **I** (Interface Segregation) → Nenhuma classe depende de métodos que não usa
- **D** (Dependency Inversion) → Uso de injeção de dependências com Spring

### 🔹 **Observabilidade**
- **Logs estruturados** com SLF4J e Logback
- **Tratamento de erros centralizado** via `GlobalExceptionHandler`
- **Mensagens informativas e warnings** para rastreamento eficiente

## 📖 Documentação da API
A API está documentada usando **Swagger UI** e pode ser acessada em:
```
http://localhost:8080/swagger-ui.html
```

## 🚀 Como Executar o Projeto
1. Clone o repositório:
   ```sh
   git clone https://github.com/jessicaaraposos/jwtapi.git
   ```
2. Entre no diretório do projeto:
   ```sh
   cd jwtapi
   ```
3. Compile e execute o projeto com Maven:
   ```sh
   mvn spring-boot:run
   ```
4. Acesse a API via Swagger UI em `http://localhost:8080/swagger-ui.html`

## 🚀 Massa de teste
### Caso 1:
- Entrada:
   ```
   eyJhbGciOiJIUzM4NCJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.W7Y0RU7HSvvUB7ve76QVH2JQw9H1icQgP38rlrvfrLOBXanFDuSR8anM4ieiaDaq
   ```
- Saída:
   ```
   {
    "message": "Token v\u00e1lido",
    "status": "success"
   }
   ```
- Justificativa: As Claims recebidas são válidas.
   ```
   {
  "Role": "Admin",
  "Seed": "7841",
  "Name": "Toninho Araujo"
   }
   ```
   ### Caso 2:
- Entrada:
   ```
   eyJhbGciOiJzI1NiJ9.dfsdfsfryJSr2xrIjoiQWRtaW4iLCJTZrkIjoiNzg0MSIsIk5hbrUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05fsdfsIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg
   ```
- Saída:
   ```
   {
       "status": "error",
       "message": "Formato do token JWT inválido."
   }
   ```
- Justificativa: Formato Inválido.
   ### Caso 3:
- Entrada:
   ```
   eyJhbGciOiJIUzM4NCJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiNzIzNDEiLCJOYW1lIjoiTTRyaWEgT2xpdmlhIn0.g9NRgu85QBXTO20V5E0sUht04kNDzS8px08pz_ppPjdZG_yWqtYHHkh2WeGr0O9t
   ```
- Saída:
   ```
   {
    "status": "error",
    "message": "Formato do token JWT inválido."
   }
   ```
- Justificativa: Token JWT malformado: Claim 'Name' contém caracteres inválidos. Apenas letras são permitidas.
   ```
    { 
     "Role": "External",
     "Seed": "72341",
     "Name": "M4ria Olivia"
   }
   ```
   ### Caso 4:
- Entrada:
   ```
  eyJhbGciOiJIUzM4NCJ9.eyJSb2xlIjoiTWVtYmVyIiwiT3JnIjoiQlIiLCJTZWVkIjoiMTQ2MjciLCJOYW1lIjoiVmFsZGlyIEFyYW5oYSJ9.0Z5e_WTcW0lKzUDyyVGyQk-ki-4viX7caclqxYGYGNfCKRnQtwdAtjmVGKCSeRCZ
   ```
- Saída:
   ```
  {
    "status": "error",
    "message": "Formato do token JWT inválido."
   }
   ```
- Justificativa: Token JWT malformado: Token contém claims inválidas ou extras
   ```
    { 
     "Role": "Member",
     "Org": "BR",
     "Seed": "14627",
     "Name": "Valdir Aranha"
   }
   ```
## 🧪 Testes
O projeto contém testes unitários para:
- `JwtServiceTest`
- `JwtControllerTest`
- `PrimeNumberUtilTest`
- `GlobalExceptionHandlerTest`

Execute os testes com:
```sh
mvn test
```

## 📌 Considerações Finais
Este projeto foi estruturado para ser **seguro, modular e de fácil manutenção**, seguindo princípios modernos de desenvolvimento de software. Está preparado para futuras expansões e personalizações conforme necessário.

---
🔗 **Repositório:** [GitHub - jwtapi](https://github.com/jessicaaraposos/jwtapi)

