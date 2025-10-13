package com.linketinder.dao

import com.linketinder.database.DatabaseConnection
import com.linketinder.model.Vaga
import com.linketinder.model.Empresa
import spock.lang.Specification
import java.sql.Connection

/**
 * VagaDAOSpec - Testes TDD para VagaDAO
 *
 * Testa todas as operações CRUD de vagas no banco de dados:
 * - Inserir vaga (com competências N:N)
 * - Listar todas as vagas
 * - Listar vagas por empresa
 * - Buscar vaga por ID
 * - Atualizar vaga
 * - Deletar vaga
 *
 * IMPORTANTE: Os testes usam o banco real PostgreSQL
 */
class VagaDAOSpec extends Specification {

    VagaDAO dao
    EmpresaDAO empresaDAO
    Connection conn
    Empresa empresaTeste

    def setup() {
        dao = new VagaDAO()
        empresaDAO = new EmpresaDAO()
        conn = DatabaseConnection.getConnection()

        // Limpar tabelas relacionadas
        conn.createStatement().execute("DELETE FROM competencias_vagas")
        conn.createStatement().execute("DELETE FROM vagas")
        conn.createStatement().execute("DELETE FROM empresas")

        // Criar uma empresa de teste para associar às vagas
        empresaTeste = new Empresa(
            "Tech Company",
            "tech@company.com",
            "12.345.678/0001-99",
            "Brasil",
            "SP",
            "01310-100",
            "Empresa de tecnologia",
            ["Java", "Python"]
        )
        empresaDAO.inserir(empresaTeste)
    }

    def cleanup() {
        if (conn != null && !conn.isClosed()) {
            conn.close()
        }
    }

    def "deve inserir uma nova vaga no banco de dados"() {
        given: "uma vaga válida"
        def vaga = new Vaga(
            "Desenvolvedor Java Sênior",
            "Vaga para desenvolvedor Java com 5+ anos de experiência",
            ["Java", "Spring Boot", "Microservices"],
            empresaTeste
        )
        vaga.cidade = "São Paulo"

        when: "inserir a vaga no banco"
        dao.inserir(vaga)

        then: "a vaga deve ter um ID gerado"
        vaga.id != null
        vaga.id > 0
    }

    def "deve inserir vaga com competências na tabela N:N"() {
        given: "uma vaga com 3 competências"
        def vaga = new Vaga(
            "Analista de Dados",
            "Análise e visualização de dados",
            ["Python", "SQL", "Power BI"],
            empresaTeste
        )
        vaga.cidade = "Rio de Janeiro"

        when: "inserir a vaga"
        dao.inserir(vaga)

        and: "buscar a vaga inserida"
        def vagaBuscada = dao.buscarPorId(vaga.id)

        then: "deve ter as 3 competências associadas"
        vagaBuscada != null
        vagaBuscada.competencias.size() == 3
        vagaBuscada.competencias.containsAll(["Python", "SQL", "Power BI"])
    }

    def "deve listar todas as vagas cadastradas"() {
        given: "duas vagas cadastradas"
        def vaga1 = new Vaga(
            "Designer UX",
            "Design de experiência do usuário",
            ["Figma", "Adobe XD"],
            empresaTeste
        )
        vaga1.cidade = "Belo Horizonte"

        def vaga2 = new Vaga(
            "DevOps Engineer",
            "Infraestrutura e CI/CD",
            ["Docker", "Kubernetes", "AWS"],
            empresaTeste
        )
        vaga2.cidade = "Curitiba"

        dao.inserir(vaga1)
        dao.inserir(vaga2)

        when: "listar todas as vagas"
        def vagas = dao.listar()

        then: "deve retornar as 2 vagas"
        vagas.size() == 2
        vagas*.titulo.containsAll(["Designer UX", "DevOps Engineer"])
    }

