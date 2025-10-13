# LinkeTinder

## Autor
Nathalia Veiga 

## Descrição
Projeto MVP de backend simplificado para sistema de contratação inspirado no LinkedIn e Tinder, desenvolvido em **Groovy com integração JDBC ao PostgreSQL**. 

Sistema permite cadastrar e listar candidatos e empresas, gerenciar vagas e implementar sistema de curtidas entre candidatos e empresas. Estrutura orientada a objetos, utilizando interface, herança e padrão DAO para persistência de dados.

> **Este projeto está em desenvolvimento para fins de aprendizado e aplicação dos conhecimentos adquiridos no programa Acelera ZG.**
> 
> **Versão atual:** Integração JDBC completa com PostgreSQL - 3 DAOs implementados e 30 testes unitários passando! ✅

## 🚀 Tecnologias Utilizadas

### Backend
- **Groovy 4.0.15** - Linguagem de programação
- **Gradle 8.14** - Build tool
- **PostgreSQL 14+** - Banco de dados relacional
- **JDBC puro** - Integração com banco de dados (sem ORM)
- **Spock 2.3** - Framework de testes unitários

### Frontend
- **TypeScript** - Linguagem tipada
- **Vite** - Build tool moderno
- **Tailwind CSS** - Framework de estilos
- **Chart.js** - Visualização de dados

## 📁 Estrutura do Projeto (Backend)

```
src/main/groovy/com/linketinder/
├── Main.groovy                    # Ponto de entrada da aplicação
├── model/                         # Entidades do domínio
│   ├── Candidato.groovy          # Modelo de candidato
│   ├── Empresa.groovy            # Modelo de empresa
│   ├── Vaga.groovy               # Modelo de vaga
│   └── Usuarios.groovy           # Interface comum
├── view/                          # Interface com usuário
│   └── Menu.groovy               # Menu interativo
├── database/                      # Camada de dados
│   ├── Database.groovy           # Gerenciador em memória (legacy)
│   └── DatabaseConnection.groovy # ✅ Conexão JDBC com PostgreSQL
└── dao/                          # ✅ Data Access Objects (IMPLEMENTADOS)
    ├── CandidatoDAO.groovy       # ✅ CRUD de candidatos (8/8 testes)
    ├── EmpresaDAO.groovy         # ✅ CRUD de empresas (8/8 testes)
    └── VagaDAO.groovy            # ✅ CRUD de vagas (9/9 testes)

src/test/groovy/com/linketinder/
├── dao/                          # ✅ Testes dos DAOs
│   ├── DatabaseConnectionSpec.groovy  # ✅ 5/5 testes passando
│   ├── CandidatoDAOSpec.groovy       # ✅ 8/8 testes passando
│   ├── EmpresaDAOSpec.groovy         # ✅ 8/8 testes passando
│   └── VagaDAOSpec.groovy            # ✅ 9/9 testes passando
└── view/
    ├── MenuCadastroCandidatoSpec.groovy
    ├── MenuCadastroEmpresaSpec.groovy
    ├── MenuCurtidasCandidatoSpec.groovy
    └── MenuCurtidasEmpresaSpec.groovy

doc/                              # Documentação técnica
├── GUIA_INTEGRACAO_JDBC.txt     # Guia completo de integração
├── PROGRESSO_DESAFIO_K1-T9.txt  # Status do desafio
└── ... (outros guias)
```

## 🎯 Funcionalidades (Backend)

### ✅ Implementadas
- **Menu interativo** no terminal
- **Cadastro de candidatos** (com data de nascimento e competências)
- **Cadastro de empresas** (com informações completas)
- **Cadastro de vagas** (com competências e vínculo à empresa)
- **Listagem** de candidatos, empresas e vagas
- **Sistema de curtidas**:
  - Candidatos podem curtir vagas
  - Empresas podem curtir candidatos
- **✅ Persistência completa no PostgreSQL via JDBC**
- **✅ 3 DAOs completos** (CandidatoDAO, EmpresaDAO, VagaDAO)
- **✅ 30 testes unitários** com Spock Framework
- **✅ Relacionamentos N:N** (candidato↔competências, vaga↔competências)
- **✅ Relacionamento 1:N** (empresa→vagas)

