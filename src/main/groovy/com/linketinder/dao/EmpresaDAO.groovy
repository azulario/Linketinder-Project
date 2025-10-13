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

    /**
     * Insere uma nova empresa no banco de dados
     * @param empresa - objeto Empresa a ser inserido
     */
    void inserir (Empresa empresa) {
        String sql = """
            INSERT INTO empresas (nome_empresa, email, cnpj, pais, cep, descricao, senha, criado_em)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """

        Connection conn = null
        PreparedStatement statement = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

            statement.setString(1, empresa.nome)
            statement.setString(2, empresa.email)
            statement.setString(3, empresa.cnpj)
            statement.setString(4, empresa.pais)
            statement.setString(5, empresa.cep)
            statement.setString(6, empresa.descricao)
            statement.setString(7, empresa.senha ?: "senha123") // senha padrão se não informada
            statement.setObject(8, LocalDateTime.now())

            int rowsAffected = statement.executeUpdate()
            println "DEBUG: Rows affected: ${rowsAffected}"

            resultSet = statement.getGeneratedKeys()
            if (resultSet.next()) {
                empresa.id = resultSet.getInt(1)
                println "DEBUG: ID gerado: ${empresa.id}"
            }
        } catch (Exception e) {
            println "ERRO ao inserir empresa: ${e.message}"
            e.printStackTrace()
            throw e
        } finally {
            DatabaseConnection.closeResources(conn, statement, resultSet)
        }
    }

    /**
     * Lista todas as empresas cadastradas no banco
     * @return List<Empresa> - lista com todas as empresas
     */
    List<Empresa> listar() {
        String sql = "SELECT * FROM empresas ORDER BY idEmpresas"
        List<Empresa> empresas = []

        Connection conn = null
        Statement statement = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.createStatement()
            resultSet = statement.executeQuery(sql)

            while (resultSet.next()) {
                empresas.add(mapearEmpresa(resultSet))
            }
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
        String sql = "SELECT * FROM empresas WHERE idEmpresas = ?"
        Empresa empresa = null

        Connection conn = null
        PreparedStatement statement = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(sql)
            statement.setInt(1, id)
            resultSet = statement.executeQuery()

            if (resultSet.next()) {
                empresa = mapearEmpresa(resultSet)
            }
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
        String sql = """
            UPDATE empresas 
            SET nome_empresa = ?, email = ?, cnpj = ?, pais = ?, cep = ?, descricao = ?, senha = ?
            WHERE idEmpresas = ?
        """

        Connection conn = null
        PreparedStatement statement = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(sql)

            statement.setString(1, empresa.nome)
            statement.setString(2, empresa.email)
            statement.setString(3, empresa.cnpj)
            statement.setString(4, empresa.pais)
            statement.setString(5, empresa.cep)
            statement.setString(6, empresa.descricao)
            statement.setString(7, empresa.senha ?: "senha123") // senha padrão se null
            statement.setInt(8, empresa.id)

            statement.executeUpdate()
        } finally {
            DatabaseConnection.closeResources(conn, statement, null)
        }
    }

    /**
     * Remove uma empresa do banco de dados
     * @param id - ID da empresa a ser removida
     */
    void deletar(Integer id) {
        String sql = "DELETE FROM empresas WHERE idEmpresas = ?"

        Connection conn = null
        PreparedStatement statement = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(sql)
            statement.setInt(1, id)
            statement.executeUpdate()
        } finally {
            DatabaseConnection.closeResources(conn, statement, null)
        }
    }

    private Empresa mapearEmpresa(ResultSet rs) {
        Integer empresaId = rs.getInt("idEmpresas")

        // Buscar competências da empresa (se necessário implementar no futuro)
        List<String> competencias = [] // Por enquanto retorna vazio, pois a tabela empresas não tem relação N:N com competências

        Empresa empresa = new Empresa(
            rs.getString("nome_empresa"),
            rs.getString("email"),
            rs.getString("cnpj"),
            rs.getString("pais"),
            rs.getString("pais"), // estado - usando pais já que estado não existe na tabela
            rs.getString("cep"),
            rs.getString("descricao"),
            competencias
        )
        empresa.id = empresaId
        empresa.senha = rs.getString("senha")
        empresa.criadoEm = rs.getTimestamp("criado_em")?.toLocalDateTime()
        return empresa
    }
}
