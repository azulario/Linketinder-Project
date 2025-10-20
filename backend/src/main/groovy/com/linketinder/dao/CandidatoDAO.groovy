package com.linketinder.dao

import com.linketinder.database.DatabaseConnection
import com.linketinder.model.Candidato
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.time.LocalDateTime

class CandidatoDAO {

    private static final String SQL_INSERIR = """
        INSERT INTO candidatos (nome, sobrenome, data_de_nascimento, email, cpf, endereco_id, descricao, senha, criado_em)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    """
    private static final String SQL_LISTAR = "SELECT * FROM candidatos ORDER BY idCandidatos"
    private static final String SQL_BUSCAR_POR_ID = "SELECT * FROM candidatos WHERE idCandidatos = ?"
    private static final String SQL_ATUALIZAR = """
        UPDATE candidatos 
        SET nome = ?, sobrenome = ?, data_de_nascimento = ?, email = ?, cpf = ?, endereco_id = ?, descricao = ?, senha = ?
        WHERE idCandidatos = ?
    """
    private static final String SQL_DELETAR = "DELETE FROM candidatos WHERE idCandidatos = ?"
    private static final String SQL_INSERIR_COMPETENCIA = "INSERT INTO candidato_competencias (candidato_id, competencia_id) VALUES (?, ?)"
    private static final String SQL_DELETAR_COMPETENCIAS = "DELETE FROM candidato_competencias WHERE candidato_id = ?"

    private final CompetenciaDAO competenciaDAO = new CompetenciaDAO()
    private final EnderecoDAO enderecoDAO = new EnderecoDAO()


    void inserir(Candidato candidato) {

        Connection conn = null
        PreparedStatement statement = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()

            Integer enderecoId = null
            if (candidato.endereco) {
                enderecoId = enderecoDAO.buscarOuCriar(candidato.endereco)
                candidato.enderecoId = enderecoId
            }

            statement = conn.prepareStatement(SQL_INSERIR, Statement.RETURN_GENERATED_KEYS)

            statement.setString(1, candidato.nome)
            statement.setString(2, candidato.sobrenome)
            statement.setObject(3, candidato.dataDeNascimento)
            statement.setString(4, candidato.email)
            statement.setString(5, candidato.cpf)
            statement.setObject(6, enderecoId)
            statement.setString(7, candidato.descricao)
            statement.setString(8, candidato.senha ?: "senha123")
            statement.setObject(9, LocalDateTime.now())

            statement.executeUpdate()

            resultSet = statement.getGeneratedKeys()
            if (resultSet.next()) {
                candidato.id = resultSet.getInt(1)
            }

            inserirCompetencias(candidato.id, candidato.competencias, conn)

        } catch (Exception e) {
            throw new RuntimeException("Erro ao inserir candidato: ${e.message}", e)
        } finally {
            DatabaseConnection.closeResources(conn, statement, resultSet)

        }
    }

    List<Candidato> listar() {
        List<Candidato> candidatos = []
        Connection conn = null
        Statement statement = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.createStatement()
            resultSet = statement.executeQuery(SQL_LISTAR)

            while (resultSet.next()) {
                Candidato candidato = mapearCandidato(resultSet)
                candidato.competencias = competenciaDAO.buscarPorCandidato(candidato.id, conn)

                if (candidato.enderecoId) {
                    candidato.endereco = enderecoDAO.buscarPorId(candidato.enderecoId)
                }

                candidatos << candidato
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar candidatos: ${e.message}", e)
        } finally {
            DatabaseConnection.closeResources(conn, statement, resultSet)
        }
        return candidatos
    }

    Candidato buscarPorId(Integer id) {
        Candidato candidato = null
        Connection conn = null
        PreparedStatement statement = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(SQL_BUSCAR_POR_ID)
            statement.setInt(1, id)
            resultSet = statement.executeQuery()

            if (resultSet.next()) {
                candidato = mapearCandidato(resultSet)
                candidato.competencias = competenciaDAO.buscarPorCandidato(candidato.id, conn)

                if (candidato.enderecoId) {
                    candidato.endereco = enderecoDAO.buscarPorId(candidato.enderecoId)
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar candidato por ID: ${e.message}", e)
        } finally {
            DatabaseConnection.closeResources(conn, statement, resultSet)
        }
        return candidato
    }

    void atualizar(Candidato candidato) {
        Connection conn = null
        PreparedStatement statement = null

        try {
            conn = DatabaseConnection.getConnection()

            Integer enderecoId = candidato.enderecoId
            if (candidato.endereco) {
                enderecoId = enderecoDAO.buscarOuCriar(candidato.endereco)
                candidato.enderecoId = enderecoId
            }

            statement = conn.prepareStatement(SQL_ATUALIZAR)

            statement.setString(1, candidato.nome)
            statement.setString(2, candidato.sobrenome)
            statement.setObject(3, candidato.dataDeNascimento)
            statement.setString(4, candidato.email)
            statement.setString(5, candidato.cpf)
            statement.setObject(6, enderecoId)
            statement.setString(7, candidato.descricao)
            statement.setString(8, candidato.senha ?: "senha123")
            statement.setInt(9, candidato.id)

            statement.executeUpdate()

            atualizarCompetencias(candidato.id, candidato.competencias, conn)

        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar candidato: ${e.message}", e)
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
            throw new RuntimeException("Erro ao deletar candidato: ${e.message}", e)
        } finally {
            DatabaseConnection.closeResources(conn, statement, null)
        }
    }

    private Candidato mapearCandidato(ResultSet rs) {
        Candidato candidato = new Candidato(
            rs.getString("nome"),
            rs.getString("sobrenome"),
            rs.getString("email"),
            rs.getString("cpf"),
            rs.getDate("data_de_nascimento")?.toLocalDate(),
            rs.getString("descricao"),
            []
        )

        candidato.id = rs.getInt("idcandidatos")
        candidato.enderecoId = rs.getObject("endereco_id") as Integer
        candidato.senha = rs.getString("senha")
        candidato.criadoEm = rs.getTimestamp("criado_em")?.toLocalDateTime()

        return candidato
    }

    private void inserirCompetencias(Integer candidatoId, List<String> competencias, Connection conn) {
        if (!competencias) return

        competencias.each { competencia ->
            Integer competenciaId = competenciaDAO.buscarOuCriar(competencia, conn)

            PreparedStatement compStmt = conn.prepareStatement(SQL_INSERIR_COMPETENCIA)
            compStmt.setInt(1, candidatoId)
            compStmt.setInt(2, competenciaId)
            compStmt.executeUpdate()
            compStmt.close()
        }
    }

    private void atualizarCompetencias(Integer candidatoId, List<String> competencias, Connection conn) {
        deletarCompetencias(candidatoId, conn)
        inserirCompetencias(candidatoId, competencias, conn)
    }

    private void deletarCompetencias(Integer candidatoId, Connection conn) {
        PreparedStatement statement = conn.prepareStatement(SQL_DELETAR_COMPETENCIAS)
        statement.setInt(1, candidatoId)
        statement.executeUpdate()
        statement.close()
    }
}
