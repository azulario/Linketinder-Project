package com.linketinder.dao

import com.linketinder.model.Competencia


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

    @Override
    protected Competencia mapearResultSet(ResultSet resultSet) {
        Competencia competencia = new Competencia(resultSet.getString("nome_competencia"))
        competencia.id = resultSet.getInt("idCompetencias")
        competencia.criadoEm = resultSet.getTimestamp("criado_em").toLocalDateTime()
        return competencia
    }
}

