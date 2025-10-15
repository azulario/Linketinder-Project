package com.linketinder.database

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

/**
 * DatabaseConnection - Gerencia conexões com o banco PostgreSQL
 *
 * Esta classe é responsável por:
 * - Abrir conexões com o banco de dados
 * - Fechar conexões de forma segura
 * - Centralizar as configurações de conexão
 *
 */
class DatabaseConnection {

    // Configurações do banco de dados
    private static final String URL = "jdbc:postgresql://localhost:5432/linketinder"
    private static final String USER = "azulario"
    private static final String PASSWORD = "uzumaki"


    /**
     * abre uma nova conexão com o banco de dados
     * @return Connection - objeto de conexão ativo
     * @throws SQLException se não conseguir conectar
     */
    static Connection getConnection() throws SQLException {
        try {
            // carrega o driver JDBC do PostgreSQL
            Class.forName("org.postgresql.Driver")

            // cria e retorna a conexão
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)
            println "✓ Conexão com banco de dados estabelecida!"
            return conn

        } catch (ClassNotFoundException e) {
            println "✗ ERRO: Driver PostgreSQL não encontrado!"
            println "  Verifique se a dependência está no build.gradle"
            throw new SQLException("Driver PostgreSQL não encontrado", e)
            // e = exceção original que causou o problema ou erro
        } catch (SQLException e) {
            println "✗ ERRO ao conectar ao banco de dados!"
            println "  Verifique se:"
            println "  1. PostgreSQL está rodando"
            println "  2. Banco 'linketinder' existe"
            println "  3. Usuário e senha estão corretos"
            println "  4. Porta 5432 está disponível"
            throw e
        }
    }

    /**
     * Fecha uma conexão com o banco de dados
     * @param conn - conexão a ser fechada
     */
    static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close()
                println "✓ Conexão com banco de dados fechada!"
            } catch (SQLException e) {
                println "✗ ERRO ao fechar conexão: ${e.message}"
            }
        }
    }

    /**
     * Fecha recursos JDBC de forma segura (Connection, Statement, ResultSet)
     * @param conn - conexão a ser fechada
     * @param stmt - statement a ser fechado
     * @param rs - result set a ser fechado
     */
    static void closeResources(Connection conn, java.sql.Statement stmt, java.sql.ResultSet rs) {
        if (rs != null) {
            try {
                rs.close()
            } catch (SQLException e) {
                println "✗ ERRO ao fechar ResultSet: ${e.message}"
            }
        }
        if (stmt != null) {
            try {
                stmt.close()
            } catch (SQLException e) {
                println "✗ ERRO ao fechar Statement: ${e.message}"
            }
        }
        if (conn != null) {
            try {
                conn.close()
                println "✓ Recursos do banco fechados!"
            } catch (SQLException e) {
                println "✗ ERRO ao fechar conexão: ${e.message}"
            }
        }
    }
}
