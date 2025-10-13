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
        conn.createStatement().execute("DELETE FROM candidato_competencias")
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
        def candidato = new Candidato(
            "Ana Lima",
            "ana@email.com",
            "999.888.777-66",
            LocalDate.of(1993, 12, 25),
            "BA",
            "40000-000",
            "Analista QA",
            ["Selenium", "JUnit"]
        )
        dao.inserir(candidato)

        when: "buscar o candidato pelo ID"
        def candidatoEncontrado = dao.buscarPorId(candidato.id)

        then: "deve retornar o candidato correto"
        candidatoEncontrado != null
        candidatoEncontrado.nome == "Ana Lima"
        candidatoEncontrado.email == "ana@email.com"
    }

    def "deve retornar null ao buscar candidato inexistente"() {
        when: "buscar um ID que não existe"
        def candidato = dao.buscarPorId(99999)

        then: "deve retornar null"
        candidato == null
    }

    def "deve atualizar os dados de um candidato"() {
        given: "um candidato cadastrado"
        def candidato = new Candidato(
            "Carlos Souza",
            "carlos@email.com",
            "777.666.555-44",
            LocalDate.of(1991, 6, 10),
            "RS",
            "90000-000",
            "Desenvolvedor Mobile",
            ["Kotlin", "Swift"]
        )
        dao.inserir(candidato)

        when: "atualizar os dados do candidato"
        candidato.nome = "Carlos Alberto Souza"
        candidato.descricao = "Desenvolvedor Mobile Senior"
        candidato.competencias = ["Kotlin", "Swift", "Flutter"]
        dao.atualizar(candidato)

        and: "buscar o candidato atualizado"
        def candidatoAtualizado = dao.buscarPorId(candidato.id)

        then: "os dados devem estar atualizados"
        candidatoAtualizado.nome == "Carlos Alberto Souza"
        candidatoAtualizado.descricao == "Desenvolvedor Mobile Senior"
        candidatoAtualizado.competencias.size() == 3
        candidatoAtualizado.competencias.contains("Flutter")
    }

    def "deve deletar um candidato do banco"() {
        given: "um candidato cadastrado"
        def candidato = new Candidato(
            "Paula Rocha",
            "paula@email.com",
            "444.333.222-11",
            LocalDate.of(1997, 4, 5),
            "PR",
            "80000-000",
            "Scrum Master",
            ["Agile", "Scrum"]
        )
        dao.inserir(candidato)
        def idCandidato = candidato.id

        when: "deletar o candidato"
        dao.deletar(idCandidato)

        and: "tentar buscar o candidato deletado"
        def candidatoDeletado = dao.buscarPorId(idCandidato)

        then: "não deve encontrar o candidato"
        candidatoDeletado == null
    }

    def "deve listar candidatos com suas competências"() {
        given: "um candidato com múltiplas competências"
        def candidato = new Candidato(
            "Ricardo Alves",
            "ricardo@email.com",
            "111.999.888-77",
            LocalDate.of(1989, 1, 30),
            "CE",
            "60000-000",
            "Arquiteto de Software",
            ["Java", "Microservices", "AWS", "Docker"]
        )
        dao.inserir(candidato)

        when: "listar os candidatos"
        def candidatos = dao.listar()

        then: "deve retornar as competências corretamente"
        candidatos.size() == 1
        def candidatoEncontrado = candidatos[0]
        candidatoEncontrado.competencias.size() == 4
        candidatoEncontrado.competencias.containsAll(["Java", "Microservices", "AWS", "Docker"])
    }

    def "não deve inserir candidato com dados inválidos"() {
        given: "um candidato com email inválido (null)"
        def candidato = new Candidato(
            "Nome Teste",
            null,  // email null
            "123.456.789-00",
            LocalDate.of(1990, 1, 1),
            "SP",
            "01000-000",
            "Descrição",
            ["Java"]
        )

        when: "tentar inserir o candidato"
        dao.inserir(candidato)

        then: "deve lançar uma exceção"
        thrown(Exception)
    }
}

