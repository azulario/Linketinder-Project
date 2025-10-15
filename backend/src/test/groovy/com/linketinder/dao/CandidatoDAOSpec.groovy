package com.linketinder.dao

import com.linketinder.database.DatabaseConnection
import com.linketinder.model.Candidato
import com.linketinder.model.Endereco
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
            "João",
            "Silva",
            "joao@email.com",
            "123.456.789-00",
            LocalDate.of(1990, 5, 15),
            "Desenvolvedor Java",
            ["Java", "Spring", "SQL"]
        )
        candidato.endereco = new Endereco("Brasil", "SP", "São Paulo", "01234-567")

        when: "inserir o candidato no banco"
        dao.inserir(candidato)

        then: "o candidato deve ter um ID gerado"
        candidato.id != null
        candidato.id > 0
    }

    def "deve listar todos os candidatos cadastrados"() {
        given: "dois candidatos cadastrados no banco"
        def candidato1 = new Candidato(
            "Maria",
            "Santos",
            "maria@email.com",
            "111.222.333-44",
            LocalDate.of(1995, 8, 20),
            "Desenvolvedora Python",
            ["Python", "Django"]
        )
        candidato1.endereco = new Endereco("Brasil", "RJ", "Rio de Janeiro", "20000-000")

        def candidato2 = new Candidato(
            "Pedro",
            "Costa",
            "pedro@email.com",
            "555.666.777-88",
            LocalDate.of(1988, 3, 10),
            "Desenvolvedor Frontend",
            ["JavaScript", "React"]
        )
        candidato2.endereco = new Endereco("Brasil", "MG", "Belo Horizonte", "30000-000")

        dao.inserir(candidato1)
        dao.inserir(candidato2)

        when: "listar todos os candidatos"
        def candidatos = dao.listar()

        then: "deve retornar os 2 candidatos cadastrados"
        candidatos.size() == 2
        candidatos*.nome.containsAll(["Maria", "Pedro"])
    }

    def "deve buscar um candidato por ID"() {
        given: "um candidato cadastrado no banco"
        def candidato = new Candidato(
            "Ana",
            "Oliveira",
            "ana@email.com",
            "999.888.777-66",
            LocalDate.of(1992, 11, 25),
            "Designer UX",
            ["Figma", "Adobe XD"]
        )
        candidato.endereco = new Endereco("Brasil", "PR", "Curitiba", "80000-000")
        dao.inserir(candidato)

        when: "buscar o candidato pelo ID"
        def candidatoEncontrado = dao.buscarPorId(candidato.id)

        then: "deve retornar o candidato correto"
        candidatoEncontrado != null
        candidatoEncontrado.id == candidato.id
        candidatoEncontrado.nome == "Ana"
        candidatoEncontrado.email == "ana@email.com"
    }

    def "deve retornar null ao buscar candidato inexistente"() {
        when: "buscar um candidato com ID inexistente"
        def candidato = dao.buscarPorId(99999)

        then: "deve retornar null"
        candidato == null
    }

    def "deve atualizar os dados de um candidato"() {
        given: "um candidato cadastrado no banco"
        def candidato = new Candidato(
            "Carlos",
            "Ferreira",
            "carlos@email.com",
            "777.888.999-00",
            LocalDate.of(1990, 6, 15),
            "Desenvolvedor Backend",
            ["Java", "Spring"]
        )
        candidato.endereco = new Endereco("Brasil", "RS", "Porto Alegre", "90000-000")
        dao.inserir(candidato)

        when: "atualizar os dados do candidato"
        candidato.nome = "Carlos Augusto"
        candidato.descricao = "Arquiteto de Software"
        candidato.competencias = ["Java", "Spring", "Docker", "Kubernetes"]
        candidato.endereco = new Endereco("Brasil", "SC", "Florianópolis", "88000-000")
        dao.atualizar(candidato)

        and: "buscar o candidato atualizado"
        def candidatoAtualizado = dao.buscarPorId(candidato.id)

        then: "os dados devem estar atualizados"
        candidatoAtualizado.nome == "Carlos Augusto"
        candidatoAtualizado.descricao == "Arquiteto de Software"
        candidatoAtualizado.competencias.size() == 4
    }

    def "deve deletar um candidato do banco"() {
        given: "um candidato cadastrado no banco"
        def candidato = new Candidato(
            "Delete",
            "Test",
            "delete@test.com",
            "111.111.111-11",
            LocalDate.of(1990, 1, 1),
            "Test",
            []
        )
        candidato.endereco = new Endereco("Brasil", "SP", "São Paulo", "01000-000")
        dao.inserir(candidato)
        def candidatoId = candidato.id

        when: "deletar o candidato"
        dao.deletar(candidatoId)

        and: "tentar buscar o candidato deletado"
        def candidatoDeletado = dao.buscarPorId(candidatoId)

        then: "não deve encontrar o candidato"
        candidatoDeletado == null
    }

    def "deve listar candidatos com suas competências"() {
        given: "um candidato com competências"
        def candidato = new Candidato(
            "Tech",
            "Master",
            "tech@master.com",
            "123.123.123-12",
            LocalDate.of(1985, 5, 5),
            "Full Stack Developer",
            ["Java", "Python", "JavaScript", "Docker"]
        )
        candidato.endereco = new Endereco("Brasil", "SP", "São Paulo", "01000-000")
        dao.inserir(candidato)

        when: "buscar o candidato"
        def candidatoComCompetencias = dao.buscarPorId(candidato.id)

        then: "deve ter as competências associadas"
        candidatoComCompetencias.competencias.size() == 4
        candidatoComCompetencias.competencias.containsAll(["Java", "Python", "JavaScript", "Docker"])
    }

    def "não deve inserir candidato com dados inválidos"() {
        given: "um candidato sem email"
        def candidato = new Candidato(
            "Invalid",
            "User",
            null,
            "000.000.000-00",
            LocalDate.of(1990, 1, 1),
            "Test",
            []
        )
        candidato.endereco = new Endereco("Brasil", "SP", "São Paulo", "01000-000")

        when: "tentar inserir o candidato"
        dao.inserir(candidato)

        then: "deve lançar exceção"
        thrown(RuntimeException)
    }
}
