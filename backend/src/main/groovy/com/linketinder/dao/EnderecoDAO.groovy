package com.linketinder.dao

import com.linketinder.model.Endereco

import java.sql.ResultSet
import java.time.LocalDateTime


class EnderecoDAO extends BaseDAO<Endereco> {

    private static final String SQL_INSERIR = """
        INSERT INTO enderecos (cep, estado, cidade, pais, criado_em)
        VALUES (?, ?, ?, ?, ?)
    """
    private static final String SQL_BUSCAR_POR_ID = "SELECT * FROM enderecos WHERE idEnderecos = ?"
    private static final String SQL_BUSCAR_POR_DADOS = """
        SELECT * FROM enderecos 
        WHERE pais = ? AND (estado = ? OR estado IS NULL) 
        AND (cidade = ? OR cidade IS NULL) 
        AND (cep = ? OR cep IS NULL)
        LIMIT 1
    """
    private static final String SQL_ATUALIZAR = """
        UPDATE enderecos 
        SET cep = ?, estado = ?, cidade = ?, pais = ?
        WHERE idEnderecos = ?
    """
    private static final String SQL_DELETAR = "DELETE FROM enderecos WHERE idEnderecos = ?"

    Integer inserir(Endereco endereco) {
        Integer id = executarInsert(
            SQL_INSERIR,
            endereco.cep,
            endereco.estado,
            endereco.cidade,
            endereco.pais,
            LocalDateTime.now()
        )
        endereco.id = id
        return id
    }

    Endereco buscarPorId(Integer id) {
        if (!id) return null
        return buscarUmObjeto(SQL_BUSCAR_POR_ID, id)
    }

    Integer buscarOuCriar(Endereco endereco) {
        Integer enderecoId = buscarPorDados(endereco)
        return enderecoId ?: inserir(endereco)
    }

    private Integer buscarPorDados(Endereco endereco) {
        List<Endereco> resultados = executarConsulta(
            SQL_BUSCAR_POR_DADOS,
            endereco.pais,
            endereco.estado,
            endereco.cidade,
            endereco.cep
        )
        return resultados.isEmpty() ? null : resultados[0].id
    }

    void atualizar(Endereco endereco) {
        executarUpdate(
            SQL_ATUALIZAR,
            endereco.cep,
            endereco.estado,
            endereco.cidade,
            endereco.pais,
            endereco.id
        )
    }

    void deletar(Integer id) {
        executarUpdate(SQL_DELETAR, id)
    }

    @Override
    protected Endereco mapearResultSet(ResultSet rs) {
        Endereco endereco = new Endereco(
            rs.getString("pais"),
            rs.getString("estado"),
            rs.getString("cidade"),
            rs.getString("cep")
        )
        endereco.id = rs.getInt("idEnderecos")
        endereco.criadoEm = rs.getTimestamp("criado_em")?.toLocalDateTime()
        return endereco
    }
}

