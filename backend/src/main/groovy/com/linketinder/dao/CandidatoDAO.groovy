package com.linketinder.dao

import com.linketinder.model.Candidato
import java.sql.ResultSet
import java.time.LocalDate
import java.time.LocalDateTime

class CandidatoDAO extends BaseDAO<Candidato> {
    private static final String SQL_LISTAR = "SELECT * FROM candidatos ORDER BY idCandidatos"
    private static final String SQL_BUSCAR_POR_ID = "SELECT * FROM candidatos WHERE idCandidatos = ?"
    private static final String SQL_INSERIR = """
        INSERT INTO candidatos (
            nome,
            sobrenome,
            data_de_nascimento,
            email,
            cpf,
            endereco_id,
            descricao,
            senha,
            criado_em
        )
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
"""
    private static final String SQL_ATUALIZAR = """
        UPDATE candidatos 
        SET nome = ?, 
        sobrenome = ?, 
        data_de_nascimento = ?, 
        email = ?, 
        cpf = ?, 
        endereco_id = ?, 
        descricao = ?, 
        senha = ?
        WHERE idCandidatos = ?
"""
    private static final String SQL_DELETAR = "DELETE FROM candidatos WHERE idCandidatos = ?"

    private final CompetenciaDAO competenciaDAO = new CompetenciaDAO()
    private final EnderecoDAO enderecoDAO = new EnderecoDAO()

    List<Candidato> listar() {
        return executarConsulta(SQL_LISTAR).collect {candidato ->
            addEndEComp(candidato)
        }
    }

    Candidato buscarPorId(Integer id) {
        Candidato candidato = buscarUmObjeto(SQL_BUSCAR_POR_ID, id)
        return candidato ? addEndEComp(candidato) : null
    }

    void inserir(Candidato candidato) {
        Integer enderecoId = candidato.endereco ?
                enderecoDAO.buscarOuCriar(candidato.endereco) : null

        candidato.id = executarInsert(SQL_INSERIR,
                candidato.nome,
                candidato.sobrenome,
                candidato.dataDeNascimento,
                candidato.email,
                candidato.cpf,
                enderecoId,
                candidato.descricao,
                candidato.senha ?: "senha123",
                LocalDateTime.now()
        )

        if (candidato.competencias) {
            competenciaDAO.associarACandidato(candidato.id, candidato.competencias)
        }
    }

    void atualizar(Candidato candidato) {
        Integer enderecoId = candidato.endereco ?
                enderecoDAO.buscarOuCriar(candidato.endereco) : candidato.enderecoId

        executarUpdate(SQL_ATUALIZAR,
                candidato.nome,
                candidato.sobrenome,
                candidato.dataDeNascimento,
                candidato.email,
                candidato.cpf,
                enderecoId,
                candidato.descricao,
                candidato.senha ?: "senha123",
                candidato.id
        )

        if (candidato.competencias) {
            competenciaDAO.associarACandidato(candidato.id, candidato.competencias)
        }
    }

    void deletar(Integer id) {
        executarUpdate(SQL_DELETAR, id)
    }

    @Override
    protected Candidato mapearResultSet(ResultSet resultSet) {
        Candidato candidato = new Candidato(
                resultSet.getString("nome"),
                resultSet.getString("sobrenome"),
                resultSet.getString("email"),
                resultSet.getString("cpf"),
                resultSet.getDate("data_de_nascimento")?.toLocalDate(),
                resultSet.getString("descricao"),
                []
        )
        candidato.id = resultSet.getInt("idCandidatos")
        candidato.enderecoId = resultSet.getObject("endereco_id") as Integer
        return candidato
    }

    private Candidato addEndEComp(Candidato candidato) {
        candidato.competencias = competenciaDAO.buscarPorCandidato(candidato.id)

        if (candidato.enderecoId) {
            candidato.endereco = enderecoDAO.buscarPorId(candidato.enderecoId)
        }
        return candidato
    }
}
