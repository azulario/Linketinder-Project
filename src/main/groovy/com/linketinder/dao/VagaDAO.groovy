package com.linketinder.dao

import com.linketinder.database.DatabaseConnection
import com.linketinder.model.Vaga
import com.linketinder.model.Empresa

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.time.LocalDateTime

/**
 * VagaDAO - Data Access Object para a entidade Vaga
 *
 * Responsável por todas as operações de banco de dados relacionadas a vagas:
 * - CREATE (inserir)
 * - READ (listar, listarPorEmpresa, buscarPorId)
 * - UPDATE (atualizar)
 * - DELETE (deletar)
 */
class VagaDAO {

    /**
     * Insere uma nova vaga no banco de dados
     * @param vaga - objeto Vaga a ser inserido
     */
    void inserir(Vaga vaga) {
        String sql = """
            INSERT INTO vagas (nome_vaga, descricao, empresa_id, estado, cidade, criado_em)
            VALUES (?, ?, ?, ?, ?, ?)
        """

        Connection conn = null
        PreparedStatement statement = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

            statement.setString(1, vaga.titulo)
            statement.setString(2, vaga.descricao)
            statement.setInt(3, vaga.empresaId)
            statement.setString(4, vaga.empresa?.pais) // estado no banco, mas usamos pais da empresa
            statement.setString(5, vaga.cidade)
            statement.setObject(6, LocalDateTime.now())

            int rowsAffected = statement.executeUpdate()
            println "DEBUG: Rows affected: ${rowsAffected}"

            resultSet = statement.getGeneratedKeys()
            if (resultSet.next()) {
                vaga.id = resultSet.getInt(1)
                println "DEBUG: ID gerado: ${vaga.id}"
            }

            // Inserir competências na tabela N:N competencias_vagas
            if (vaga.competencias) {
                for (competencia in vaga.competencias) {
                    Integer competenciaId = buscarOuCriarCompetencia(competencia, conn)
                    String insertCompetenciaSql = """
                        INSERT INTO competencias_vagas (competencia_id, vaga_id)
                        VALUES (?, ?)
                    """
                    PreparedStatement compStmt = conn.prepareStatement(insertCompetenciaSql)
                    compStmt.setInt(1, competenciaId)
                    compStmt.setInt(2, vaga.id)
                    compStmt.executeUpdate()
                    compStmt.close()
                }
            }

        } catch (Exception e) {
            println "ERRO ao inserir vaga: ${e.message}"
            e.printStackTrace()
            throw e
        } finally {
            DatabaseConnection.closeResources(conn, statement, resultSet)
        }
    }

    /**
     * Lista todas as vagas cadastradas no banco
     * @return List<Vaga> - lista com todas as vagas
     */
    List<Vaga> listar() {
        String sql = "SELECT * FROM vagas ORDER BY idVagas"
        List<Vaga> vagas = []

        Connection conn = null
        Statement statement = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.createStatement()
            resultSet = statement.executeQuery(sql)

            while (resultSet.next()) {
                Vaga vaga = mapearVaga(resultSet, conn)
                // Buscar competências da vaga
                vaga.competencias = buscarCompetencias(vaga.id, conn)
                vagas << vaga
            }
        } catch (Exception e) {
            println "ERRO ao listar vagas: ${e.message}"
            e.printStackTrace()
        } finally {
            DatabaseConnection.closeResources(conn, statement, resultSet)
        }
        return vagas
    }

    /**
     * Lista todas as vagas de uma empresa específica
     * @param empresaId - ID da empresa
     * @return List<Vaga> - lista de vagas da empresa
     */
    List<Vaga> listarPorEmpresa(Integer empresaId) {
        String sql = "SELECT * FROM vagas WHERE empresa_id = ? ORDER BY idVagas"
        List<Vaga> vagas = []

        Connection conn = null
        PreparedStatement statement = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(sql)
            statement.setInt(1, empresaId)
            resultSet = statement.executeQuery()

            while (resultSet.next()) {
                Vaga vaga = mapearVaga(resultSet, conn)
                // Buscar competências da vaga
                vaga.competencias = buscarCompetencias(vaga.id, conn)
                vagas << vaga
            }
        } catch (Exception e) {
            println "ERRO ao listar vagas por empresa: ${e.message}"
            e.printStackTrace()
        } finally {
            DatabaseConnection.closeResources(conn, statement, resultSet)
        }
        return vagas
    }

    /**
     * Busca uma vaga específica pelo ID
     * @param id - ID da vaga
     * @return Vaga - objeto encontrado ou null
     */
    Vaga buscarPorId(Integer id) {
        String sql = "SELECT * FROM vagas WHERE idVagas = ?"

        Vaga vaga = null
        Connection conn = null
        PreparedStatement statement = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(sql)
            statement.setInt(1, id)
            resultSet = statement.executeQuery()

            if (resultSet.next()) {
                vaga = mapearVaga(resultSet, conn)
                // Buscar competências da vaga
                vaga.competencias = buscarCompetencias(vaga.id, conn)
            }
        } catch (Exception e) {
            println "ERRO ao buscar vaga por ID: ${e.message}"
            e.printStackTrace()
        } finally {
            DatabaseConnection.closeResources(conn, statement, resultSet)
        }
        return vaga
    }

    /**
     * Atualiza os dados de uma vaga existente
     * @param vaga - objeto com dados atualizados
     */
    void atualizar(Vaga vaga) {
        String sql = """
            UPDATE vagas 
            SET nome_vaga = ?, descricao = ?, empresa_id = ?, estado = ?, cidade = ?
            WHERE idVagas = ?
        """

        Connection conn = null
        PreparedStatement statement = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(sql)

            statement.setString(1, vaga.titulo)
            statement.setString(2, vaga.descricao)
            statement.setInt(3, vaga.empresaId)
            statement.setString(4, vaga.empresa?.pais)
            statement.setString(5, vaga.cidade)
            statement.setInt(6, vaga.id)

            int rowsAffected = statement.executeUpdate()
            println "DEBUG: Rows affected on update: ${rowsAffected}"

            // Atualizar competências
            String deleteCompetenciasSql = "DELETE FROM competencias_vagas WHERE vaga_id = ?"
            PreparedStatement deleteStmt = conn.prepareStatement(deleteCompetenciasSql)
            deleteStmt.setInt(1, vaga.id)
            deleteStmt.executeUpdate()
            deleteStmt.close()

            if (vaga.competencias) {
                for (competencia in vaga.competencias) {
                    Integer competenciaId = buscarOuCriarCompetencia(competencia, conn)
                    String insertCompetenciaSql = """
                        INSERT INTO competencias_vagas (competencia_id, vaga_id)
                        VALUES (?, ?)
                    """
                    PreparedStatement compStmt = conn.prepareStatement(insertCompetenciaSql)
                    compStmt.setInt(1, competenciaId)
                    compStmt.setInt(2, vaga.id)
                    compStmt.executeUpdate()
                    compStmt.close()
                }
            }

        } catch (Exception e) {
            println "ERRO ao atualizar vaga: ${e.message}"
            e.printStackTrace()
            throw e
        } finally {
            DatabaseConnection.closeResources(conn, statement, null)
        }
    }

    /**
     * Remove uma vaga do banco de dados
     * @param id - ID da vaga a ser removida
     */
    void deletar(Integer id) {
        String sql = "DELETE FROM vagas WHERE idVagas = ?"

        Connection conn = null
        PreparedStatement statement = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(sql)
            statement.setInt(1, id)

            int rowsAffected = statement.executeUpdate()
            println "DEBUG: Rows affected on delete: ${rowsAffected}"

        } catch (Exception e) {
            println "ERRO ao deletar vaga: ${e.message}"
            e.printStackTrace()
        } finally {
            DatabaseConnection.closeResources(conn, statement, null)
        }
    }

    /**
     * Método auxiliar para mapear ResultSet em objeto Vaga
     * @param rs - ResultSet posicionado na linha atual
     * @param conn - conexão para buscar empresa
     * @return Vaga - objeto criado a partir dos dados
     */
    private Vaga mapearVaga(ResultSet rs, Connection conn) {
        Integer vagaId = rs.getInt("idvagas")
        Integer empresaId = rs.getInt("empresa_id")

        // Buscar empresa
        Empresa empresa = buscarEmpresa(empresaId, conn)

        Vaga vaga = new Vaga(
            rs.getString("nome_vaga"),
            rs.getString("descricao"),
            [], // competências serão carregadas separadamente
            empresa
        )

        vaga.id = vagaId
        vaga.cidade = rs.getString("cidade")
        vaga.criadoEm = rs.getTimestamp("criado_em")?.toLocalDateTime()

        return vaga
    }

    /**
     * Busca uma empresa pelo ID
     * @param empresaId - ID da empresa
     * @param conn - conexão ativa
     * @return Empresa - objeto empresa
     */
    private Empresa buscarEmpresa(Integer empresaId, Connection conn) {
        String sql = "SELECT * FROM empresas WHERE idEmpresas = ?"

        PreparedStatement statement = null
        ResultSet rs = null

        try {
            statement = conn.prepareStatement(sql)
            statement.setInt(1, empresaId)
            rs = statement.executeQuery()

            if (rs.next()) {
                Empresa empresa = new Empresa(
                    rs.getString("nome_empresa"),
                    rs.getString("email"),
                    rs.getString("cnpj"),
                    rs.getString("pais"),
                    rs.getString("pais"), // estado - usando pais já que estado não existe na tabela
                    rs.getString("cep"),
                    rs.getString("descricao")
                )
                empresa.id = empresaId
                empresa.senha = rs.getString("senha")
                empresa.criadoEm = rs.getTimestamp("criado_em")?.toLocalDateTime()
                return empresa
            }
        } catch (Exception e) {
            println "ERRO ao buscar empresa: ${e.message}"
        } finally {
            if (rs != null) rs.close()
            if (statement != null) statement.close()
        }

        return null
    }

    /**
     * Busca ou cria uma competência no banco de dados
     * @param nomeCompetencia - nome da competência
     * @param conn - conexão ativa
     * @return Integer - ID da competência
     */
    private Integer buscarOuCriarCompetencia(String nomeCompetencia, Connection conn) {
        String sqlBuscar = "SELECT idcompetencias FROM competencias WHERE nome_competencia = ?"

        PreparedStatement stmtBuscar = null
        ResultSet rs = null

        try {
            // Tentar buscar competência existente
            stmtBuscar = conn.prepareStatement(sqlBuscar)
            stmtBuscar.setString(1, nomeCompetencia)
            rs = stmtBuscar.executeQuery()

            if (rs.next()) {
                // Competência já existe, retornar ID
                return rs.getInt("idcompetencias")
            }

            // Competência não existe, criar nova
            String sqlInserir = "INSERT INTO competencias (nome_competencia, criado_em) VALUES (?, ?)"
            PreparedStatement stmtInserir = conn.prepareStatement(sqlInserir, Statement.RETURN_GENERATED_KEYS)
            stmtInserir.setString(1, nomeCompetencia)
            stmtInserir.setObject(2, LocalDateTime.now())
            stmtInserir.executeUpdate()

            ResultSet rsKeys = stmtInserir.getGeneratedKeys()
            if (rsKeys.next()) {
                Integer novoId = rsKeys.getInt(1)
                rsKeys.close()
                stmtInserir.close()
                return novoId
            }

            stmtInserir.close()

        } catch (Exception e) {
            println "ERRO ao buscar/criar competência '${nomeCompetencia}': ${e.message}"
            e.printStackTrace()
        } finally {
            if (rs != null) rs.close()
            if (stmtBuscar != null) stmtBuscar.close()
        }

        return null
    }

    /**
     * Busca todas as competências de uma vaga
     * @param vagaId - ID da vaga
     * @param conn - conexão ativa
     * @return List<String> - lista de nomes das competências
     */
    private List<String> buscarCompetencias(Integer vagaId, Connection conn) {
        String sql = """
            SELECT c.nome_competencia
            FROM competencias c
            INNER JOIN competencias_vagas cv ON c.idcompetencias = cv.competencia_id
            WHERE cv.vaga_id = ?
        """

        List<String> competencias = []
        PreparedStatement statement = null
        ResultSet resultSet = null

        try {
            statement = conn.prepareStatement(sql)
            statement.setInt(1, vagaId)
            resultSet = statement.executeQuery()

            while (resultSet.next()) {
                competencias << resultSet.getString("nome_competencia")
            }

        } catch (Exception e) {
            println "ERRO ao buscar competências da vaga ${vagaId}: ${e.message}"
            e.printStackTrace()
        } finally {
            if (resultSet != null) resultSet.close()
            if (statement != null) statement.close()
        }

        return competencias
    }
}
