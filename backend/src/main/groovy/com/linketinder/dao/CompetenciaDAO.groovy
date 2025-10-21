package com.linketinder.dao

import com.linketinder.database.DatabaseConnection
import com.linketinder.model.Competencia

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.time.LocalDateTime

class CompetenciaDAO {

    private static final String SQL_INSERIR = """
        INSERT INTO competencias (nome_competencia, criado_em)
        VALUES (?, ?)
    """
    private static final String SQL_LISTAR = """
        SELECT * FROM competencias 
        ORDER BY nome_competencia
    """
    private static final String SQL_BUSCAR_POR_ID = "SELECT * FROM competencias WHERE idcompetencias = ?"
    private static final String SQL_BUSCAR_POR_NOME = "SELECT * FROM competencias WHERE nome_competencia = ?"
    private static final String SQL_ATUALIZAR = """
        UPDATE competencias 
        SET nome_competencia = ? 
        WHERE idcompetencias = ?
    """
    private static final String SQL_DELETAR = "DELETE FROM competencias WHERE idcompetencias = ?"

    Integer inserir(Competencia competencia) {
        Connection conn = null
        PreparedStatement statement = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(SQL_INSERIR, Statement.RETURN_GENERATED_KEYS)

            statement.setString(1, competencia.nomeCompetencia)
            statement.setObject(2, LocalDateTime.now())

            statement.executeUpdate()

            resultSet = statement.getGeneratedKeys()
            if (resultSet.next()) {
                competencia.idCompetencias = resultSet.getInt(1)
                return competencia.idCompetencias
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inserir competência: ${e.message}", e)
        } finally {
            DatabaseConnection.closeResources(conn, statement, resultSet)
        }

        return null
    }

    List<Competencia> listarTodas() {
        List<Competencia> competencias = []
        Connection conn = null
        PreparedStatement statement = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(SQL_LISTAR)
            resultSet = statement.executeQuery()

            while (resultSet.next()) {
                competencias.add(mapearCompetencia(resultSet))
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar competências: ${e.message}", e)
        } finally {
            DatabaseConnection.closeResources(conn, statement, resultSet)
        }

        return competencias
    }

    Competencia buscarPorId(Integer id) {
        if (!id) return null

        Competencia competencia = null
        Connection conn = null
        PreparedStatement statement = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(SQL_BUSCAR_POR_ID)
            statement.setInt(1, id)
            resultSet = statement.executeQuery()

            if (resultSet.next()) {
                competencia = mapearCompetencia(resultSet)
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar competência por ID: ${e.message}", e)
        } finally {
            DatabaseConnection.closeResources(conn, statement, resultSet)
        }

        return competencia
    }

    Competencia buscarPorNome(String nome) {
        if (!nome) return null

        Competencia competencia = null
        Connection conn = null
        PreparedStatement statement = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(SQL_BUSCAR_POR_NOME)
            statement.setString(1, nome)
            resultSet = statement.executeQuery()

            if (resultSet.next()) {
                competencia = mapearCompetencia(resultSet)
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar competência por nome: ${e.message}", e)
        } finally {
            DatabaseConnection.closeResources(conn, statement, resultSet)
        }

        return competencia
    }

    Integer buscarOuCriar(String nomeCompetencia) {
        Competencia competenciaExistente = buscarPorNome(nomeCompetencia)

        if (competenciaExistente) {
            return competenciaExistente.idCompetencias
        }

        Competencia novaCompetencia = new Competencia(nomeCompetencia)
        return inserir(novaCompetencia)
    }

    Integer buscarOuCriar(String nomeCompetencia, Connection conn) {
        PreparedStatement statement = null
        ResultSet resultSet = null
        Integer competenciaId = null

        try {
            statement = conn.prepareStatement(SQL_BUSCAR_POR_NOME)
            statement.setString(1, nomeCompetencia)
            resultSet = statement.executeQuery()

            if (resultSet.next()) {
                competenciaId = resultSet.getInt("idcompetencias")
                return competenciaId
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar competência por nome: ${e.message}", e)
        } finally {
            if (statement) statement.close()
            if (resultSet) resultSet.close()
        }


        PreparedStatement insertStmt = null
        ResultSet generatedKeys = null

        try {
            insertStmt = conn.prepareStatement(SQL_INSERIR, Statement.RETURN_GENERATED_KEYS)
            insertStmt.setString(1, nomeCompetencia)
            insertStmt.setObject(2, LocalDateTime.now())
            insertStmt.executeUpdate()

            generatedKeys = insertStmt.getGeneratedKeys()
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1)
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inserir competência: ${e.message}", e)
        } finally {
            if (insertStmt) insertStmt.close()
            if (generatedKeys) generatedKeys.close()
        }

        return null
    }

