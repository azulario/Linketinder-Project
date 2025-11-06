package main.groovy.com.linketinder.database

import groovy.transform.CompileStatic

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

@CompileStatic
class PostgreSQLConnection implements IConnectionProvider {

    private static volatile PostgreSQLConnection instancia

    private Connection connection

    private static final String URL = "jdbc:postgresql://localhost:5432/linketinder"
    private static final String USER = "azulario"
    private static final String PASSWORD = "uzumaki"

    private PostgreSQLConnection() {

    }

    static PostgreSQLConnection getInstancia() {
        if (instancia == null) {
            synchronized (PostgreSQLConnection.class) {
                if (instancia == null) {
                    instancia = new PostgreSQLConnection()
                    println "[SINGLETON] Instância única de PostgreSQLConnection criada."

                }
            }
        }
        return instancia
    }

    @Override
    Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                criarConexao()
            }
            return connection
        } catch (SQLException erro) {
            println "Erro ao obter conexão: ${erro.message}"
            throw erro
        }
    }

    private void criarConexao() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver")

            connection = DriverManager.getConnection(URL, USER, PASSWORD)

            println "Conexão com o banco de dados PostgreSQL estabelecida com sucesso."
            println "URL: ${URL}"

        } catch (ClassNotFoundException erro) {
            throw new SQLException(
                "Driver do PostgreSQL não encontrado." +
                "Adicione dependência no build.gradle", erro

            )
        } catch (SQLException erro) {
            println "Erro ao conectar PostgreSQL!"
            println "Verifique:"
            println "1. PostgreSQL está rodando?"
            println "2. Bando 'linketinder' existe?"
            println "3. Usuário e senha estão corretos?"
            println "Posta 5432 disponível?"
            throw erro
        }
    }

    @Override
    void closeConnection() {
        if (connection != null) {
            try {
                connection.close()
                connection = null
                println "Conexão PostgreSQL fechada com sucesso."
            } catch (SQLException erro) {
                println "Erro ao fechar conexão: ${erro.message}"

            }
        }
    }

    static void resetarInstancia() {
        if (instancia != null) {
            instancia.closeConnection()
            instancia = null
            println "[TESTE] Singleton resetado."
        }
    }
}
