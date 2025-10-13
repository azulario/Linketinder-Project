# LinkeTinder

## Autor
Nathalia Veiga 

## Descrição
Projeto MVP de backend simplificado para sistema de contratação inspirado no LinkedIn e Tinder, desenvolvido em **Groovy com integração JDBC ao PostgreSQL**. 

Sistema permite cadastrar e listar candidatos e empresas, gerenciar vagas e implementar sistema de curtidas entre candidatos e empresas. Estrutura orientada a objetos, utilizando interface, herança e padrão DAO para persistência de dados.

> **Este projeto está em desenvolvimento para fins de aprendizado e aplicação dos conhecimentos adquiridos no programa Acelera ZG.**
> 
> **Versão atual:** Refatoração e simplificação com foco em JDBC e testes unitários com Spock.

## 🚀 Tecnologias Utilizadas

### Backend
- **Groovy 4.0.15** - Linguagem de programação
- **Gradle 8.14** - Build tool
- **PostgreSQL 42.7.1** - Banco de dados relacional
- **JDBC** - Integração com banco de dados
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
│   ├── Candidato.groovy
│   ├── Empresa.groovy
│   ├── Vaga.groovy
│   └── Usuarios.groovy (interface)
├── view/                          # Interface com usuário
│   └── Menu.groovy
├── database/                      # Camada de dados
│   ├── Database.groovy            # Gerenciador central
│   └── DatabaseConnection.groovy  # Conexão JDBC
└── dao/                          # Data Access Objects (em desenvolvimento)
    ├── CandidatoDAO.groovy
    ├── EmpresaDAO.groovy
    └── VagaDAO.groovy

src/test/groovy/com/linketinder/
├── database/
│   └── DatabaseConnectionSpec.groovy  # Testes de conexão
└── view/
    ├── MenuCadastroCandidatoSpec.groovy
    ├── MenuCadastroEmpresaSpec.groovy
    ├── MenuCurtidasCandidatoSpec.groovy
    └── MenuCurtidasEmpresaSpec.groovy
```

## 🎯 Funcionalidades (Backend)

### ✅ Implementadas
- Menu interativo no terminal
- Cadastro de candidatos (com data de nascimento)
- Cadastro de empresas
- Listagem de candidatos e empresas
- Sistema de curtidas:
  - Candidatos podem curtir vagas
  - Empresas podem curtir candidatos
- Testes unitários com Spock
- Conexão JDBC com PostgreSQL

### 🚧 Em Desenvolvimento
- Persistência completa no banco de dados (DAOs)
- CRUD completo de candidatos, empresas e vagas
- Sistema de competências (tabela N:N)
- Matches entre candidatos e empresas

## 💾 Banco de Dados

### Diagrama ER
![Diagrama do Banco](diagram-er.png)

**Software utilizado:** dbdiagram.io

### Estrutura
- **PostgreSQL** como SGBD
- **7 tabelas principais**:
  - `candidatos` - Dados dos candidatos
  - `empresas` - Dados das empresas
  - `vagas` - Vagas disponíveis
  - `competencias` - Lista de competências
  - `competencias_candidatos` - N:N entre candidatos e competências
  - `competencias_vagas` - N:N entre vagas e competências
  - `curtidas` - Registro de curtidas entre candidatos e empresas

### Scripts SQL
- `LinkeTinder.sql` - Script completo de criação do banco e dados de exemplo

## 🏃 Como Executar

### Pré-requisitos
1. **Java 21+** instalado
2. **Groovy 4.0+** ou usar via Gradle
3. **PostgreSQL** instalado e rodando
4. **Gradle** (ou usar o wrapper `./gradlew`)

### Configuração do Banco de Dados

1. Criar o banco:
```sql
CREATE DATABASE linketinder;
```

2. Executar o script SQL:
```bash
psql -U postgres -d linketinder -f LinkeTinder.sql
```

3. Configurar senha em `DatabaseConnection.groovy`:
```groovy
private static final String PASSWORD = "sua_senha_aqui"
```

### Executar a Aplicação

Via Gradle (recomendado):
```bash
./gradlew run
```

Via Groovy:
```bash
groovy src/main/groovy/com/linketinder/Main.groovy
```

## 🧪 Testes Automatizados

### Executar todos os testes
```bash
./gradlew test
```

### Executar teste específico
```bash
./gradlew test --tests "com.linketinder.database.DatabaseConnectionSpec"
```

### Relatórios de Teste
Após executar os testes, os relatórios estarão em:
- `build/reports/tests/test/index.html` (HTML - abra no navegador)
- `build/test-results/test/` (XML)

### Cobertura de Testes
- ✅ Conexão com banco de dados (5 cenários)
- ✅ Cadastro de candidatos
- ✅ Cadastro de empresas
- ✅ Sistema de curtidas (candidato → vaga)
- ✅ Sistema de curtidas (empresa → candidato)

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

## 🔄 Mudanças da Refatoração

### O que mudou do projeto anterior:
- ✅ **Simplificação da estrutura** - Código mais direto e fácil de entender
- ✅ **Integração JDBC** - Preparação para persistência real no PostgreSQL
- ✅ **Testes com Spock** - Framework moderno e expressivo
- ✅ **Padrão DAO** - Separação da lógica de acesso a dados
- ✅ **Models atualizados** - Campos necessários para banco (id, timestamps, etc)
- ✅ **Data de nascimento** - Candidatos agora informam data de nascimento em vez de idade
- ✅ **Sistema de curtidas simplificado** - Lógica mantida simples para fins didáticos

## 📚 Aprendizados Aplicados

Este projeto demonstra:
- ✅ Programação Orientada a Objetos (POO)
- ✅ Interfaces e Herança
- ✅ Integração JDBC com PostgreSQL
- ✅ Padrão DAO (Data Access Object)
- ✅ Testes Unitários com Spock
- ✅ TDD (Test-Driven Development)
- ✅ Estrutura de projeto Gradle
- ✅ Versionamento com Git

---

**Projeto desenvolvido por Nathalia Veiga para fins didáticos no programa Acelera ZG.**