### 🚧 Em Desenvolvimento
- Integração dos DAOs com Menu.groovy
- Sistema completo de matches
- API REST (futuro)

## 💾 Integração com PostgreSQL

### 🎯 Padrão DAO Implementado

O projeto utiliza o padrão **DAO (Data Access Object)** para separar a lógica de acesso a dados. Cada DAO é responsável pelas operações CRUD de uma entidade:

#### ✅ **CandidatoDAO** (8/8 testes passando)
- `inserir(Candidato)` - Salva candidato no banco
- `listar()` - Retorna todos os candidatos
- `buscarPorId(Integer)` - Busca candidato específico
- `atualizar(Candidato)` - Atualiza dados do candidato
- `deletar(Integer)` - Remove candidato
- **Gerencia relação N:N** com competências via tabela `candidato_competencias`

#### ✅ **EmpresaDAO** (8/8 testes passando)
- `inserir(Empresa)` - Salva empresa no banco
- `listar()` - Retorna todas as empresas
- `buscarPorId(Integer)` - Busca empresa específica
- `atualizar(Empresa)` - Atualiza dados da empresa
- `deletar(Integer)` - Remove empresa

#### ✅ **VagaDAO** (9/9 testes passando)
- `inserir(Vaga)` - Salva vaga no banco
- `listar()` - Retorna todas as vagas
- `listarPorEmpresa(Integer)` - Vagas de uma empresa específica
- `buscarPorId(Integer)` - Busca vaga específica
- `atualizar(Vaga)` - Atualiza dados da vaga
- `deletar(Integer)` - Remove vaga
- **Gerencia relação 1:N** com empresas (FK empresa_id)
- **Gerencia relação N:N** com competências via tabela `competencias_vagas`

### 📊 Estatísticas do Código

```
📈 Linhas de código: ~1800+ linhas
🧪 Testes unitários: 30/30 passando (100% de cobertura dos DAOs)
📁 Arquivos criados: 18+ arquivos
🗄️ Tabelas no banco: 7 tabelas
🔗 Relacionamentos: 3 N:N + 1 1:N
```

### Diagrama ER
![Diagrama do Banco](diagram-er.png)

**Software utilizado:** dbdiagram.io

### Estrutura do Banco
- **PostgreSQL** como SGBD
- **7 tabelas principais**:
  - `candidatos` - Dados dos candidatos (13 campos)
  - `empresas` - Dados das empresas (10 campos)
  - `vagas` - Vagas disponíveis (7 campos)
  - `competencias` - Lista de competências (3 campos)
  - `candidato_competencias` - N:N entre candidatos e competências
  - `competencias_vagas` - N:N entre vagas e competências
  - `curtidas` - Registro de curtidas entre candidatos e empresas

### Relacionamentos Implementados

#### **N:N - Candidato ↔ Competências**
- Tabela intermediária: `candidato_competencias`
- Gerenciado automaticamente pelo `CandidatoDAO`
- Método `buscarOuCriarCompetencia()` evita duplicação

#### **N:N - Vaga ↔ Competências**
- Tabela intermediária: `competencias_vagas`
- Gerenciado automaticamente pelo `VagaDAO`
- Suporta múltiplas competências por vaga

#### **1:N - Empresa → Vagas**
- Foreign Key `empresa_id` na tabela `vagas`
- Método `listarPorEmpresa(Integer)` no VagaDAO
- CASCADE DELETE configurado

### Scripts SQL
- `LinkeTinder.sql` - Script completo de criação do banco e dados de exemplo
  - ✅ Criação de todas as tabelas
  - ✅ 5 candidatos fictícios
  - ✅ 5 empresas fictícias
  - ✅ 10 competências
  - ✅ 6 vagas com competências

## 🏃 Como Executar

### Pré-requisitos
1. **Java 21+** instalado
2. **Groovy 4.0+** (ou usar via Gradle)
3. **PostgreSQL 14+** instalado e rodando
4. **Gradle 8.14+** (ou usar o wrapper `./gradlew`)
5. **Git** para clonar o repositório

### Passo 1: Clonar o Repositório

```bash
git clone <url-do-repositorio>
cd LinkeTinder
```

### Passo 2: Configurar o Banco de Dados

