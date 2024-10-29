# Users CRUD
## Descrição do Projeto
**Users CRUD** é uma aplicação de gerenciamento de usuários construída em Java, com operações de criação, atualização, remoção e recuperação de usuários. O projeto utiliza servlets para expor uma API RESTful, implementando boas práticas como validação de dados, criptografia de senhas e testes unitários.

## Tecnologias Utilizadas
- **Java**: Linguagem principal do projeto.
- **Maven**: Gerenciador de dependências.
- **PostgreSQL**: Banco de dados relacional.
- **JDBC**: Comunicação com o banco de dados.
- **JUnit**: Biblioteca para testes unitários.
- **Mockito**: Biblioteca para criação de Mocks.
- **Servlet API**: Exposição de endpoints HTTP para manipulação de dados de usuários.
- **Gson**: Biblioteca para serialização e desserialização de objetos JSON.
- **BCrypt**: Biblioteca para criptografia de senhas.
---

## Estrutura do Projeto
O projeto segue uma arquitetura em camadas, onde cada camada é responsável por um conjunto específico de responsabilidades:

### 1. Camada de Modelos
- **`User`**: Classe de domínio representando um usuário.

### 2. Camada de Persistência
- **`UserRepository`**: Classe que contém a lógica de persistência e métodos para manipular dados no banco de dados.

### 3. Camada de Serviços
- **`UserService`**: Camada que contém a lógica de negócios, fazendo o intermédio entre a camada de Persistência e a de Controladores.

### 4. Camada de Controladores
- **`UsersServlet` e `UserServlet`**: Lidam com as requisições HTTP e interagem com a camada de serviços.
---

## Endpoints da API

| Método | Endpoint        | Descrição                       | Códigos de Status                                      | Retorno                                           |
|--------|------------------|---------------------------------|--------------------------------------------------------|---------------------------------------------------|
| POST   | `/users`        | Cria um novo usuário.          | 201 Created / <br/> 400 Bad Request                         | Cabeçalho de localização / <br/> Mensagem de erro |
| GET    | `/users`        | Recupera todos os usuários.    | 200 OK                                                 | Lista de usuários salvos                          |
| GET    | `/users/{id}`   | Recupera um usuário pelo ID.   | 200 OK / <br/> 404 Not Found                           | Detalhes do usuário / <br/> Mensagem de erro      |
| PUT    | `/users/{id}`   | Atualiza um usuário pelo ID.   | 204 No Content / <br/>  404 Not Found, 400 Bad Request | Body vazio / <br/> Mensagem de erro               |
| DELETE | `/users/{id}`   | Remove um usuário pelo ID.     | 204 No Content / <br/> 404 Not Found                   | Body vazio / <br/> Mensagem de erro               |

### Detalhes dos Códigos de Status e Retornos

- **201 Created**: Retornado após a criação bem-sucedida de um novo usuário.
- **200 OK**: Indica que a operação foi bem-sucedida e o corpo da resposta contém os dados solicitados.
- **204 No Content**: Retornado após operação bem-sucedida, sem conteúdo no corpo da resposta.
- **400 Bad Request**: Indica que a requisição estava malformada devido a erros de validação.
- **404 Not Found**: Indica que o recurso solicitado (usuário) não foi encontrado.

### Exemplos de Uso

POST 
```bash
curl -X POST http://localhost:8080/UsersCRUD/v1/users \
-H "Content-Type: application/json" \
-d '{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "123456@John"
}'
```

GET _(by ID)_
```bash
curl -X GET localhost:8080/usersCRUD/v1/users/a16ce042-a655-435a-9f10-16ba20d620ff
-H "Content-Type: application/json"
```

```json
{
    "id": "a16ce042-a655-435a-9f10-16ba20d620ff",
    "name": "John Doe",
    "email": "john.doe@example.com",
    "password": "$2a$10$nOBRFoV/xx/kNy6Z6xidyOBxaRzFQoIM6isCnFcRxXsZCzb7dikG.",
    "createdAt": "2024-10-29T15:22:52.145111Z",
    "updatedAt": "2024-10-29T15:22:52.145111Z"
}
```
---

## Rodando o projeto
Para rodar o projeto, precisamos realizar algumas etapas:

### Configuração do Banco de Dados

* Criar e configurar um novo banco de dados e fornecer as credenciais no arquivo ```db.properties```
```properties
db.url=jdbc:postgresql://localhost:5432/users
db.username=seu_usuario
db.password=sua_senha
```

* Criar uma tabela ```users``` com a seguinte estrutura:
```SQL
CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    createdAt TIMESTAMP NOT NULL,
    updatedAt TIMESTAMP NOT NULL
);
```
### Configuração do Tomcat
* **Instale o Apache Tomcat**
* **Configure o ambiente Java**: Certifique-se de que o Java está instalado e que a variável de ambiente ```JAVA_HOME``` aponta para o diretório de instalação do Java.

### Deploy Local no Tomcat
* **Gerar arquivo ```.war```**: No diretório do projeto, execute o comando ```mvn clean package``` para gerar o arquivo ```.war```.
* **Deploy do ```.war```**: Copie o arquivo gerado (target/UsersCRUDAPI.war) para o diretório **webapps** do Tomcat.
* **Inicie o Tomcat**: Navegue até o diretório **bin** do Tomcat e execute ```startup.sh``` (Linux/macOS) ou ```startup.bat``` (Windows).

---
