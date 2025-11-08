package com.linketinder.database

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


class DatabaseConnection {

    // Configurações do banco de dados
    private static final String URL = "jdbc:postgresql://localhost:5432/linketinder"
    private static final String USER = "azulario"
    private static final String PASSWORD = "uzumaki"


    static Connection getConnection() throws SQLException {
        try {

            Class.forName("org.postgresql.Driver")


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
