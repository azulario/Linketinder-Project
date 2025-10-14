package com.linketinder.dao

import com.linketinder.database.DatabaseConnection
import com.linketinder.model.Candidato
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.time.LocalDateTime
import java.time.LocalDate

/**
 * CandidatoDAO - Data Access Object para a entidade Candidato
 *
 * Responsável por todas as operações de banco de dados relacionadas a candidatos:
 * - CREATE (inserir)
 * - READ (listar, buscarPorId)
 * - UPDATE (atualizar)
 * - DELETE (deletar)
 */
class CandidatoDAO {

    /**
     * Insere um novo candidato no banco de dados
     * @param candidato - objeto Candidato a ser inserido
     */
    void inserir(Candidato candidato) {

        String sql = """
            INSERT INTO candidatos (nome, sobrenome, data_de_nascimento, email, cpf, pais, cep, descricao, senha, criado_em)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """

        Connection conn = null
        PreparedStatement statement = null
        ResultSet resultSet = null


        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

            statement.setString(1, candidato.nome)
            statement.setString(2, candidato.sobrenome)
            statement.setObject(3, candidato.dataDeNascimento)
            statement.setString(4, candidato.email)
            statement.setString(5, candidato.cpf)
            statement.setString(6, candidato.pais)
            statement.setString(7, candidato.cep)
            statement.setString(8, candidato.descricao)
            statement.setString(9, candidato.senha ?: "senha123")
            statement.setObject(10, LocalDateTime.now())

            int rowsAffected = statement.executeUpdate()
            println "DEBUG: Rows affected: ${rowsAffected}"

            resultSet = statement.getGeneratedKeys()
            if (resultSet.next()) {
                candidato.id = resultSet.getInt(1)
                println "DEBUG: ID gerado: ${candidato.id}"
            }

            // Inserir competências na tabela N:N candidato_competencias
            if (candidato.competencias) {
                for (competencia in candidato.competencias) {
                    Integer competenciaId = buscarOuCriarCompetencia(competencia, conn)
                    String insertCompetenciaSql = """
                        INSERT INTO candidato_competencias (candidato_id, competencia_id)
                        VALUES (?, ?)
                    """
                    PreparedStatement compStmt = conn.prepareStatement(insertCompetenciaSql)
                    compStmt.setInt(1, candidato.id)
                    compStmt.setInt(2, competenciaId)
                    compStmt.executeUpdate()
                    compStmt.close()
                }
            }

        } catch (Exception e) {
            println "ERRO ao inserir candidato: ${e.message}"
            e.printStackTrace()
            throw e  // Relançar a exceção para que o teste possa capturá-la
        } finally {
            DatabaseConnection.closeResources(conn, statement, resultSet)

        }
    }
    /**
     * Lista todos os candidatos cadastrados no banco
     * @return List<Candidato> - lista com todos os candidatos
     */
    List<Candidato> listar() {
        String sql = "SELECT * FROM candidatos ORDER BY idCandidatos"
        List<Candidato> candidatos = []

        Connection conn = null
        Statement statement = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.createStatement()
            resultSet = statement.executeQuery(sql)

            while (resultSet.next()) {
                Candidato candidato = mapearCandidato(resultSet)
                // Buscar competências do candidato
                candidato.competencias = buscarCompetencias(candidato.id, conn)
                candidatos << candidato
            }
        } catch (Exception e) {
            println "ERRO ao listar candidatos: ${e.message}"
            e.printStackTrace()
        } finally {
            DatabaseConnection.closeResources(conn, statement, resultSet)
        }
        return candidatos
    }

    /**
     * Busca um candidato específico pelo ID
     * @param id - ID do candidato
     * @return Candidato - objeto encontrado ou null
     */
    Candidato buscarPorId(Integer id) {
        String sql = "SELECT * FROM candidatos WHERE idCandidatos = ?"

        Candidato candidato = null
        Connection conn = null
        PreparedStatement statement = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(sql)
            statement.setInt(1, id)
            resultSet = statement.executeQuery()

            if (resultSet.next()) {
                candidato = mapearCandidato(resultSet)
                // Buscar competências do candidato
                candidato.competencias = buscarCompetencias(candidato.id, conn)
            }
        } catch (Exception e) {
            println "ERRO ao buscar candidato por ID: ${e.message}"
            e.printStackTrace()
        } finally {
            DatabaseConnection.closeResources(conn, statement, resultSet)
        }
        return candidato
    }

    /**
     * Atualiza os dados de um candidato existente
     * @param candidato - objeto com dados atualizados
     */
    void atualizar(Candidato candidato) {
        // TODO: Implementar atualização de candidato
        // 1. Criar SQL UPDATE candidatos SET ... WHERE id = ?
        //    Atualizar: nome, sobrenome, data_nascimento, email, cpf, pais, cep, descricao, senha
        // 2. Abrir conexão
        // 3. Preparar PreparedStatement
        // 4. Setar todos os parâmetros (9 campos + id no WHERE)
        // 5. Executar stmt.executeUpdate()
        // 6. Atualizar competências:
        //    - Deletar todas as competências antigas: DELETE FROM candidato_competencias WHERE candidato_id = ?
        //    - Inserir novas competências (mesmo processo do inserir)
        // 7. Fechar recursos

        String sql = """
            UPDATE candidatos 
            SET nome = ?, sobrenome = ?, data_de_nascimento = ?, email = ?, cpf = ?, pais = ?, cep = ?, descricao = ?, senha = ?
            WHERE idCandidatos = ?
        """

        Connection conn = null
        PreparedStatement statement = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(sql)

            statement.setString(1, candidato.nome)
            statement.setString(2, candidato.sobrenome)
            statement.setObject(3, candidato.dataDeNascimento)
            statement.setString(4, candidato.email)
            statement.setString(5, candidato.cpf)
            statement.setString(6, candidato.pais)
            statement.setString(7, candidato.cep)
            statement.setString(8, candidato.descricao)
            statement.setString(9, candidato.senha ?: "senha123") // senha padrão se não informada
            statement.setInt(10, candidato.id)

            int rowsAffected = statement.executeUpdate()
            println "DEBUG: Rows affected on update: ${rowsAffected}"

            // Atualizar competências
            String deleteCompetenciasSql = "DELETE FROM candidato_competencias WHERE candidato_id = ?"
            PreparedStatement deleteStmt = conn.prepareStatement(deleteCompetenciasSql)
            deleteStmt.setInt(1, candidato.id)
            deleteStmt.executeUpdate()
            deleteStmt.close()

            if (candidato.competencias) {
                for (competencia in candidato.competencias) {
                    Integer competenciaId = buscarOuCriarCompetencia(competencia, conn)
                    String insertCompetenciaSql = """
                        INSERT INTO candidato_competencias (candidato_id, competencia_id)
                        VALUES (?, ?)
                    """
                    PreparedStatement compStmt = conn.prepareStatement(insertCompetenciaSql)
                    compStmt.setInt(1, candidato.id)
                    compStmt.setInt(2, competenciaId)
                    compStmt.executeUpdate()
                    compStmt.close()
                }
            }

        } catch (Exception e) {
            println "ERRO ao atualizar candidato: ${e.message}"
            e.printStackTrace()
            throw e // Relançar a exceção para que o teste possa capturá-la
        } finally {
            DatabaseConnection.closeResources(conn, statement, null)
        }
    }

    /**
     * Remove um candidato do banco de dados
     * @param id - ID do candidato a ser removido
     */
    void deletar(Integer id) {
        String sql = "DELETE FROM candidatos WHERE idCandidatos = ?"

        Connection conn = null
        PreparedStatement statement = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(sql)
            statement.setInt(1, id)

            int rowsAffected = statement.executeUpdate()
            println "DEBUG: Rows affected on delete: ${rowsAffected}"

        } catch (Exception e) {
            println "ERRO ao deletar candidato: ${e.message}"
            e.printStackTrace()
        } finally {
            DatabaseConnection.closeResources(conn, statement, null)
        }
    }

    /**
     * Metodo auxiliar para mapear ResultSet em objeto Candidato
     * @param rs - ResultSet posicionado na linha atual
     * @return Candidato - objeto criado a partir dos dados
     */
    private Candidato mapearCandidato(ResultSet rs) {
        Integer candidatoId = rs.getInt("idcandidatos")

        Candidato candidato = new Candidato(
            rs.getString("nome"),
            rs.getString("sobrenome"),
            rs.getString("email"),
            rs.getString("cpf"),
            rs.getDate("data_de_nascimento")?.toLocalDate(),
            rs.getString("pais"),
            rs.getString("cep"),
            rs.getString("descricao"),
            [] // competências serão carregadas separadamente
        )

        candidato.id = candidatoId
        candidato.senha = rs.getString("senha")
        candidato.criadoEm = rs.getTimestamp("criado_em")?.toLocalDateTime()

        return candidato
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
     * Busca todas as competências de um candidato
     * @param candidatoId - ID do candidato
     * @param conn - conexão ativa
     * @return List<String> - lista de nomes das competências
     */
    private List<String> buscarCompetencias(Integer candidatoId, Connection conn) {
        String sql = """
            SELECT c.nome_competencia
            FROM competencias c
            INNER JOIN candidato_competencias cc ON c.idcompetencias = cc.competencia_id
            WHERE cc.candidato_id = ?
        """

        List<String> competencias = []
        PreparedStatement statement = null
        ResultSet resultSet = null

        try {
            statement = conn.prepareStatement(sql)
            statement.setInt(1, candidatoId)
            resultSet = statement.executeQuery()

            while (resultSet.next()) {
                competencias << resultSet.getString("nome_competencia")
            }

        } catch (Exception e) {
            println "ERRO ao buscar competências do candidato ${candidatoId}: ${e.message}"
            e.printStackTrace()
        } finally {
            if (resultSet != null) resultSet.close()
            if (statement != null) statement.close()
        }

        return competencias
    }
}
