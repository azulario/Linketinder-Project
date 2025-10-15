package com.linketinder.dao

import com.linketinder.database.DatabaseConnection
import com.linketinder.model.Empresa

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.time.LocalDateTime

/**
 * EmpresaDAO - Data Access Object para a entidade Empresa
 *
 * Responsável por todas as operações de banco de dados relacionadas a empresas:
 * - CREATE (inserir)
 * - READ (listar, buscarPorId)
 * - UPDATE (atualizar)
 * - DELETE (deletar)
 */
class EmpresaDAO {

    private static final String SQL_INSERIR = """
        INSERT INTO empresas (nome_empresa, email, cnpj, endereco_id, descricao, senha, criado_em)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """
    private static final String SQL_LISTAR = "SELECT * FROM empresas ORDER BY idEmpresas"
    private static final String SQL_BUSCAR_POR_ID = "SELECT * FROM empresas WHERE idEmpresas = ?"
    private static final String SQL_ATUALIZAR = """
        UPDATE empresas 
        SET nome_empresa = ?, email = ?, cnpj = ?, endereco_id = ?, descricao = ?, senha = ?
        WHERE idEmpresas = ?
    """
    private static final String SQL_DELETAR = "DELETE FROM empresas WHERE idEmpresas = ?"

    private final EnderecoDAO enderecoDAO = new EnderecoDAO()

    /**
     * Insere uma nova empresa no banco de dados
     * @param empresa - objeto Empresa a ser inserido
     */
    void inserir (Empresa empresa) {
        Connection conn = null
        PreparedStatement statement = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()

            Integer enderecoId = null
            if (empresa.endereco) {
                enderecoId = enderecoDAO.buscarOuCriar(empresa.endereco)
                empresa.enderecoId = enderecoId
            }

            statement = conn.prepareStatement(SQL_INSERIR, Statement.RETURN_GENERATED_KEYS)

            statement.setString(1, empresa.nome)
            statement.setString(2, empresa.email)
            statement.setString(3, empresa.cnpj)
            statement.setObject(4, enderecoId)
            statement.setString(5, empresa.descricao)
            statement.setString(6, empresa.senha ?: "senha123") // senha padrão se não informada
            statement.setObject(7, LocalDateTime.now())

            statement.executeUpdate()

            resultSet = statement.getGeneratedKeys()
            if (resultSet.next()) {
                empresa.id = resultSet.getInt(1)
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inserir empresa: ${e.message}", e)
        } finally {
            DatabaseConnection.closeResources(conn, statement, resultSet)
        }
    }

    /**
     * Lista todas as empresas cadastradas no banco
     * @return List<Empresa> - lista com todas as empresas
     */
    List<Empresa> listar() {
        List<Empresa> empresas = []
        Connection conn = null
        Statement statement = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.createStatement()
            resultSet = statement.executeQuery(SQL_LISTAR)

            while (resultSet.next()) {
                Empresa empresa = mapearEmpresa(resultSet)

                if (empresa.enderecoId) {
                    empresa.endereco = enderecoDAO.buscarPorId(empresa.enderecoId)
                }

                empresas << empresa
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar empresas: ${e.message}", e)
        } finally {
            DatabaseConnection.closeResources(conn, statement, resultSet)
        }

        return empresas
    }

    /**
     * Busca uma empresa específica pelo ID
     * @param id - ID da empresa
     * @return Empresa - objeto encontrado ou null
     */
    Empresa buscarPorId(Integer id) {
        Empresa empresa = null
        Connection conn = null
        PreparedStatement statement = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(SQL_BUSCAR_POR_ID)
            statement.setInt(1, id)
            resultSet = statement.executeQuery()

            if (resultSet.next()) {
                empresa = mapearEmpresa(resultSet)

                if (empresa.enderecoId) {
                    empresa.endereco = enderecoDAO.buscarPorId(empresa.enderecoId)
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar empresa por ID: ${e.message}", e)
        } finally {
            DatabaseConnection.closeResources(conn, statement, resultSet)
        }

        return empresa
    }

    /**
     * Atualiza os dados de uma empresa existente
     * @param empresa - objeto com dados atualizados
     */
    void atualizar(Empresa empresa) {
        Connection conn = null
        PreparedStatement statement = null

        try {
            conn = DatabaseConnection.getConnection()

            Integer enderecoId = empresa.enderecoId
            if (empresa.endereco) {
                enderecoId = enderecoDAO.buscarOuCriar(empresa.endereco)
                empresa.enderecoId = enderecoId
            }

            statement = conn.prepareStatement(SQL_ATUALIZAR)

            statement.setString(1, empresa.nome)
            statement.setString(2, empresa.email)
            statement.setString(3, empresa.cnpj)
            statement.setObject(4, enderecoId)
            statement.setString(5, empresa.descricao)
            statement.setString(6, empresa.senha ?: "senha123") // senha padrão se null
            statement.setInt(7, empresa.id)

            statement.executeUpdate()
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar empresa: ${e.message}", e)
        } finally {
            DatabaseConnection.closeResources(conn, statement, null)
        }
    }

    /**
     * Remove uma empresa do banco de dados
     * @param id - ID da empresa a ser removida
     */
    void deletar(Integer id) {
        Connection conn = null
        PreparedStatement statement = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(SQL_DELETAR)
            statement.setInt(1, id)
            statement.executeUpdate()
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar empresa: ${e.message}", e)
        } finally {
            DatabaseConnection.closeResources(conn, statement, null)
        }
    }

    private Empresa mapearEmpresa(ResultSet rs) {
        Empresa empresa = new Empresa(
            rs.getString("nome_empresa"),
            rs.getString("email"),
            rs.getString("cnpj"),
            rs.getString("descricao")
        )
        empresa.id = rs.getInt("idEmpresas")
        empresa.enderecoId = rs.getObject("endereco_id") as Integer
        empresa.senha = rs.getString("senha")
        empresa.criadoEm = rs.getTimestamp("criado_em")?.toLocalDateTime()
        return empresa
    }
}
