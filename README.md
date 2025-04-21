## Projeto final 1BIM - Programação Avançada

Esse é o nosso projeto final da matéria de Programação Avançada do curso de ADSIS - 5SEM! Nossa equipe é:
- Ana Carolina Gomes da Silva 23211454-2
- Emily Grzgorczyki 23034936-2

## Estrutura do trabalho

Interpretamos a estrutura do trabalho como:
- Models: Pessoa, Funcionario, ContaBancaria, ContaCorrente, ContaPoupança, Tributavel, Produto
- Services: Empresa
Entendemos a classe empresa como services por conta do seu controle das outras classes.

### Conexão
Para lidar com a conexão com o banco, definimos as duas pastas:
- Factory: para criar a classe de conexão e inserir as credenciais do banco
- DAO: Data Access Object, classes que nos permite a manipulação de query's sql