    void atualizar(Competencia competencia) {
        Connection conn = null
        PreparedStatement statement = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(SQL_ATUALIZAR)

            statement.setString(1, competencia.nomeCompetencia)
            statement.setInt(2, competencia.idCompetencias)

            statement.executeUpdate()
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar competência: ${e.message}", e)
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
            throw new RuntimeException("Erro ao deletar competência: ${e.message}", e)
        } finally {
            DatabaseConnection.closeResources(conn, statement, null)
        }
    }

    private Competencia mapearCompetencia(ResultSet rs) {
        Competencia competencia = new Competencia(
            rs.getString("nome_competencia")
        )
        competencia.idCompetencias = rs.getInt("idcompetencias")
        competencia.criadoEm = rs.getTimestamp("criado_em")
        return competencia
    }

    void associarACandidato(Integer candidatoId, List<String> competencias) {
        if (!competencias || competencias.isEmpty()) return

        Connection conn = null
        PreparedStatement deleteStmt = null
        PreparedStatement insertStmt = null

        try {
            conn = DatabaseConnection.getConnection()

            // Deletar competências antigas
            String sqlDelete = "DELETE FROM candidato_competencias WHERE candidato_id = ?"
            deleteStmt = conn.prepareStatement(sqlDelete)
            deleteStmt.setInt(1, candidatoId)
            deleteStmt.executeUpdate()
            deleteStmt.close()

            // Inserir novas competências
            String sqlInsert = "INSERT INTO candidato_competencias (candidato_id, competencia_id) VALUES (?, ?)"
            insertStmt = conn.prepareStatement(sqlInsert)

            competencias.each { nomeCompetencia ->
                Integer competenciaId = buscarOuCriar(nomeCompetencia)
                insertStmt.setInt(1, candidatoId)
                insertStmt.setInt(2, competenciaId)
                insertStmt.executeUpdate()
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao associar competências ao candidato: ${e.message}", e)
        } finally {
            if (deleteStmt) deleteStmt.close()
            if (insertStmt) insertStmt.close()
            DatabaseConnection.closeResources(conn, null, null)
        }
    }

    List<String> buscarPorCandidato(Integer candidatoId) {
        Connection conn = null
        PreparedStatement statement = null
        ResultSet resultSet = null
        List<String> competencias = []

        String sql = """
            SELECT c.nome_competencia 
            FROM competencias c
            INNER JOIN candidato_competencias cc ON c.idcompetencias = cc.competencia_id
            WHERE cc.candidato_id = ?
        """

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(sql)
            statement.setInt(1, candidatoId)
            resultSet = statement.executeQuery()

            while (resultSet.next()) {
                competencias.add(resultSet.getString("nome_competencia"))
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar competências do candidato: ${e.message}", e)
        } finally {
            DatabaseConnection.closeResources(conn, statement, resultSet)
        }

        return competencias
    }

    List<String> buscarPorCandidato(Integer candidatoId, Connection conn) {
        List<String> competencias = []
        PreparedStatement statement = null
        ResultSet resultSet = null

        String sql = """
            SELECT c.nome_competencia 
            FROM competencias c
            INNER JOIN candidato_competencias cc ON c.idcompetencias = cc.competencia_id
            WHERE cc.candidato_id = ?
        """

        try {
            statement = conn.prepareStatement(sql)
            statement.setInt(1, candidatoId)
            resultSet = statement.executeQuery()

            while (resultSet.next()) {
                competencias.add(resultSet.getString("nome_competencia"))
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar competências do candidato: ${e.message}", e)
        } finally {
            if (statement) statement.close()
            if (resultSet) resultSet.close()
        }

        return competencias
    }

    List<String> buscarPorVaga(Integer vagaId, Connection conn) {
        List<String> competencias = []
        PreparedStatement statement = null
        ResultSet resultSet = null

        String sql = """
            SELECT c.nome_competencia 
            FROM competencias c
            INNER JOIN vaga_competencias vc ON c.idcompetencias = vc.competencia_id
            WHERE vc.vaga_id = ?
        """

        try {
            statement = conn.prepareStatement(sql)
            statement.setInt(1, vagaId)
            resultSet = statement.executeQuery()

            while (resultSet.next()) {
                competencias.add(resultSet.getString("nome_competencia"))
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar competências da vaga: ${e.message}", e)
        } finally {
            if (statement) statement.close()
            if (resultSet) resultSet.close()
        }

        return competencias
    }
}

