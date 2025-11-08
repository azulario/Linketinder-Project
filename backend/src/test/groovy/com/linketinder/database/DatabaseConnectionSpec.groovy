package com.linketinder.database

import spock.lang.Specification
import java.sql.Connection
import java.sql.Statement
import java.sql.ResultSet


class DatabaseConnectionSpec extends Specification {

    Connection connection

    def cleanup() {
        // Fechar conexão após cada teste
        if (connection != null && !connection.isClosed()) {
            DatabaseConnection.closeConnection(connection)
        }
    }

    def "deve estabelecer conexão com banco de dados"() {
        when: "solicitar conexão"
        connection = DatabaseConnection.getConnection()

        then: "deve retornar conexão válida"
        connection != null
        !connection.isClosed()
    }

    def "deve carregar driver PostgreSQL corretamente"() {
        when: "tentar criar conexão"
        connection = DatabaseConnection.getConnection()

        then: "driver deve estar carregado"
        Class.forName("org.postgresql.Driver") != null
        connection != null
    }

    def "deve conectar ao banco linketinder"() {
        when: "estabelecer conexão"
        connection = DatabaseConnection.getConnection()

        then: "deve estar conectada ao banco correto"
        connection.catalog == "linketinder"
    }

    def "deve fechar conexão corretamente"() {
        given: "conexão estabelecida"
        connection = DatabaseConnection.getConnection()

        when: "fechar conexão"
        DatabaseConnection.closeConnection(connection)

        then: "conexão deve estar fechada"
        connection.isClosed()
    }

    def "deve lidar com fechamento de conexão nula"() {
        when: "tentar fechar conexão nula"
        DatabaseConnection.closeConnection(null)

        then: "não deve lançar exceção"
        notThrown(Exception)
    }

    def "deve fechar recursos corretamente"() {
        given: "conexão com statement e resultset"
        connection = DatabaseConnection.getConnection()
        Statement stmt = connection.createStatement()
        ResultSet rs = stmt.executeQuery("SELECT 1")

        when: "fechar recursos"
        DatabaseConnection.closeResources(connection, stmt, rs)

        then: "todos devem estar fechados"
        rs.isClosed()
        stmt.isClosed()
        connection.isClosed()
    }

    def "deve fechar recursos mesmo com alguns nulos"() {
        given: "apenas conexão estabelecida"
        connection = DatabaseConnection.getConnection()

        when: "fechar recursos com statement e resultset nulos"
        DatabaseConnection.closeResources(connection, null, null)

        then: "não deve lançar exceção e conexão deve estar fechada"
        notThrown(Exception)
        connection.isClosed()
    }

    def "deve lidar com todos os recursos nulos"() {
        when: "tentar fechar recursos nulos"
        DatabaseConnection.closeResources(null, null, null)

        then: "não deve lançar exceção"
        notThrown(Exception)
    }

    def "deve permitir criar múltiplas conexões"() {
        when: "criar duas conexões"
        def conn1 = DatabaseConnection.getConnection()
        def conn2 = DatabaseConnection.getConnection()

        then: "ambas devem ser válidas e independentes"
        conn1 != null
        conn2 != null
        !conn1.isClosed()
        !conn2.isClosed()
        conn1 !== conn2

        cleanup:
        DatabaseConnection.closeConnection(conn1)
        DatabaseConnection.closeConnection(conn2)
    }

    def "deve executar query simples com sucesso"() {
        given: "conexão estabelecida"
        connection = DatabaseConnection.getConnection()
        Statement stmt = connection.createStatement()

        when: "executar query simples"
        ResultSet rs = stmt.executeQuery("SELECT 1 as numero")

        then: "deve retornar resultado"
        rs.next()
        rs.getInt("numero") == 1

        cleanup:
        DatabaseConnection.closeResources(connection, stmt, rs)
    }

    def "deve fechar statement mesmo se já fechado"() {
        given: "statement já fechado"
        connection = DatabaseConnection.getConnection()
        Statement stmt = connection.createStatement()
        stmt.close()

        when: "tentar fechar recursos"
        DatabaseConnection.closeResources(connection, stmt, null)

        then: "não deve lançar exceção"
        notThrown(Exception)
        connection.isClosed()
    }

    def "deve fechar resultset mesmo se já fechado"() {
        given: "resultset já fechado"
        connection = DatabaseConnection.getConnection()
        Statement stmt = connection.createStatement()
        ResultSet rs = stmt.executeQuery("SELECT 1")
        rs.close()

        when: "tentar fechar recursos"
        DatabaseConnection.closeResources(connection, stmt, rs)

        then: "não deve lançar exceção"
        notThrown(Exception)
        stmt.isClosed()
        connection.isClosed()
    }

    def "deve manter auto-commit habilitado por padrão"() {
        when: "criar conexão"
        connection = DatabaseConnection.getConnection()

        then: "auto-commit deve estar habilitado"
        connection.autoCommit
    }

    def "deve permitir desabilitar auto-commit"() {
        given: "conexão estabelecida"
        connection = DatabaseConnection.getConnection()

        when: "desabilitar auto-commit"
        connection.autoCommit = false

        then: "auto-commit deve estar desabilitado"
        !connection.autoCommit
    }
}

