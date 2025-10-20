
-- Tabela de Endereços (CENTRALIZADA)
CREATE TABLE enderecos (
    idEnderecos SERIAL PRIMARY KEY,
    cep VARCHAR(10),
    estado VARCHAR(50),
    cidade VARCHAR(100),
    pais VARCHAR(100) NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Candidatos
CREATE TABLE candidatos (
    idCandidatos SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    sobrenome VARCHAR(100) NOT NULL,
    data_de_nascimento DATE NOT NULL,
    email VARCHAR(180) UNIQUE NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    endereco_id INTEGER,
    descricao TEXT,
    senha VARCHAR(200) NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (endereco_id) REFERENCES enderecos(idEnderecos) ON DELETE SET NULL
);

-- Tabela de Empresas
CREATE TABLE empresas (
    idEmpresas SERIAL PRIMARY KEY,
    nome_empresa VARCHAR(180) NOT NULL,
    cnpj VARCHAR(18) UNIQUE NOT NULL,
    email VARCHAR(180) UNIQUE NOT NULL,
    descricao VARCHAR(45),
    endereco_id INTEGER,
    senha VARCHAR(10) NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (endereco_id) REFERENCES enderecos(idEnderecos) ON DELETE SET NULL
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
    endereco_id INTEGER,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (empresa_id) REFERENCES empresas(idEmpresas) ON DELETE CASCADE,
    FOREIGN KEY (endereco_id) REFERENCES enderecos(idEnderecos) ON DELETE SET NULL
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

-- ========================================
-- ÍNDICES PARA PERFORMANCE
-- ========================================

CREATE INDEX idx_candidatos_email ON candidatos(email);
CREATE INDEX idx_candidatos_cpf ON candidatos(cpf);
CREATE INDEX idx_candidatos_endereco ON candidatos(endereco_id);

CREATE INDEX idx_empresas_email ON empresas(email);
CREATE INDEX idx_empresas_cnpj ON empresas(cnpj);
CREATE INDEX idx_empresas_endereco ON empresas(endereco_id);

CREATE INDEX idx_vagas_empresa ON vagas(empresa_id);
CREATE INDEX idx_vagas_endereco ON vagas(endereco_id);

CREATE INDEX idx_curtidas_match ON curtidas(match);

CREATE INDEX idx_enderecos_cep ON enderecos(cep);
CREATE INDEX idx_enderecos_estado_cidade ON enderecos(estado, cidade);

-- ========================================
-- INSERIR DADOS FICTÍCIOS
-- ========================================

-- Inserir Endereços
INSERT INTO enderecos (cep, estado, cidade, pais) VALUES
    ('86010-001', 'Paraná', 'Londrina', 'Brasil'),
    ('01310-100', 'São Paulo', 'São Paulo', 'Brasil'),
    ('30130-100', 'Minas Gerais', 'Belo Horizonte', 'Brasil'),
    ('40020-000', 'Bahia', 'Salvador', 'Brasil'),
    ('50010-000', 'Pernambuco', 'Recife', 'Brasil');

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
INSERT INTO candidatos (nome, sobrenome, data_de_nascimento, email, cpf, endereco_id, descricao, senha) VALUES
    ('Sandubinha', 'Silva', '1995-03-15', 'sandubinha@email.com', '123.456.789-00', 1, 'Desenvolvedor full-stack apaixonado por tecnologia e inovação.', 'senha123'),
    ('Maria', 'Oliveira', '1998-07-22', 'maria.oliveira@email.com', '234.567.890-11', 2, 'Engenheira de software com experiência em backend.', 'senha456'),
    ('João', 'Santos', '1992-11-10', 'joao.santos@email.com', '345.678.901-22', 3, 'Especialista em banco de dados e otimização.', 'senha789'),
    ('Ana', 'Costa', '1996-05-30', 'ana.costa@email.com', '456.789.012-33', 4, 'Designer UX/UI e desenvolvedora frontend.', 'senhaABC'),
    ('Carlos', 'Pereira', '1994-09-18', 'carlos.pereira@email.com', '567.890.123-44', 5, 'DevOps engineer com foco em automação.', 'senhaDEF');

-- Inserir 5 Empresas
INSERT INTO empresas (nome_empresa, cnpj, email, descricao, endereco_id, senha) VALUES
    ('Pastelsoft', '12.345.678/0001-90', 'contato@pastelsoft.com', 'Desenvolvimento de ERPs para restaurantes', 1, 'pastel123'),
    ('TechBurger Ltda', '23.456.789/0001-01', 'rh@techburger.com', 'Soluções em tecnologia para e-commerce', 2, 'burger456'),
    ('CodeCoxinha SA', '34.567.890/0001-12', 'vagas@codecoxinha.com', 'Consultoria em desenvolvimento ágil', 3, 'coxinha789'),
    ('DataEmpada Inc', '45.678.901/0001-23', 'jobs@dataempada.com', 'Big Data e Analytics', 4, 'empada321'),
    ('CloudKibe Tech', '56.789.012/0001-34', 'careers@cloudkibe.com', 'Cloud computing e infraestrutura', 5, 'kibe654');

-- Associar Competências aos Candidatos
INSERT INTO candidato_competencias (candidato_id, competencia_id) VALUES
    (1, 1), (1, 3), (1, 4), (1, 7),
    (2, 1), (2, 6), (2, 7), (2, 8),
    (3, 7), (3, 8), (3, 2), (3, 9),
    (4, 4), (4, 5), (4, 10), (4, 9),
    (5, 8), (5, 9), (5, 2), (5, 7);

-- Inserir Vagas
INSERT INTO vagas (empresa_id, nome_vaga, descricao, endereco_id) VALUES
    (1, 'Desenvolvedor Spring', 'Desenvolvedor backend com Spring Framework', 1),
    (1, 'Desenvolvedor Angular', 'Desenvolvedor frontend especialista em Angular', 1),
    (2, 'Full Stack Java', 'Desenvolvedor full-stack para e-commerce', 2),
    (3, 'Consultor Ágil', 'Consultor para implementação de metodologias ágeis', 3),
    (4, 'Engenheiro de Dados', 'Especialista em Big Data e Python', 4),
    (5, 'DevOps Engineer', 'Profissional para infraestrutura em cloud', 5);

-- Associar Competências às Vagas
INSERT INTO competencias_vagas (competencia_id, vaga_id) VALUES
    (1, 1), (6, 1), (7, 1),
    (4, 2), (10, 2), (9, 2),
    (1, 3), (5, 3), (7, 3),
    (9, 4), (1, 4),
    (2, 5), (7, 5), (8, 5),
    (8, 6), (9, 6), (2, 6);

-- Inserir Curtidas (alguns exemplos)
INSERT INTO curtidas (candidato_id, vaga_id, empresa_id, curtida_candidato, curtida_empresa, match) VALUES
    (1, 2, 1, TRUE, FALSE, FALSE),
    (2, 1, 1, TRUE, TRUE, TRUE),
    (3, 5, 4, TRUE, FALSE, FALSE),
    (4, 2, 1, TRUE, TRUE, TRUE),
    (5, 6, 5, TRUE, FALSE, FALSE);
