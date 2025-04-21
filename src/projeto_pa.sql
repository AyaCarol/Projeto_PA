select * from contas_bancarias;
select * from funcionarios;
select * from produtos;

CREATE DATABASE IF NOT EXISTS projeto_pa;
USE projeto_pa;

-- Tabela de contas bancárias 
CREATE TABLE contas_bancarias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titular VARCHAR(255) NOT NULL,
    agencia VARCHAR(255) NOT NULL,
    conta VARCHAR(255) NOT NULL UNIQUE,
    saldo DOUBLE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    tarifa DOUBLE NOT NULL,
    rendimento_mensal DOUBLE NOT NULL,
    tipo VARCHAR(50) NOT NULL
);

-- Tabela de empresas
CREATE TABLE empresas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cnpj VARCHAR(255) NOT NULL UNIQUE
);

-- Tabela de emails das empresas
CREATE TABLE empresa_emails (
    id INT AUTO_INCREMENT PRIMARY KEY,
    empresa_id INT NOT NULL,
    email VARCHAR(255) NOT NULL,
    FOREIGN KEY (empresa_id) REFERENCES empresas(id) ON DELETE CASCADE
);

-- Tabela de funcionários
CREATE TABLE funcionarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cpf VARCHAR(255) NOT NULL UNIQUE,
    nome VARCHAR(255) NOT NULL,
    idade INT NOT NULL,
    endereco VARCHAR(255) NOT NULL,
    salario DOUBLE NOT NULL,
    cargo VARCHAR(255) NOT NULL,
    id_conta INT NOT NULL,
    FOREIGN KEY (id_conta) REFERENCES contas_bancarias(id) ON DELETE CASCADE
);

-- Tabela de produtos
CREATE TABLE produtos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(255) NOT NULL UNIQUE,
    valor DOUBLE NOT NULL,
    nome VARCHAR(255) NOT NULL,
    quantidade INT NOT NULL
);

-- Tabela de relacionamento empresa-funcionários
CREATE TABLE empresa_funcionarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    empresa_id INT NOT NULL,
    funcionario_id INT NOT NULL,
    FOREIGN KEY (empresa_id) REFERENCES empresas(id) ON DELETE CASCADE,
    FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id) ON DELETE CASCADE
);

-- Tabela de relacionamento empresa-produtos
CREATE TABLE empresa_produtos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    empresa_id INT NOT NULL,
    produto_id INT NOT NULL,
    FOREIGN KEY (empresa_id) REFERENCES empresas(id) ON DELETE CASCADE,
    FOREIGN KEY (produto_id) REFERENCES produtos(id) ON DELETE CASCADE
);

-- Índices para melhor performance
CREATE INDEX idx_contas_tipo ON contas_bancarias(tipo);
CREATE INDEX idx_empresas_cnpj ON empresas(cnpj);
CREATE INDEX idx_funcionarios_cpf ON funcionarios(cpf);
CREATE INDEX idx_produtos_codigo ON produtos(codigo);