    def "deve listar vagas de uma empresa específica"() {
        given: "uma segunda empresa"
        def empresa2 = new Empresa(
            "Another Company",
            "another@company.com",
            "98.765.432/0001-11",
            "Brasil",
            "RJ",
            "20000-000",
            "Outra empresa",
            ["JavaScript"]
        )
        empresaDAO.inserir(empresa2)

        and: "vagas de ambas as empresas"
        def vaga1 = new Vaga("Vaga 1", "Descrição 1", ["Java"], empresaTeste)
        def vaga2 = new Vaga("Vaga 2", "Descrição 2", ["Python"], empresaTeste)
        def vaga3 = new Vaga("Vaga 3", "Descrição 3", ["JavaScript"], empresa2)

        dao.inserir(vaga1)
        dao.inserir(vaga2)
        dao.inserir(vaga3)

        when: "listar vagas da primeira empresa"
        def vagasEmpresa1 = dao.listarPorEmpresa(empresaTeste.id)

        then: "deve retornar apenas as 2 vagas da primeira empresa"
        vagasEmpresa1.size() == 2
        vagasEmpresa1.every { it.empresaId == empresaTeste.id }
    }

    def "deve buscar uma vaga por ID"() {
        given: "uma vaga cadastrada"
        def vagaOriginal = new Vaga(
            "Full Stack Developer",
            "Desenvolvimento front e back-end",
            ["React", "Node.js", "MongoDB"],
            empresaTeste
        )
        vagaOriginal.cidade = "Florianópolis"
        dao.inserir(vagaOriginal)
        def idGerado = vagaOriginal.id

        when: "buscar a vaga pelo ID"
        def vagaEncontrada = dao.buscarPorId(idGerado)

        then: "deve retornar a vaga correta"
        vagaEncontrada != null
        vagaEncontrada.id == idGerado
        vagaEncontrada.titulo == "Full Stack Developer"
        vagaEncontrada.cidade == "Florianópolis"
    }

    def "deve retornar null ao buscar vaga inexistente"() {
        given: "um ID que não existe"
        def idInexistente = 99999

        when: "buscar pelo ID inexistente"
        def vaga = dao.buscarPorId(idInexistente)

        then: "deve retornar null"
        vaga == null
    }

    def "deve atualizar os dados de uma vaga"() {
        given: "uma vaga cadastrada"
        def vaga = new Vaga(
            "QA Tester",
            "Testes automatizados",
            ["Selenium", "JUnit"],
            empresaTeste
        )
        vaga.cidade = "Porto Alegre"
        dao.inserir(vaga)

        when: "atualizar os dados da vaga"
        vaga.titulo = "QA Engineer Sênior"
        vaga.descricao = "Testes automatizados e manuais"
        vaga.cidade = "Porto Alegre - RS"
        vaga.competencias = ["Selenium", "JUnit", "TestNG", "Cucumber"]
        dao.atualizar(vaga)

        and: "buscar a vaga atualizada"
        def vagaAtualizada = dao.buscarPorId(vaga.id)

        then: "os dados devem estar atualizados"
        vagaAtualizada.titulo == "QA Engineer Sênior"
        vagaAtualizada.descricao == "Testes automatizados e manuais"
        vagaAtualizada.cidade == "Porto Alegre - RS"
        vagaAtualizada.competencias.size() == 4
        vagaAtualizada.competencias.contains("Cucumber")
    }

    def "deve deletar uma vaga do banco"() {
        given: "uma vaga cadastrada"
        def vaga = new Vaga(
            "Scrum Master",
            "Gerenciamento ágil de projetos",
            ["Scrum", "Jira", "Agile"],
            empresaTeste
        )
        vaga.cidade = "Brasília"
        dao.inserir(vaga)
        def idParaDeletar = vaga.id

        when: "deletar a vaga"
        dao.deletar(idParaDeletar)

        and: "tentar buscar a vaga deletada"
        def vagaDeletada = dao.buscarPorId(idParaDeletar)

        then: "não deve encontrar a vaga"
        vagaDeletada == null
    }

