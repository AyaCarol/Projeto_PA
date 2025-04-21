## Projeto final 1BIM - Programação Avançada

Esse é o nosso projeto final da matéria de Programação Avançada do curso de ADSIS - 5SEM! Nossa equipe é:
- Ana Carolina Gomes da Silva 23211454-2
- Emily Grzgorczyki 23034936-2

## Estrutura do trabalho

Entendemos a classe empresa como services por conta do seu controle das outras classes. Dessa forma, interpretamos a estrutura do trabalho como:
- Models: Pessoa, Funcionario, ContaBancaria, ContaCorrente, ContaPoupança, Tributavel, Produto
- Services: Empresa


### Banco SQL - JDBC
O banco criado foi em MySQL e escolhemos o JDBC ao invés do JPA apenas pela familiaridade com a API.
Nosso banco segue algumas relações extra entre Empresa - Produtos, Empresa - Funcionarios, além da junção de ContaBancaria com ContaCorrente em uma tabela geral contas_bancarias. O arquivo do script SQL se encontra na pasta src, com a criação total do schema, criação das tabelas e criação de indexes para praticidade.

### Conexão
Para lidar com a conexão com o banco, definimos as duas pastas:
- Factory: Pasta para criar a classe de conexão e inserir as credenciais do banco
- DAO: Data Access Object, classes que nos permite a manipulação de query's SQL. É através das classes DAO que realizamos os métodos CRUD (create, read, update, delete) para inserção, busca, atualização e exclusão de itens ao banco.

### Classe Main e Testes de CRUD
Para realização dos testes dos métodos CRUD, foram criados métodos na classe main com objetivo de acelerar o processo de verificação. Basta executar a classe main, e comentar a parte de Delete para consultar as modificações no banco.
