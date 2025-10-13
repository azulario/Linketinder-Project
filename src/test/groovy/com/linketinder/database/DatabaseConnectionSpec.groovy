package com.linketinder.database

import spock.lang.Specification
import java.sql.Connection

/**
 * DatabaseConnectionSpec - Testes unitários para a classe DatabaseConnection
 *
 * Este teste verifica se:
 * 1. A conexão com o banco está funcionando
 * 2. O metodo getConnection() retorna uma conexão válida
 * 3. O metodo closeConnection() fecha a conexão corretamente
 *
 * pro teste teste passar precisa:
 * - PostgreSQL rodando na porta 5432
 * - Banco de dados 'linketinder' criado
 * - Senha correta configurada em DatabaseConnection.groovy
 */
class DatabaseConnectionSpec extends Specification {

    def "deve abrir uma conexão com o banco de dados com sucesso"() {
        given: "que o PostgreSQL está rodando e configurado corretamente"
        Connection conn = null

        when: "tento abrir uma conexão"
        conn = DatabaseConnection.getConnection()

        then: "a conexão não deve ser nula"
        conn != null

        and: "a conexão deve estar ativa (não fechada)"
        !conn.isClosed()

        and: "deve conseguir acessar metadados do banco"
        conn.metaData != null
        conn.metaData.databaseProductName == "PostgreSQL"

        cleanup: "fecha a conexão após o teste"
        if (conn != null) {
            DatabaseConnection.closeConnection(conn)
        }
    }

    def "deve fechar uma conexão aberta corretamente"() {
        given: "uma conexão ativa com o banco"
        Connection conn = DatabaseConnection.getConnection()

        and: "a conexão está aberta"
        assert !conn.isClosed()

        when: "fecho a conexão"
        DatabaseConnection.closeConnection(conn)

        then: "a conexão deve estar fechada"
        conn.isClosed()
    }

    def "deve lidar com tentativa de fechar conexão nula sem erro"() {
        given: "uma conexão nula"
        Connection conn = null

        when: "tento fechar a conexão nula"
        DatabaseConnection.closeConnection(conn)

        then: "não deve lançar exceção"
        noExceptionThrown()
    }

    def "deve conectar múltiplas vezes sem erro"() {
        given: "uma lista para armazenar conexões"
        List<Connection> conexoes = []

        when: "abro 3 conexões diferentes"
        3.times {
            conexoes.add(DatabaseConnection.getConnection())
        }

        then: "todas as conexões devem ser válidas"
        conexoes.size() == 3
        conexoes.every { it != null && !it.isClosed() }

        cleanup: "fecha todas as conexões"
        conexoes.each { conn ->
            if (conn != null) {
                DatabaseConnection.closeConnection(conn)
            }
        }
    }

    def "deve retornar informações corretas do banco de dados"() {
        given: "uma conexão com o banco"
        Connection conn = DatabaseConnection.getConnection()

        when: "busco os metadados"
        def metaData = conn.metaData

        then: "deve conter informações do PostgreSQL"
        metaData.databaseProductName == "PostgreSQL"
        metaData.URL.contains("jdbc:postgresql://localhost:5432/linketinder")
        metaData.driverName.contains("PostgreSQL")

        cleanup:
        DatabaseConnection.closeConnection(conn)
    }
}

