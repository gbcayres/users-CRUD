# Java CRUD
## Descrição
Este é um projeto desenvolvido com o fim de praticar diversos conceitos de programação em Java. Este projeto serve como uma primeira experiência na implementação de operações CRUD (Create, Read, Update, Delete) e também no uso de várias técnicas e padrões, o que pode torná-lo aparentemente mais complexo do que uma tarefa CRUD simples. Através deste projeto, busquei testar e integrar conhecimentos adquiridos recentemente, e aplicá-los de maneira coesa.

## Conceitos e Padrões Utilizados
- Arquitetura em Camadas (Layered Architecture): Estruturação do projeto em diferentes camadas (Controller, Service, DAO, etc.) para separar responsabilidades.
- Injeção de Dependência (Dependency Injection): Utilização de interfaces e passagem de dependências via construtores para promover o desacoplamento.
- Princípios SOLID:
  - SRP (Single Responsibility Principle): Cada classe tem uma única responsabilidade.
  - DIP (Dependency Inversion Principle): Dependência de abstrações, não de concretizações.
- Padrão DAO (Data Access Object): Abstração das operações de acesso a dados.
- Persistência com JDBC Puro: Interação direta com o banco de dados usando JDBC sem frameworks adicionais.
- Ferramenta de Build Maven: Gerenciamento de dependências e automação do processo de build.
- Validação de Dados: Implementação de validações robustas para entrada de dados.
- Tratamento de Exceções com padrão Notification: Utilização de um sistema de notificações para gerenciamento de erros e mensagens.
- Hashing de Senhas com BCrypt: Segurança no armazenamento de senhas utilizando hashing.
- Gestão de Configurações via Arquivos de Propriedades: Separação das configurações do ambiente através de arquivos .properties.
- Interface-Based Programming: Uso de interfaces para definir contratos e promover flexibilidade.
