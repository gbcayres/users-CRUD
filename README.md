# Users CRUD
## Descrição do Projeto
**Users CRUD** é uma aplicação de gerenciamento de usuários construída em Java, com operações de criação, atualização, remoção e recuperação de usuários. O projeto utiliza servlets para expor uma API RESTful, implementando boas práticas como validação de dados e criptografia de senhas.

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
- **`UserDAO`**: Classe que contém a lógica de persistência e métodos para manipular dados no banco de dados.

### 3. Camada de Serviços
- **`UserService`**: Camada que contém a lógica de negócios, fazendo o intermédio entre a camada de DAO e a de Controllers.

### 4. Camada de Controladores
- **`UsersServlet` e `UserServlet`**: Lidam com as requisições HTTP e interagem com a camada de serviços.
---

## Endpoints da API
| Método | Endpoint        | Descrição                       |
|--------|------------------|---------------------------------|
| POST   | `/users`        | Cria um novo usuário.          |
| GET    | `/users`        | Recupera todos os usuários.    |
| GET    | `/users/{id}`   | Recupera um usuário pelo ID.   |
| PUT    | `/users/{id}`   | Atualiza um usuário pelo ID.   |
| DELETE | `/users/{id}`   | Remove um usuário pelo ID.     |

### Exemplo de Uso

_Requisição_
```bash
curl -X POST http://localhost:8080/UsersCRUD/users \
-H "Content-Type: application/json" \
-d '{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "123456"
}'

```

_Resposta_
```json{
{
    "data": {
        "createdAt": "27/10/2024 17:31",
        "email": "john.doe@example.com",
        "id": 6,
        "name": "John Doe"
    },
    "message": "User created successfully",
    "status": 201
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
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    createdAt TIMESTAMP WITH TIME ZONE DEFAULT now()
);
```
### Configuração do Tomcat
* Instale o Apache Tomcat
* Configure o ambiente Java: Certifique-se de que o Java está instalado e que a variável de ambiente ```JAVA_HOME``` aponta para o diretório de instalação do Java.

### Deploy Local no Tomcat
* Gerar arquivo ```.war```: No diretório do projeto, execute o comando ```mvn clean package``` para gerar o arquivo ```.war```.
* Deploy do ```.war```: Copie o arquivo gerado (target/UsersCRUDAPI.war) para o diretório **webapps** do Tomcat.
* Inicie o Tomcat: Navegue até o diretório **bin** do Tomcat e execute ```startup.sh``` (Linux/macOS) ou ```startup.bat``` (Windows).

---