    def "deve deletar competências ao deletar vaga"() {
        given: "uma vaga com competências"
        def vaga = new Vaga(
            "Tech Lead",
            "Liderança técnica",
            ["Java", "Architecture", "Leadership"],
            empresaTeste
        )
        dao.inserir(vaga)
        def vagaId = vaga.id

        when: "deletar a vaga"
        dao.deletar(vagaId)

        and: "verificar se as competências foram deletadas"
        def stmt = conn.prepareStatement("SELECT COUNT(*) FROM competencias_vagas WHERE vaga_id = ?")
        stmt.setInt(1, vagaId)
        def rs = stmt.executeQuery()
        rs.next()
        def count = rs.getInt(1)

        then: "não deve haver competências associadas"
        count == 0
    }

    def "não deve inserir vaga sem empresa associada"() {
        given: "uma vaga sem empresa"
        def vaga = new Vaga(
            "Teste",
            "Descrição teste",
            ["Java"],
            null // empresa nula
        )

        when: "tentar inserir a vaga"
        dao.inserir(vaga)

        then: "deve lançar exceção"
        thrown(Exception)
    }
}
package com.linketinder.dao

import com.linketinder.database.DatabaseConnection
import com.linketinder.model.Candidato
import spock.lang.Specification
import java.sql.Connection
import java.time.LocalDate

/**
 * CandidatoDAOSpec - Testes TDD para CandidatoDAO
 *
 * Testa todas as operações CRUD de candidatos no banco de dados:
 * - Inserir candidato
 * - Listar todos os candidatos
 * - Buscar candidato por ID
 * - Atualizar candidato
 * - Deletar candidato
 *
 * IMPORTANTE: Os testes usam o banco real PostgreSQL
 * Certifique-se que o banco 'linketinder' está rodando e configurado!
 */
class CandidatoDAOSpec extends Specification {

    CandidatoDAO dao
    Connection conn

    // Executado ANTES de cada teste
    def setup() {
        dao = new CandidatoDAO()
        conn = DatabaseConnection.getConnection()

        // Limpar tabela de candidatos antes de cada teste
        // Isso garante que os testes sejam isolados e independentes
        conn.createStatement().execute("DELETE FROM competencias_candidatos")
        conn.createStatement().execute("DELETE FROM candidatos")
    }

    // Executado DEPOIS de cada teste
    def cleanup() {
        if (conn != null && !conn.isClosed()) {
            conn.close()
        }
    }

    def "deve inserir um novo candidato no banco de dados"() {
        given: "um candidato válido"
        def candidato = new Candidato(
            "João Silva",
            "joao@email.com",
            "123.456.789-00",
            LocalDate.of(1990, 5, 15),
            "SP",
            "01234-567",
            "Desenvolvedor Java",
            ["Java", "Spring", "SQL"]
        )

        when: "inserir o candidato no banco"
        dao.inserir(candidato)

        then: "o candidato deve ter um ID gerado"
        candidato.id != null
        candidato.id > 0
    }

    def "deve listar todos os candidatos cadastrados"() {
        given: "dois candidatos cadastrados no banco"
        def candidato1 = new Candidato(
            "Maria Santos",
            "maria@email.com",
            "111.222.333-44",
            LocalDate.of(1995, 8, 20),
            "RJ",
            "20000-000",
            "Desenvolvedora Python",
            ["Python", "Django"]
        )
        def candidato2 = new Candidato(
            "Pedro Costa",
            "pedro@email.com",
            "555.666.777-88",
            LocalDate.of(1988, 3, 10),
            "MG",
            "30000-000",
            "Analista de Dados",
            ["SQL", "Power BI"]
        )

        dao.inserir(candidato1)
        dao.inserir(candidato2)

        when: "listar todos os candidatos"
        def candidatos = dao.listar()

        then: "deve retornar os 2 candidatos cadastrados"
        candidatos.size() == 2
        candidatos*.nome.containsAll(["Maria Santos", "Pedro Costa"])
    }

