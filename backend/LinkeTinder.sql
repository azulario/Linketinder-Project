-- Tabela de Candidatos
CREATE TABLE candidatos (
    idCandidatos SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    sobrenome VARCHAR(100) NOT NULL,
    data_de_nascimento DATE NOT NULL,
    email VARCHAR(180) UNIQUE NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    pais VARCHAR(100) NOT NULL,
    cep VARCHAR(10),
    descricao TEXT,
    senha VARCHAR(200) NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Empresas
CREATE TABLE empresas (
    idEmpresas SERIAL PRIMARY KEY,
    nome_empresa VARCHAR(180) NOT NULL,
    cnpj VARCHAR(18) UNIQUE NOT NULL,
    email VARCHAR(180) UNIQUE NOT NULL,
    descricao VARCHAR(45),
    pais VARCHAR(100) NOT NULL,
    cep VARCHAR(45),
    senha VARCHAR(10) NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Competências
CREATE TABLE competencias (
    idCompetencias SERIAL PRIMARY KEY,
    nome_competencia VARCHAR(100) UNIQUE NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Vagas
CREATE TABLE vagas (
    idVagas SERIAL PRIMARY KEY,
    empresa_id INTEGER NOT NULL,
    nome_vaga VARCHAR(200) NOT NULL,
    descricao TEXT,
    estado VARCHAR(50),
    cidade VARCHAR(45),
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (empresa_id) REFERENCES empresas(idEmpresas) ON DELETE CASCADE
);

-- Tabela de Relacionamento: Candidato_Competencias (N:N)
CREATE TABLE candidato_competencias (
    idCandidato_Competencias SERIAL PRIMARY KEY,
    candidato_id INTEGER NOT NULL,
    competencia_id INTEGER NOT NULL,
    FOREIGN KEY (candidato_id) REFERENCES candidatos(idCandidatos) ON DELETE CASCADE,
    FOREIGN KEY (competencia_id) REFERENCES competencias(idCompetencias) ON DELETE CASCADE,
    UNIQUE(candidato_id, competencia_id)
);

-- Tabela de Relacionamento: Competencias_Vagas (N:N)
CREATE TABLE competencias_vagas (
    idCompetencias_Vagas SERIAL PRIMARY KEY,
    competencia_id INTEGER NOT NULL,
    vaga_id INTEGER NOT NULL,
    FOREIGN KEY (competencia_id) REFERENCES competencias(idCompetencias) ON DELETE CASCADE,
    FOREIGN KEY (vaga_id) REFERENCES vagas(idVagas) ON DELETE CASCADE,
    UNIQUE(competencia_id, vaga_id)
);

-- Tabela de Curtidas e Matches
CREATE TABLE curtidas (
    idCurtidas SERIAL PRIMARY KEY,
    candidato_id INTEGER NOT NULL,
    vaga_id INTEGER NOT NULL,
    empresa_id INTEGER NOT NULL,
    curtida_candidato BOOLEAN DEFAULT FALSE,
    curtida_empresa BOOLEAN DEFAULT FALSE,
    match BOOLEAN DEFAULT FALSE,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (candidato_id) REFERENCES candidatos(idCandidatos) ON DELETE CASCADE,
    FOREIGN KEY (vaga_id) REFERENCES vagas(idVagas) ON DELETE CASCADE,
    FOREIGN KEY (empresa_id) REFERENCES empresas(idEmpresas) ON DELETE CASCADE,
    UNIQUE(candidato_id, vaga_id)
);


-- CRIAR ÍNDICES (PERFORMANCE)
-- (tipo um marca pagina)


CREATE INDEX idx_candidatos_email ON candidatos(email);
CREATE INDEX idx_candidatos_cpf ON candidatos(cpf);
CREATE INDEX idx_empresas_email ON empresas(email);
CREATE INDEX idx_empresas_cnpj ON empresas(cnpj);
CREATE INDEX idx_vagas_empresa ON vagas(empresa_id);
CREATE INDEX idx_curtidas_match ON curtidas(match);

-- INSERIR DADOS FICTÍCIOS

-- Inserir Competências
INSERT INTO competencias (nome_competencia) VALUES
('Java'),
('Python'),
('Groovy'),
('Angular'),
('React'),
('Spring Framework'),
('PostgreSQL'),
('Docker'),
('Git'),
('JavaScript');

-- Inserir 5 Candidatos
INSERT INTO candidatos (nome, sobrenome, data_de_nascimento, email, cpf, pais, cep, descricao, senha) VALUES
('Sandubinha', 'Silva', '1995-03-15', 'sandubinha@email.com', '123.456.789-00', 'Brasil', '86010-001', 'Desenvolvedor full-stack apaixonado por tecnologia e inovação.', 'senha123'),
('Maria', 'Oliveira', '1998-07-22', 'maria.oliveira@email.com', '234.567.890-11', 'Brasil', '01310-100', 'Engenheira de software com experiência em backend.', 'senha456'),
('João', 'Santos', '1992-11-10', 'joao.santos@email.com', '345.678.901-22', 'Brasil', '30130-100', 'Especialista em banco de dados e otimização.', 'senha789'),
('Ana', 'Costa', '1996-05-30', 'ana.costa@email.com', '456.789.012-33', 'Brasil', '40020-000', 'Designer UX/UI e desenvolvedora frontend.', 'senhaABC'),
('Carlos', 'Pereira', '1994-09-18', 'carlos.pereira@email.com', '567.890.123-44', 'Brasil', '50010-000', 'DevOps engineer com foco em automação.', 'senhaDEF');

-- Inserir 5 Empresas
INSERT INTO empresas (nome_empresa, cnpj, email, descricao, pais, cep, senha) VALUES
('Pastelsoft', '12.345.678/0001-90', 'contato@pastelsoft.com', 'Desenvolvimento de ERPs para restaurantes', 'Brasil', '86020-000', 'pastel123'),
('TechBurger Ltda', '23.456.789/0001-01', 'rh@techburger.com', 'Soluções em tecnologia para e-commerce', 'Brasil', '01310-200', 'burger456'),
('CodeCoxinha SA', '34.567.890/0001-12', 'vagas@codecoxinha.com', 'Consultoria em desenvolvimento ágil', 'Brasil', '30140-000', 'coxinha789'),
('DataEmpada Inc', '45.678.901/0001-23', 'jobs@dataempada.com', 'Big Data e Analytics', 'Brasil', '40030-000', 'empada321'),
('CloudKibe Tech', '56.789.012/0001-34', 'careers@cloudkibe.com', 'Cloud computing e infraestrutura', 'Brasil', '50020-000', 'kibe654');

-- Associar Competências aos Candidatos
INSERT INTO candidato_competencias (candidato_id, competencia_id) VALUES
-- Sandubinha
(1, 1), (1, 3), (1, 4), (1, 7),
-- Maria
(2, 1), (2, 6), (2, 7), (2, 8),
-- João
(3, 7), (3, 8), (3, 2), (3, 9),
-- Ana
(4, 4), (4, 5), (4, 10), (4, 9),
-- Carlos
(5, 8), (5, 9), (5, 2), (5, 7);

-- Inserir Vagas
INSERT INTO vagas (empresa_id, nome_vaga, descricao, estado, cidade) VALUES
(1, 'Desenvolvedor Spring', 'Desenvolvedor backend com Spring Framework', 'Paraná', 'Londrina'),
(1, 'Desenvolvedor Angular', 'Desenvolvedor frontend especialista em Angular', 'Paraná', 'Londrina'),
(2, 'Full Stack Java', 'Desenvolvedor full-stack para e-commerce', 'São Paulo', 'São Paulo'),
(3, 'Consultor Ágil', 'Consultor para implementação de metodologias ágeis', 'Minas Gerais', 'Belo Horizonte'),
(4, 'Engenheiro de Dados', 'Especialista em Big Data e Python', 'Bahia', 'Salvador'),
(5, 'DevOps Engineer', 'Profissional para infraestrutura em cloud', 'Pernambuco', 'Recife');

-- Associar Competências às Vagas
INSERT INTO competencias_vagas (competencia_id, vaga_id) VALUES
-- Vaga 1: Spring
(1, 1), (6, 1), (7, 1),
-- Vaga 2: Angular
(4, 2), (10, 2), (9, 2),
-- Vaga 3: Full Stack
(1, 3), (5, 3), (7, 3),
-- Vaga 4: Consultor
(9, 4), (1, 4),
-- Vaga 5: Dados
(2, 5), (7, 5), (8, 5),
-- Vaga 6: DevOps
(8, 6), (9, 6), (2, 6);

-- Inserir Curtidas (alguns exemplos)
INSERT INTO curtidas (candidato_id, vaga_id, empresa_id, curtida_candidato, curtida_empresa, match) VALUES
-- Sandubinha curtiu vaga Angular da Pastelsoft
(1, 2, 1, TRUE, FALSE, FALSE),
-- Maria curtiu vaga Spring da Pastelsoft E empresa curtiu de volta = MATCH!
(2, 1, 1, TRUE, TRUE, TRUE),
-- João curtiu vaga de Dados
(3, 5, 4, TRUE, FALSE, FALSE),
-- Ana curtiu vaga Angular e empresa curtiu = MATCH!
(4, 2, 1, TRUE, TRUE, TRUE),
-- Carlos curtiu vaga DevOps
(5, 6, 5, TRUE, FALSE, FALSE);

