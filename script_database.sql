CREATE DATABASE gym_management;
USE gym_management;

CREATE TABLE Plano (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL,
    valor DECIMAL(10,2) NOT NULL
);

CREATE TABLE Cliente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    plano_id BIGINT,
    FOREIGN KEY (plano_id) REFERENCES Plano(id)
);

CREATE TABLE Instrutor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    especialidade VARCHAR(255)
);

CREATE TABLE Treino (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descricao TEXT NOT NULL,
    cliente_id BIGINT,
    FOREIGN KEY (cliente_id) REFERENCES Cliente(id)
);

CREATE TABLE Exercicio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    repeticoes INT,
    series INT,
    treino_id BIGINT,
    FOREIGN KEY (treino_id) REFERENCES Treino(id)
);

CREATE TABLE Pagamento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    valor DECIMAL(10,2) NOT NULL,
    dataPagamento DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    cliente_id BIGINT,
    FOREIGN KEY (cliente_id) REFERENCES Cliente(id)
);



CREATE TABLE Administrador (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL
);

ALTER TABLE Cliente ADD COLUMN administrador_id BIGINT;
ALTER TABLE Cliente ADD FOREIGN KEY (administrador_id) REFERENCES Administrador(id);

ALTER TABLE Instrutor ADD COLUMN administrador_id BIGINT;
ALTER TABLE Instrutor ADD FOREIGN KEY (administrador_id) REFERENCES Administrador(id);