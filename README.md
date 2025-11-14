# LinkeTinder - Sistema de Recrutamento

**Autor:** Nathalia Veiga | **Programa:** Acelera ZG 2025

## Descrição

Sistema de contratação inspirado no LinkedIn e Tinder. Aplicação full-stack com backend em **Groovy + JDBC/PostgreSQL** e frontend em **TypeScript + Vite**. O diferencial deste projeto é a **implementação de REST API completa SEM frameworks** (Spring, Grails, etc.), usando apenas Servlets puros + Tomcat.

### Destaques Técnicos
- REST API sem frameworks (Jakarta Servlet + Tomcat)
- 46 testes automatizados com Spock Framework (TDD)
- Arquitetura MVC com padrões DAO e Singleton
- PostgreSQL com relacionamentos N:N usando JDBC puro
- Deploy manual no Apache Tomcat

---

## Estrutura do Projeto

```
LinkeTinder/
├── backend/                 # Backend Groovy + PostgreSQL
│   ├── src/main/groovy/
│   │   ├── servlet/        # REST API (CandidatoServlet, EmpresaServlet, VagaServlet)
│   │   ├── service/        # Lógica de negócio
│   │   ├── dao/            # Acesso a dados (BaseDAO, CandidatoDAO, etc)
│   │   ├── model/          # Entidades (Candidato, Empresa, Vaga)
│   │   ├── dto/            # Data Transfer Objects
│   │   └── util/           # Adaptadores JSON
│   ├── src/test/groovy/    # 46 testes (Spock)
│   └── build.gradle        # Configuração + plugin WAR
├── frontend/               # TypeScript + Vite + Tailwind
├── doc/rest-api/          # Documentação da API
└── README.md
```

---

## Backend - Groovy + PostgreSQL + Servlets

### Tecnologias
- Groovy 4.0.15 + Gradle 8.14
- PostgreSQL 14+ com JDBC puro
- Jakarta Servlet API 6.0 + Apache Tomcat 10
- Gson 2.10 para JSON
- Spock 2.3 para testes

### Funcionalidades
- CRUD completo para Candidatos, Empresas e Vagas
- Relacionamentos N:N (Candidato/Vaga ↔ Competências)
- REST API com 9 endpoints (GET, POST)
- 46 testes automatizados (TDD)
- Padrões: MVC, DAO, Singleton, Strategy, DTO

### Banco de Dados

![Diagrama ER](backend/diagram-er.png)

8 tabelas com relacionamentos N:N via tabelas intermediárias:
- `candidatos`, `empresas`, `vagas` - Entidades principais
- `enderecos` - Normalizado para reutilização
- `competencias` - Habilidades técnicas
- `candidato_competencias`, `competencias_vagas` - Relacionamentos
- `curtidas` - Sistema de matches


### Como Executar

```bash
# 1. Criar banco
psql -U postgres -c "CREATE DATABASE linketinder;"
psql -U postgres -d linketinder -f backend/LinkeTinder.sql

# 2. Configurar senha em DatabaseConnection.groovy

# 3. Executar
cd backend
./gradlew run        # Rodar aplicação CLI
./gradlew test       # Rodar testes
./gradlew build war  # Gerar WAR para deploy
```

## REST API - Implementação SEM Frameworks

### Visão Geral

Este projeto implementa uma **REST API completa sem utilizar frameworks** como Spring, Grails ou Micronaut. A implementação utiliza **Servlets puros** (Jakarta Servlet API) rodando no **Apache Tomcat 10**.

**Por quê?**
- Compreensão profunda do protocolo HTTP
- Controle total sobre requisições/respostas
- Conhecimento dos fundamentos antes de usar frameworks
### Endpoints Implementados

**Candidatos** `/api/candidatos`

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| GET | `/api/candidatos` | Lista todos candidatos | 200 OK |
| GET | `/api/candidatos/{id}` | Busca candidato por ID | 200 OK / 404 |
| POST | `/api/candidatos` | Cadastra novo candidato | 201 Created / 400 |

**Empresas** `/api/empresas`

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| GET | `/api/empresas` | Lista todas empresas | 200 OK |
| GET | `/api/empresas/{id}` | Busca empresa por ID | 200 OK / 404 |
| POST | `/api/empresas` | Cadastra nova empresa | 201 Created / 400 |

**Vagas** `/api/vagas`

| GET | `/api/vagas` | Lista todas vagas | 200 OK |
| GET | `/api/vagas/{id}` | Busca vaga por ID | 200 OK / 404 |
| POST | `/api/vagas` | Cadastra nova vaga | 201 Created / 400 |

### Implementação

**Estrutura de Servlets:**

Cada entidade possui um Servlet dedicado:

```groovy
@WebServlet(name = "CandidatoServlet", urlPatterns = ["/api/candidatos/*"])
class CandidatoServlet extends HttpServlet {
    // ...existing code...
}
```

**Processamento de Requisições:**
```groovy
protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    String pathInfo = request.getPathInfo()
    
    if (pathInfo == null || pathInfo == "/") {
        // Listar todos
        List<Candidato> candidatos = candidatoService.listarTodos()
        Map resultado = [
            sucesso: true,
            total: candidatos.size(),
            candidatos: candidatos
        ]
        response.status = HttpServletResponse.SC_OK
        response.writer.write(gson.toJson(resultado))
    } else {
        // Buscar por ID
        Integer id = Integer.parseInt(pathInfo.substring(1))
        Candidato candidato = candidatoService.buscarPorId(id)
        // ...
    }
}
```

