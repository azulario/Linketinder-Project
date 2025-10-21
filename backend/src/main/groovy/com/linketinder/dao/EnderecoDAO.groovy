package com.linketinder.dao

import com.linketinder.database.DatabaseConnection
import com.linketinder.model.Endereco

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.time.LocalDateTime

class EnderecoDAO {

    private static final String SQL_INSERIR = """
        INSERT INTO enderecos (cep, estado, cidade, pais, criado_em)
        VALUES (?, ?, ?, ?, ?)
    """
    private static final String SQL_BUSCAR_POR_ID = "SELECT * FROM enderecos WHERE idEnderecos = ?"
    private static final String SQL_BUSCAR_POR_DADOS = """
        SELECT * FROM enderecos 
        WHERE pais = ? AND (estado = ? OR estado IS NULL) 
        AND (cidade = ? OR cidade IS NULL) 
        AND (cep = ? OR cep IS NULL)
        LIMIT 1
    """
    private static final String SQL_ATUALIZAR = """
        UPDATE enderecos 
        SET cep = ?, estado = ?, cidade = ?, pais = ?
        WHERE idEnderecos = ?
    """
    private static final String SQL_DELETAR = "DELETE FROM enderecos WHERE idEnderecos = ?"

    Integer inserir(Endereco endereco) {
        Connection conn = null
        PreparedStatement statement = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(SQL_INSERIR, Statement.RETURN_GENERATED_KEYS)

            statement.setString(1, endereco.cep)
            statement.setString(2, endereco.estado)
            statement.setString(3, endereco.cidade)
            statement.setString(4, endereco.pais)
            statement.setObject(5, LocalDateTime.now())

            statement.executeUpdate()

            resultSet = statement.getGeneratedKeys()
            if (resultSet.next()) {
                endereco.id = resultSet.getInt(1)
                return endereco.id
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inserir endereço: ${e.message}", e)
        } finally {
            DatabaseConnection.closeResources(conn, statement, resultSet)
        }

        return null
    }

    Endereco buscarPorId(Integer id) {
        if (!id) return null

        Endereco endereco = null
        Connection conn = null
        PreparedStatement statement = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(SQL_BUSCAR_POR_ID)
            statement.setInt(1, id)
            resultSet = statement.executeQuery()

            if (resultSet.next()) {
                endereco = mapearEndereco(resultSet)
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar endereço por ID: ${e.message}", e)
        } finally {
            DatabaseConnection.closeResources(conn, statement, resultSet)
        }

        return endereco
    }

    Integer buscarOuCriar(Endereco endereco) {
        Integer enderecoId = buscarPorDados(endereco)
        if (enderecoId) {
            return enderecoId
        }
        return inserir(endereco)
    }

    private Integer buscarPorDados(Endereco endereco) {
        Connection conn = null
        PreparedStatement statement = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(SQL_BUSCAR_POR_DADOS)
            statement.setString(1, endereco.pais)
            statement.setString(2, endereco.estado)
            statement.setString(3, endereco.cidade)
            statement.setString(4, endereco.cep)
            resultSet = statement.executeQuery()

            if (resultSet.next()) {
                return resultSet.getInt("idEnderecos")
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar endereço por dados: ${e.message}", e)
        } finally {
            DatabaseConnection.closeResources(conn, statement, resultSet)
        }

        return null
    }

    void atualizar(Endereco endereco) {
        Connection conn = null
        PreparedStatement statement = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(SQL_ATUALIZAR)

            statement.setString(1, endereco.cep)
            statement.setString(2, endereco.estado)
            statement.setString(3, endereco.cidade)
            statement.setString(4, endereco.pais)
            statement.setInt(5, endereco.id)

            statement.executeUpdate()
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar endereço: ${e.message}", e)
        } finally {
            DatabaseConnection.closeResources(conn, statement, null)
        }
    }

    void deletar(Integer id) {
        Connection conn = null
        PreparedStatement statement = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(SQL_DELETAR)
            statement.setInt(1, id)
            statement.executeUpdate()
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar endereço: ${e.message}", e)
        } finally {
            DatabaseConnection.closeResources(conn, statement, null)
        }
    }

    private Endereco mapearEndereco(ResultSet rs) {
        Endereco endereco = new Endereco(
            rs.getString("pais"),
            rs.getString("estado"),
            rs.getString("cidade"),
            rs.getString("cep")
        )
        endereco.id = rs.getInt("idEnderecos")
        endereco.criadoEm = rs.getTimestamp("criado_em")?.toLocalDateTime()
        return endereco
    }
}

