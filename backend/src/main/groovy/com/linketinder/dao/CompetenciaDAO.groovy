package com.linketinder.dao

import com.linketinder.database.DatabaseConnection
import com.linketinder.model.Competencia

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.time.LocalDateTime

class CompetenciaDAO extends BaseDAO<Competencia> {
    private static final String SQL_LISTAR = "SELECT * FROM competencias ORDER BY idCompetencias"
    private static final String SQL_BUSCAR_POR_ID = "SELECT * FROM competencias WHERE idCompetencias = ?"
    private static final String SQL_INSERIR = """
        INSERT INTO competencias (
            nome_competencia,
            criado_em
        )
        VALUES (?, ?)
"""
    private static final String SQL_ATUALIZAR = """
        UPDATE competencias 
        SET nome_competencia = ?
        WHERE idCompetencias = ?
"""
    private static final String SQL_DELETAR = "DELETE FROM competencias WHERE idCompetencias = ?"

    private static final String SQL_ASSOCIAR_CANDIDATO = """
        INSERT INTO candidato_competencias (candidato_id, competencia_id)
        VALUES (?, ?)
        ON CONFLICT DO NOTHING
"""

    private static final String SQL_ASSOCIAR_VAGA = """
        INSERT INTO competencias_vagas (vaga_id, competencia_id)
        VALUES (?, ?)
        ON CONFLICT DO NOTHING
"""

    List<Competencia> listar() {
        return executarConsulta(SQL_LISTAR)
    }

    Competencia buscarPorId(Integer id) {
        return buscarUmObjeto(SQL_BUSCAR_POR_ID, id)
    }

    void inserir(Competencia competencia) {
        competencia.id = executarInsert(SQL_INSERIR,
                competencia.nomeCompetencia,
                LocalDateTime.now()
        )
    }

    void atualizar(Competencia competencia) {
        executarUpdate(SQL_ATUALIZAR,
                competencia.nomeCompetencia,
                competencia.id
        )
    }

    void deletar(Integer id) {
        executarUpdate(SQL_DELETAR, id)
    }

    void associarACandidato(Integer candidatoId, List<String> competencias) {
        competencias.each { String nomeCompetencia ->
            Integer competenciaId = buscarOuCriarPorNome(nomeCompetencia)
            executarUpdate(SQL_ASSOCIAR_CANDIDATO, candidatoId, competenciaId)
        }
    }

    void associarAVaga(Integer vagaId, List<String> competencias) {
        competencias.each { String nomeCompetencia ->
            Integer competenciaId = buscarOuCriarPorNome(nomeCompetencia)
            executarUpdate(SQL_ASSOCIAR_VAGA, vagaId, competenciaId)
        }
    }

    List<String> buscarPorCandidato(Integer candidatoId) {
        String sql = """
            SELECT c.nome_competencia 
            FROM competencias c
            INNER JOIN candidato_competencias cc ON c.idCompetencias = cc.competencia_id
            WHERE cc.candidato_id = ?
            ORDER BY c.nome_competencia
        """
        return buscarNomesDeCompetencias(sql, candidatoId)
    }

    List<String> buscarPorVaga(Integer vagaId) {
        String sql = """
            SELECT c.nome_competencia 
            FROM competencias c
            INNER JOIN competencias_vagas cv ON c.idCompetencias = cv.competencia_id
            WHERE cv.vaga_id = ?
            ORDER BY c.nome_competencia
        """
        return buscarNomesDeCompetencias(sql, vagaId)
    }

    private List<String> buscarNomesDeCompetencias(String sqlQuery, Integer parametroId) {
        List<String> nomesCompetencias = []
        Connection conexao = null
        PreparedStatement comando = null
        ResultSet resultado = null

        try {
            conexao = DatabaseConnection.getConnection()
            comando = conexao.prepareStatement(sqlQuery)
            comando.setInt(1, parametroId)
            resultado = comando.executeQuery()

            while (resultado.next()) {
                nomesCompetencias << resultado.getString("nome_competencia")
            }
        } catch (Exception erro) {
            throw new RuntimeException("Erro ao buscar nomes de competências: ${erro.message}", erro)
        } finally {
            DatabaseConnection.closeResources(conexao, comando, resultado)
        }

        return nomesCompetencias
    }

    private Integer buscarOuCriarPorNome(String nomeCompetencia) {
        String sqlBuscar = "SELECT * FROM competencias WHERE nome_competencia = ?"

        List<Competencia> competencias = executarConsulta(sqlBuscar, nomeCompetencia)

        if (competencias && !competencias.isEmpty()) {
            return competencias[0].id
        }

        Competencia novaCompetencia = new Competencia(nomeCompetencia)
        inserir(novaCompetencia)
        return novaCompetencia.id
    }

    @Override
    protected Competencia mapearResultSet(ResultSet resultSet) {
        Competencia competencia = new Competencia(resultSet.getString("nome_competencia"))
        competencia.id = resultSet.getInt("idCompetencias")
        competencia.criadoEm = resultSet.getTimestamp("criado_em").toLocalDateTime()
        return competencia
    }
}