    def "deve buscar um candidato por ID"() {
        given: "um candidato cadastrado no banco"
        def candidatoOriginal = new Candidato(
            "Ana Paula",
            "ana@email.com",
            "999.888.777-66",
            LocalDate.of(1992, 12, 25),
            "PR",
            "80000-000",
            "Designer UX",
            ["Figma", "Adobe XD"]
        )
        dao.inserir(candidatoOriginal)
        def idGerado = candidatoOriginal.id

        when: "buscar o candidato pelo ID"
        def candidatoEncontrado = dao.buscarPorId(idGerado)

        then: "deve retornar o candidato correto"
        candidatoEncontrado != null
        candidatoEncontrado.id == idGerado
        candidatoEncontrado.nome == "Ana Paula"
        candidatoEncontrado.email == "ana@email.com"
    }

    def "deve retornar null ao buscar candidato inexistente"() {
        given: "um ID que não existe no banco"
        def idInexistente = 99999

        when: "buscar pelo ID inexistente"
        def candidato = dao.buscarPorId(idInexistente)

        then: "deve retornar null"
        candidato == null
    }

    def "deve atualizar os dados de um candidato"() {
        given: "um candidato cadastrado no banco"
        def candidato = new Candidato(
            "Carlos Silva",
            "carlos@email.com",
            "123.123.123-12",
            LocalDate.of(1985, 7, 5),
            "BA",
            "40000-000",
            "Gerente de Projetos",
            ["Scrum", "Jira"]
        )
        dao.inserir(candidato)

        when: "atualizar os dados do candidato"
        candidato.nome = "Carlos Silva Santos"
        candidato.email = "carlos.santos@email.com"
        candidato.descricao = "Gerente de Projetos Sênior"
        dao.atualizar(candidato)

        and: "buscar o candidato atualizado"
        def candidatoAtualizado = dao.buscarPorId(candidato.id)

        then: "os dados devem estar atualizados"
        candidatoAtualizado.nome == "Carlos Silva Santos"
        candidatoAtualizado.email == "carlos.santos@email.com"
        candidatoAtualizado.descricao == "Gerente de Projetos Sênior"
    }

    def "deve deletar um candidato do banco"() {
        given: "um candidato cadastrado no banco"
        def candidato = new Candidato(
            "Fernanda Lima",
            "fernanda@email.com",
            "777.888.999-00",
            LocalDate.of(1993, 4, 18),
            "RS",
            "90000-000",
            "QA Tester",
            ["Selenium", "JUnit"]
        )
        dao.inserir(candidato)
        def idParaDeletar = candidato.id

        when: "deletar o candidato"
        dao.deletar(idParaDeletar)

        and: "tentar buscar o candidato deletado"
        def candidatoDeletado = dao.buscarPorId(idParaDeletar)

        then: "não deve encontrar o candidato"
        candidatoDeletado == null
    }

    def "deve listar candidatos com suas competências"() {
        given: "um candidato com múltiplas competências"
        def candidato = new Candidato(
            "Roberto Alves",
            "roberto@email.com",
            "444.555.666-77",
            LocalDate.of(1991, 9, 30),
            "SC",
            "88000-000",
            "Full Stack Developer",
            ["JavaScript", "React", "Node.js", "MongoDB"]
        )
        dao.inserir(candidato)

        when: "listar os candidatos"
        def candidatos = dao.listar()
        def candidatoEncontrado = candidatos.find { it.id == candidato.id }

        then: "deve retornar o candidato com todas as competências"
        candidatoEncontrado != null
        candidatoEncontrado.competencias.size() == 4
        candidatoEncontrado.competencias.containsAll(["JavaScript", "React", "Node.js", "MongoDB"])
    }

    def "não deve inserir candidato com dados inválidos"() {
        given: "um candidato com email nulo"
        def candidato = new Candidato(
            "Teste",
            null, // email nulo
            "123.456.789-00",
            LocalDate.of(1990, 1, 1),
            "SP",
            "01234-567",
            "Teste",
            ["Java"]
        )

        when: "tentar inserir o candidato"
        dao.inserir(candidato)

        then: "deve lançar exceção"
        thrown(Exception)
    }
}

