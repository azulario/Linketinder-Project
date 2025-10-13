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