#### 2.1. Criar o banco no PostgreSQL:
```sql
-- No psql ou pgAdmin:
CREATE DATABASE linketinder;
```

#### 2.2. Executar o script SQL:
```bash
# Via terminal:
psql -U postgres -d linketinder -f LinkeTinder.sql

# Ou importe o arquivo via pgAdmin 4
```

#### 2.3. Verificar as tabelas criadas:
```sql
-- Conectar ao banco:
\c linketinder

-- Listar tabelas:
\dt

-- Deve mostrar:
-- candidatos
-- empresas
-- vagas
-- competencias
-- candidato_competencias
-- competencias_vagas
-- curtidas
```

### Passo 3: Configurar a Senha do Banco

Edite o arquivo `src/main/groovy/com/linketinder/database/DatabaseConnection.groovy`:

```groovy
private static final String PASSWORD = "sua_senha_aqui"  // ⚠️ Altere aqui
```

### Passo 4: Executar a Aplicação

#### Via Gradle (recomendado):
```bash
./gradlew run
```

#### Via Groovy diretamente:
```bash
groovy src/main/groovy/com/linketinder/Main.groovy
```

#### Build do projeto:
```bash
./gradlew build
```

## 🧪 Testes Automatizados

### ✅ Cobertura de Testes Atual

O projeto possui **30 testes unitários** usando **Spock Framework**:

```
📊 TESTES IMPLEMENTADOS:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
✅ DatabaseConnectionSpec     5/5 testes
✅ CandidatoDAOSpec          8/8 testes
✅ EmpresaDAOSpec            8/8 testes
✅ VagaDAOSpec               9/9 testes
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
   TOTAL:                   30/30 ✅
```

### Executar Todos os Testes

```bash
./gradlew test
```

**Saída esperada:**
```
BUILD SUCCESSFUL in 7s
30 tests completed, 30 passed
```

### Executar Testes Específicos

```bash
# Testar apenas a conexão com o banco:
./gradlew test --tests "com.linketinder.database.DatabaseConnectionSpec"

# Testar apenas o CandidatoDAO:
./gradlew test --tests "com.linketinder.dao.CandidatoDAOSpec"

# Testar apenas o EmpresaDAO:
./gradlew test --tests "com.linketinder.dao.EmpresaDAOSpec"

# Testar apenas o VagaDAO:
./gradlew test --tests "com.linketinder.dao.VagaDAOSpec"
```

### Relatórios de Teste

Após executar os testes, os relatórios estarão disponíveis em:

📄 **HTML (abra no navegador):**
```
build/reports/tests/test/index.html
```

📄 **XML (para CI/CD):**
```
build/test-results/test/*.xml
```

### O Que Cada Teste Valida

#### **DatabaseConnectionSpec (5 testes)**
- ✅ Conexão bem-sucedida com PostgreSQL
- ✅ Fechamento correto de recursos (Connection, Statement, ResultSet)
- ✅ URL de conexão correta
- ✅ Múltiplas conexões simultâneas

#### **CandidatoDAOSpec (8 testes)**
- ✅ Inserir candidato no banco
- ✅ Listar todos os candidatos
- ✅ Buscar candidato por ID
- ✅ Atualizar dados do candidato
- ✅ Deletar candidato
- ✅ Gerenciar competências (N:N)
- ✅ Retornar null para ID inexistente
- ✅ Não inserir candidato sem dados obrigatórios

#### **EmpresaDAOSpec (8 testes)**
- ✅ Inserir empresa no banco
- ✅ Listar todas as empresas
- ✅ Buscar empresa por ID
- ✅ Atualizar dados da empresa
- ✅ Deletar empresa
- ✅ Retornar null para ID inexistente
- ✅ Validações de campos obrigatórios

#### **VagaDAOSpec (9 testes)**
- ✅ Inserir vaga no banco
- ✅ Listar todas as vagas
- ✅ Listar vagas por empresa (1:N)
- ✅ Buscar vaga por ID
- ✅ Atualizar dados da vaga
- ✅ Deletar vaga
- ✅ Gerenciar competências (N:N)
- ✅ Retornar null para ID inexistente
- ✅ Não inserir vaga sem empresa (FK obrigatória)

### Executar Testes com Verbose

