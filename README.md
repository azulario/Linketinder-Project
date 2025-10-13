# LinkeTinder

## Autor
Nathalia Veiga 

## Descrição
Sistema de contratação inspirado no LinkedIn e Tinder, desenvolvido em **Groovy com integração JDBC ao PostgreSQL**. 

Permite cadastrar candidatos e empresas, gerenciar vagas e implementar sistema de curtidas/matches. Utiliza POO, padrão DAO e JDBC puro para persistência de dados.

> **Projeto desenvolvido para o programa Acelera ZG - Desafio K1-T9 (Banco de Dados PostgreSQL)**
>
## 🚀 Tecnologias

**Backend:**
- Groovy 4.0.15 | Gradle 8.14
- PostgreSQL 14+ com JDBC puro (sem ORM)
- Spock 2.3 (testes unitários)

**Frontend:**
- TypeScript | Vite | Tailwind CSS

## 📁 Estrutura Principal

```
src/main/groovy/com/linketinder/
├── dao/                          # ✅ Padrão DAO implementado
│   ├── CandidatoDAO.groovy      # CRUD completo + relação N:N competências
│   ├── EmpresaDAO.groovy        # CRUD completo
│   └── VagaDAO.groovy           # CRUD completo + relações 1:N e N:N
├── database/
│   └── DatabaseConnection.groovy # Conexão JDBC com PostgreSQL
├── model/                        # Entidades (Candidato, Empresa, Vaga)
└── view/                         # Menu interativo

src/test/groovy/                  # Testes unitários com Spock
LinkeTinder.sql                   # Script completo do banco
```

## 🎯 Funcionalidades

- ✅ CRUD completo de Candidatos, Empresas e Vagas
- ✅ Sistema de curtidas entre candidatos e vagas
- ✅ Persistência PostgreSQL via JDBC
- ✅ Relacionamentos N:N (candidato↔competências, vaga↔competências)
- ✅ Relacionamento 1:N (empresa→vagas)
- ✅ 30 testes unitários com Spock Framework

## 💾 Banco de Dados

### Diagrama ER
![Diagrama do Banco](diagram-er.png)

### Estrutura
7 tabelas: `candidatos`, `empresas`, `vagas`, `competencias`, `candidato_competencias`, `competencias_vagas`, `curtidas`

**Relacionamentos:**
- N:N - Candidato ↔ Competências (via `candidato_competencias`)
- N:N - Vaga ↔ Competências (via `competencias_vagas`)
- 1:N - Empresa → Vagas (FK `empresa_id`)

## 🏃 Como Executar

### 1. Pré-requisitos
- Java 21+
- PostgreSQL 14+ rodando
- Gradle 8.14+ (ou usar `./gradlew`)

### 2. Configurar Banco
```bash
# Criar banco
psql -U postgres -c "CREATE DATABASE linketinder;"

# Executar script
psql -U postgres -d linketinder -f LinkeTinder.sql
```

### 3. Configurar Senha
Edite `src/main/groovy/com/linketinder/database/DatabaseConnection.groovy`:
```groovy
private static final String PASSWORD = "sua_senha_aqui"
```

### 4. Executar
```bash
./gradlew run        # Rodar aplicação
./gradlew test       # Rodar testes (30/30 passando)
./gradlew build      # Build do projeto
```


**Relatório HTML:** `build/reports/tests/test/index.html`

## 🎨 Frontend 

```bash
cd frontend
npm install
npm run dev          # http://localhost:5173
```

Páginas: candidatos, empresas, vagas (visualização e estatísticas)

## 🎓 Conceitos Aplicados

- **POO:** Classes, interfaces, herança, encapsulamento
- **JDBC:** Conexão PostgreSQL, PreparedStatement, gerenciamento de recursos
- **Padrão DAO:** Separação de responsabilidades, camada de persistência isolada
- **Relacionamentos DB:** 1:N e N:N com tabelas intermediárias
- **TDD:** Testes unitários com Spock Framework
- **SQL:** DDL, DML, JOINs, Foreign Keys, CASCADE
- **DOM:** Manipulação do DOM com TypeScript
- **CSS:** Estilização responsiva com Tailwind CSS

## 🐛 Troubleshooting

| Erro | Solução |
|------|---------|
| Connection refused | `sudo service postgresql start` |
| Database does not exist | `psql -U postgres -c "CREATE DATABASE linketinder;"` |
| Password authentication failed | Verificar senha em `DatabaseConnection.groovy` |

## 📊 Métricas

- **1800+ linhas** de código Groovy
- **7 tabelas** no banco de dados
- **30 testes** unitários (100% cobertura DAOs)
- **3 DAOs** completos (15 métodos CRUD)
- **4 relacionamentos** (3 N:N + 1 1:N)

---

**Projeto desenvolvido por Nathalia Veiga | Acelera ZG 2025**