**POST - Cadastrar:**
```groovy
protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    try {
        // 1. Ler corpo da requisição
        String body = request.reader.text
        
        // 2. Desserializar JSON para DTO
        CandidatoDTO dto = gson.fromJson(body, CandidatoDTO)
        
        // 3. Validar campos obrigatórios
        if (!dto.nome || !dto.email || !dto.pais || !dto.estado) {
            enviarErro(response, 400, "Campos obrigatórios faltando")
            return
        }
        
        // 4. Delegar para service
        Candidato candidato = candidatoService.cadastrar(dto)
        
        // 5. Retornar resposta
        Map resultado = [
            sucesso: true,
            mensagem: "Candidato cadastrado com sucesso",
            candidato: candidato
        ]
        response.status = HttpServletResponse.SC_CREATED
        response.writer.write(gson.toJson(resultado))
        
    } catch (JsonSyntaxException e) {
        enviarErro(response, 400, "JSON inválido")
    } catch (Exception e) {
        enviarErro(response, 500, "Erro interno: ${e.message}")
    }
}
```

**Serialização JSON:**

Adaptadores customizados para LocalDate/LocalDateTime permitem serialização correta de datas:

```groovy
class LocalDateAdapter extends TypeAdapter<LocalDate> {
    void write(JsonWriter out, LocalDate value) {
        out.value(value?.format(DateTimeFormatter.ISO_LOCAL_DATE))
    }
    // ...
}
```

**Deployment:**

```bash
# Gerar WAR
./gradlew clean build war

# Deploy no Tomcat
cp build/libs/linketinder-api-1.0-SNAPSHOT.war $CATALINA_HOME/webapps/
catalina.sh start
```

### Testes Automatizados (TDD)

46 testes desenvolvidos usando TDD (Test-Driven Development):
- CandidatoServletSpec - 12 testes
- EmpresaServletSpec - 17 testes  
- VagaServletSpec - 17 testes

Cobertura completa de GET, POST, validações e exceções.

### Exemplos de Uso
```bash
curl http://localhost:8080/linketinder-api/api/candidatos
```

**Resposta:**
```json
{
  "sucesso": true,
  "total": 2,
  "candidatos": [
    {
      "id": 1,
      "nome": "João Silva",
      "email": "joao@email.com",
      "competencias": ["Java", "Groovy"]
    }
  ]
}
```

#### Cadastrar Candidato (POST)
```bash
curl -X POST http://localhost:8080/linketinder-api/api/candidatos \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria Santos",
    "sobrenome": "Santos",
    "email": "maria@email.com",
    "cpf": "987.654.321-00",
    "dataDeNascimento": "1995-08-20",
    "pais": "Brasil",
    "estado": "SP",
    "cidade": "São Paulo",
    "cep": "01234-567",
    "descricao": "Desenvolvedora Frontend",
    "competencias": ["JavaScript", "React", "TypeScript"]
  }'
```

### Princípios REST Aplicados

- Recursos identificáveis via URLs
- Métodos HTTP semânticos (GET, POST)
- Stateless (requisições independentes)
- Formato JSON para dados
- Códigos HTTP apropriados (200, 201, 400, 404, 500)
- Validação de entrada e tratamento de exceções

### Documentação Completa

- [Implementação REST API](doc/rest-api/IMPLEMENTACAO_REST_API_SEM_FRAMEWORK.md)
- [Exemplos de Requisições](doc/rest-api/EXEMPLOS_REQUISICOES_API.md)
- [Guia de Deploy Tomcat](doc/rest-api/GUIA_DEPLOY_TOMCAT.md)

---

## Frontend - TypeScript + Vite

### Tecnologias
- TypeScript + Vite
- Tailwind CSS
- Chart.js para visualizações

### Como Executar

```bash
cd frontend
npm install
npm run dev          # http://localhost:5173
npm run build        # Produção
```

---

## Como Executar o Projeto Completo

### Backend (REST API)

```bash
# 1. Banco de dados
psql -U postgres -c "CREATE DATABASE linketinder;"
psql -U postgres -d linketinder -f backend/LinkeTinder.sql

# 2. Configurar senha em DatabaseConnection.groovy

# 3. Deploy
cd backend
./gradlew clean build war -x test
cp build/libs/linketinder-api-1.0-SNAPSHOT.war $CATALINA_HOME/webapps/linketinder-api.war
catalina.sh start

# 4. Testar
curl http://localhost:8080/linketinder-api/api/candidatos
```

### Frontend

```bash
cd frontend
npm install && npm run dev
# Acesse: http://localhost:5173
```

---

## Estatísticas do Projeto

| Métrica | Valor |
|---------|-------|
| Linhas de Código | ~5.000+ |
| Classes Groovy | 30+ |
| Testes Automatizados | 46 (100%) |
| Endpoints REST | 9 |
| Tabelas do Banco | 8 |
| Padrões de Design | 5 (MVC, DAO, Singleton, Strategy, DTO) |

---

## Tecnologias e Conceitos

### Backend
- POO em Groovy
- JDBC puro (sem ORM)
- Padrões: MVC, DAO, Singleton, Service, Strategy
- SQL avançado (JOINs, N:N, índices)
- TDD com Spock Framework
- REST API sem frameworks (Servlets + Tomcat)
- Serialização JSON

### Frontend
- TypeScript
- Manipulação do DOM
- Tailwind CSS
- Vite

---

## Links Úteis

- [Jakarta Servlet](https://jakarta.ee/specifications/servlet/)
- [PostgreSQL](https://www.postgresql.org/docs/)
- [Spock Framework](https://spockframework.org/)
- [Apache Tomcat](https://tomcat.apache.org/)
- [Groovy](https://groovy-lang.org/documentation.html)

---

## Autor

**Nathalia Veiga**  
Projeto desenvolvido no programa **Acelera ZG 2025**

---

*Última atualização: 14 de Novembro de 2025*