```bash
# Ver detalhes de cada teste:
./gradlew test --info

# Ver stack trace completo em caso de falha:
./gradlew test --stacktrace
```

## 📖 Menu do Sistema

```
LINKETINDER - MENU PRINCIPAL
==================================================
1. Listar Candidatos
2. Listar Empresas
3. Listar Vagas
4. Cadastrar novo Candidato
5. Cadastrar nova Empresa
6. Candidato curtir Vaga
7. Empresa curtir Candidato
8. Sair
==================================================
```


## 🎨 Frontend

### Descrição
Interface web desenvolvida em TypeScript com Vite como build tool e Tailwind CSS para estilização. Possui páginas interativas para visualização de candidatos, empresas e vagas.

### Estrutura do Frontend

```
frontend/
├── assets/              # Imagens e recursos estáticos
│   └── images/          # Avatares SVG dos usuários
├── css/                 # Estilos
│   └── tailwind.css     # Arquivo fonte do Tailwind
├── js/                  # JavaScript compilado (gerado)
├── ts/                  # Código fonte TypeScript
│   ├── candidatos.ts    # Lógica da página de candidatos
│   ├── empresas.ts      # Lógica da página de empresas
│   └── vagas.ts         # Lógica da página de vagas
├── public/              # Páginas HTML
│   ├── index.html       # Página inicial
│   ├── candidatos.html  # Lista de candidatos
│   ├── empresas.html    # Lista de empresas
│   └── vagas.html       # Lista de vagas
├── dist/                # Build de produção (gerado)
├── node_modules/        # Dependências npm
├── package.json         # Configuração npm
├── tsconfig.json        # Configuração TypeScript
├── vite.config.ts       # Configuração Vite
└── tailwind.config.js   # Configuração Tailwind CSS
```

### Como Executar o Frontend

1. **Instalar dependências:**
```bash
cd frontend
npm install
```

2. **Modo desenvolvimento (com hot reload):**
```bash
npm run dev
```
O servidor estará disponível em: `http://localhost:5173`

3. **Build para produção:**
```bash
npm run build
```
Os arquivos otimizados estarão em `frontend/dist/`

4. **Preview do build de produção:**
```bash
npm run preview
```

### Funcionalidades do Frontend

- ✅ **Página inicial** - Dashboard com estatísticas
- ✅ **Lista de candidatos** - Visualização de todos os candidatos cadastrados
- ✅ **Lista de empresas** - Visualização de todas as empresas cadastradas
- ✅ **Lista de vagas** - Visualização de vagas disponíveis
- ✅ **Design responsivo** - Adaptável a diferentes tamanhos de tela
- ✅ **Avatares personalizados** - Imagens SVG para cada perfil
- 🚧 **Integração com backend** - Em desenvolvimento

### Tecnologias Frontend

- **TypeScript 5.x** - Superset tipado do JavaScript
- **Vite 5.x** - Build tool moderno e rápido
- **Tailwind CSS 3.x** - Framework CSS utility-first
- **Chart.js** - Biblioteca para gráficos e visualizações
- **Vitest** - Framework de testes para Vite

### Scripts Disponíveis

```json
npm run dev      // Servidor de desenvolvimento
npm run build    // Build de produção
npm run preview  // Preview do build
npm run test     // Executar testes
```

## 🔄 Evolução do Projeto

### ✅ Versão 1.0 - Fundamentos
- Estrutura básica de POO
- Menu interativo
- Armazenamento em memória

### ✅ Versão 2.0 - Integração JDBC (ATUAL)
- **✅ Integração completa com PostgreSQL**
- **✅ Padrão DAO implementado**
- **✅ 3 DAOs completos (CandidatoDAO, EmpresaDAO, VagaDAO)**
- **✅ 30 testes unitários com Spock**
- **✅ Relacionamentos N:N e 1:N funcionando**
- **✅ CRUD completo de todas as entidades**
- **✅ Script SQL com dados de exemplo**

### 🚧 Versão 3.0 - Próximas Melhorias
- Integrar DAOs com Menu.groovy
- API REST com Spring Boot
- Sistema completo de matches
- Autenticação e autorização
- Deploy em cloud

## 🎓 Conceitos Aplicados

Este projeto demonstra conhecimento em:

