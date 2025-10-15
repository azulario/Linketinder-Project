package com.linketinder.dao

import com.linketinder.database.DatabaseConnection
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.time.LocalDateTime

class CompetenciaDAO {

    private static final String SQL_BUSCAR = "SELECT idcompetencias FROM competencias WHERE nome_competencia = ?"
    private static final String SQL_INSERIR = "INSERT INTO competencias (nome_competencia, criado_em) VALUES (?, ?)"

    Integer buscarOuCriar(String nomeCompetencia, Connection conn) {
        Integer id = buscar(nomeCompetencia, conn)
        return id ?: criar(nomeCompetencia, conn)
    }

    private Integer buscar(String nomeCompetencia, Connection conn) {
        PreparedStatement statement = null
        ResultSet rs = null

        try {
            statement = conn.prepareStatement(SQL_BUSCAR)
            statement.setString(1, nomeCompetencia)
            rs = statement.executeQuery()

            if (rs.next()) {
                return rs.getInt("idcompetencias")
            }
        } finally {
            rs?.close()
            statement?.close()
        }

        return null
    }

    private Integer criar(String nomeCompetencia, Connection conn) {
        PreparedStatement statement = null
        ResultSet rsKeys = null

        try {
            statement = conn.prepareStatement(SQL_INSERIR, Statement.RETURN_GENERATED_KEYS)
            statement.setString(1, nomeCompetencia)
            statement.setObject(2, LocalDateTime.now())
            statement.executeUpdate()

            rsKeys = statement.getGeneratedKeys()
            if (rsKeys.next()) {
                return rsKeys.getInt(1)
            }
        } finally {
            rsKeys?.close()
            statement?.close()
        }

        return null
    }

    List<String> buscarPorCandidato(Integer candidatoId, Connection conn) {
        String sql = """
            SELECT c.nome_competencia
            FROM competencias c
            INNER JOIN candidato_competencias cc ON c.idcompetencias = cc.competencia_id
            WHERE cc.candidato_id = ?
        """

        return buscarCompetencias(sql, candidatoId, conn)
    }

    List<String> buscarPorVaga(Integer vagaId, Connection conn) {
        String sql = """
            SELECT c.nome_competencia
            FROM competencias c
            INNER JOIN competencias_vagas cv ON c.idcompetencias = cv.competencia_id
            WHERE cv.vaga_id = ?
        """

        return buscarCompetencias(sql, vagaId, conn)
    }

    private List<String> buscarCompetencias(String sql, Integer id, Connection conn) {
        List<String> competencias = []
        PreparedStatement statement = null
        ResultSet rs = null

        try {
            statement = conn.prepareStatement(sql)
            statement.setInt(1, id)
            rs = statement.executeQuery()

            while (rs.next()) {
                competencias << rs.getString("nome_competencia")
            }
        } finally {
            rs?.close()
            statement?.close()
        }

        return competencias
    }
}

