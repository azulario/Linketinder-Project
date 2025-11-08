package com.linketinder.database

import spock.lang.Specification
import java.sql.Connection


class PostgreSQLConnectionSpec extends Specification {

    PostgreSQLConnection connection

    def cleanup() {

        if (connection != null) {
            connection.closeConnection()
        }

        PostgreSQLConnection.resetarInstancia()
    }

    def "deve criar instância única do singleton"() {
        when: "buscar instância duas vezes"
        def instancia1 = PostgreSQLConnection.getInstancia()
        def instancia2 = PostgreSQLConnection.getInstancia()

        then: "deve retornar a mesma instância"
        instancia1 !== null
        instancia1 === instancia2
    }

    def "deve implementar IConnectionProvider"() {
        when: "buscar instância"
        def instancia = PostgreSQLConnection.getInstancia()

        then: "deve implementar a interface"
        instancia instanceof IConnectionProvider
    }

    def "deve criar conexão quando solicitada"() {
        given: "instância do connection provider"
        connection = PostgreSQLConnection.getInstancia()

        when: "solicitar conexão"
        Connection conn = connection.getConnection()

        then: "deve retornar conexão válida"
        conn != null
        !conn.isClosed()
    }

    def "deve reutilizar conexão existente"() {
        given: "instância com conexão estabelecida"
        connection = PostgreSQLConnection.getInstancia()
        Connection conn1 = connection.getConnection()

        when: "solicitar conexão novamente"
        Connection conn2 = connection.getConnection()

        then: "deve retornar mesma conexão"
        conn1 === conn2
    }

    def "deve fechar conexão corretamente"() {
        given: "conexão estabelecida"
        connection = PostgreSQLConnection.getInstancia()
        Connection conn = connection.getConnection()

        when: "fechar conexão"
        connection.closeConnection()

        then: "conexão deve estar fechada"
        conn.isClosed()
    }

    def "deve recriar conexão após fechamento"() {
        given: "conexão que foi fechada"
        connection = PostgreSQLConnection.getInstancia()
        Connection conn1 = connection.getConnection()
        connection.closeConnection()

        when: "solicitar nova conexão"
        Connection conn2 = connection.getConnection()

        then: "deve criar nova conexão"
        conn2 != null
        !conn2.isClosed()
        conn1 !== conn2
    }

    def "deve resetar instância singleton"() {
        given: "instância criada"
        def instancia1 = PostgreSQLConnection.getInstancia()

        when: "resetar singleton"
        PostgreSQLConnection.resetarInstancia()
        def instancia2 = PostgreSQLConnection.getInstancia()

        then: "deve criar nova instância"
        instancia1 !== instancia2
    }

    def "deve ser thread-safe ao criar singleton"() {
        given: "múltiplas threads buscando instância"
        def instancias = []
        def threads = []

        when: "criar 10 threads simultâneas"
        10.times {
            threads << Thread.start {
                instancias << PostgreSQLConnection.getInstancia()
            }
        }
        threads*.join()

        then: "todas devem ter a mesma instância"
        instancias.size() == 10
        instancias.every { it === instancias[0] }
    }

    def "deve lidar com múltiplos fechamentos"() {
        given: "conexão estabelecida"
        connection = PostgreSQLConnection.getInstancia()
        connection.getConnection()

        when: "fechar múltiplas vezes"
        connection.closeConnection()
        connection.closeConnection()

        then: "não deve lançar exceção"
        notThrown(Exception)
    }

    def "deve verificar se conexão está fechada antes de usar"() {
        given: "conexão que será fechada externamente"
        connection = PostgreSQLConnection.getInstancia()
        Connection conn1 = connection.getConnection()
        conn1.close() // Fechar diretamente

        when: "solicitar conexão novamente"
        Connection conn2 = connection.getConnection()

        then: "deve criar nova conexão"
        conn2 != null
        !conn2.isClosed()
        conn1 !== conn2
    }
}