### Backend
- ✅ **Programação Orientada a Objetos (POO)**
  - Classes, interfaces, herança
  - Encapsulamento e abstração
  
- ✅ **JDBC (Java Database Connectivity)**
  - Conexão com PostgreSQL
  - PreparedStatement (prevenção SQL injection)
  - Gerenciamento de recursos (try-finally)
  
- ✅ **Padrão DAO (Data Access Object)**
  - Separação de responsabilidades
  - Camada de persistência isolada
  
- ✅ **Relacionamentos de Banco de Dados**
  - 1:N (empresa → vagas)
  - N:N (candidato ↔ competências, vaga ↔ competências)
  
- ✅ **Testes Unitários**
  - Spock Framework
  - TDD (Test-Driven Development)
  - 100% de cobertura dos DAOs
  
- ✅ **Build Tools**
  - Gradle 8.14
  - Gerenciamento de dependências
  
- ✅ **SQL**
  - DDL (CREATE TABLE, ALTER TABLE)
  - DML (INSERT, UPDATE, DELETE, SELECT)
  - JOINs e relacionamentos

### Frontend
- ✅ TypeScript
- ✅ Vite
- ✅ Tailwind CSS
- ✅ Design responsivo

## 📚 Documentação Adicional

O projeto inclui documentação técnica detalhada na pasta `doc/`:

- 📄 `GUIA_INTEGRACAO_JDBC.txt` - Guia completo de integração JDBC
- 📄 `PROGRESSO_DESAFIO_K1-T9.txt` - Status e progresso do desafio
- 📄 `GUIA_ESTUDO_TDD_SPOCK.txt` - Guia de testes com Spock
- 📄 `LOGICA_TESTES_UNITARIOS_EXPLICACAO.txt` - Explicação dos testes

## 🐛 Troubleshooting

### Erro: "Connection refused"
```
Solução: Verifique se o PostgreSQL está rodando
sudo service postgresql start  # Linux
brew services start postgresql # macOS
```

### Erro: "Database does not exist"
```
Solução: Crie o banco de dados
psql -U postgres -c "CREATE DATABASE linketinder;"
```

### Erro: "Password authentication failed"
```
Solução: Verifique a senha em DatabaseConnection.groovy
private static final String PASSWORD = "sua_senha_correta"
```

### Testes falhando
```
Solução: Limpe o banco antes de executar os testes
Os testes fazem cleanup automático (DELETE) nas tabelas
Mas se persistir, execute: ./gradlew clean test
```

## 📊 Métricas do Projeto

```
📈 Código Backend:
   - 1800+ linhas de código Groovy
   - 7 classes de modelo
   - 3 DAOs completos
   - 30 testes unitários
   
🗄️ Banco de Dados:
   - 7 tabelas
   - 4 relacionamentos
   - 5 candidatos de exemplo
   - 5 empresas de exemplo
   - 10 competências
   - 6 vagas cadastradas
   
✅ Qualidade:
   - 100% cobertura de testes dos DAOs
   - 0 erros de compilação
   - 0 warnings
   - Código documentado
```

## 🤝 Contribuindo

Este é um projeto didático, mas sugestões são bem-vindas! 

1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

## 📝 Licença

Este projeto é de código aberto e está disponível para fins educacionais.

---

## 🎯 Sobre o Desafio K1-T9

Este projeto atende aos requisitos do desafio **K1-T9 (Banco de Dados - PostgreSQL)** do programa **Acelera ZG**:

### Requisitos Atendidos:
✅ Integração de 4 tabelas do BD com CRUD completo
✅ Uso exclusivo de JDBC (sem JPA/Hibernate)
✅ Padrão DAO implementado
✅ Relacionamentos N:N e 1:N funcionando
✅ Script SQL com 5 candidatos/empresas fictícios
✅ Código versionado no GitHub
✅ README atualizado com explicações

### Extras Implementados:
✅ 30 testes unitários com Spock Framework
✅ Documentação técnica completa
✅ Tratamento de exceções robusto
✅ Código didático com comentários explicativos

---

**Projeto desenvolvido por Nathalia Veiga para o programa Acelera ZG.**

**Status:** ✅ Integração JDBC completa - 30/30 testes passando! 🎉
