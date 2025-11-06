
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
    descricao VARCHAR(500),
    endereco_id INTEGER,
    senha VARCHAR(200) NOT NULL,
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

