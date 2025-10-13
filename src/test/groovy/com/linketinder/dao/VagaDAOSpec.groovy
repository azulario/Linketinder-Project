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
