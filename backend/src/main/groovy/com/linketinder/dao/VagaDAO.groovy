package com.linketinder.dao

import com.linketinder.model.Vaga
import java.sql.ResultSet
import java.time.LocalDateTime

class VagaDAO extends BaseDAO<Vaga> {
    private static final String SQL_LISTAR = "SELECT * FROM vagas ORDER BY idVagas"
    private static final String SQL_BUSCAR_POR_ID = "SELECT * FROM vagas WHERE idVagas = ?"
    private static final String SQL_LISTAR_POR_EMPRESA = """
        SELECT * FROM vagas 
        WHERE empresa_id = ? 
        ORDER BY criado_em DESC
    """
    private static final String SQL_INSERIR = """
        INSERT INTO vagas (
            nome_vaga,
            descricao,
            endereco_id,
            empresa_id,
            criado_em
        )
        VALUES (?, ?, ?, ?, ?)
    """
    private static final String SQL_ATUALIZAR = """
        UPDATE vagas 
        SET nome_vaga = ?, descricao = ?, endereco_id = ?, empresa_id = ?
        WHERE idVagas = ?
    """
    private static final String SQL_DELETAR = "DELETE FROM vagas WHERE idVagas = ?"

    private final CompetenciaDAO competenciaDAO = new CompetenciaDAO()
    private final EnderecoDAO enderecoDAO = new EnderecoDAO()
    private final EmpresaDAO empresaDAO = new EmpresaDAO()

    List<Vaga> listar() {
        return executarConsulta(SQL_LISTAR).collect { vaga ->
            addEnderecoECompetencias(vaga)
        }
    }

    List<Vaga> listarPorEmpresa(Integer empresaId) {
        return executarConsulta(SQL_LISTAR_POR_EMPRESA, empresaId).collect { vaga ->
            addEnderecoECompetencias(vaga)
        }
    }

    Vaga buscarPorId(Integer id) {
        Vaga vaga = buscarUmObjeto(SQL_BUSCAR_POR_ID, id)
        return vaga ? addEnderecoECompetencias(vaga) : null
    }

    void inserir(Vaga vaga) {
        Integer enderecoId = vaga.endereco ?
                enderecoDAO.buscarOuCriar(vaga.endereco) : null

        vaga.id = executarInsert(SQL_INSERIR,
            vaga.titulo,
            vaga.descricao,
            enderecoId,
            vaga.empresaId,
            LocalDateTime.now()
        )

        if (vaga.competencias) {
            competenciaDAO.associarAVaga(vaga.id, vaga.competencias)
        }
    }

    void atualizar(Vaga vaga) {
        Integer enderecoId = vaga.endereco ?
                enderecoDAO.buscarOuCriar(vaga.endereco) : vaga.enderecoId

        executarUpdate(SQL_ATUALIZAR,
            vaga.titulo,
            vaga.descricao,
            enderecoId,
            vaga.empresaId,
            vaga.id
        )

        if (vaga.competencias) {
            competenciaDAO.associarAVaga(vaga.id, vaga.competencias)
        }
    }

    void deletar(Integer id) {
        executarUpdate(SQL_DELETAR, id)
    }

    @Override
    protected Vaga mapearResultSet(ResultSet resultSet) {
        Vaga vaga = new Vaga(
            resultSet.getString("nome_vaga"),
            resultSet.getString("descricao"),
            resultSet.getInt("empresa_id")
        )
        vaga.id = resultSet.getInt("idVagas")
        vaga.enderecoId = resultSet.getObject("endereco_id") as Integer
        return vaga
    }

    private Vaga addEnderecoECompetencias(Vaga vaga) {
        // Buscar e popular empresa
        if (vaga.empresaId) {
            vaga.empresa = empresaDAO.buscarPorId(vaga.empresaId)
        }

        // Buscar e popular endereço
        if (vaga.enderecoId) {
            vaga.endereco = enderecoDAO.buscarPorId(vaga.enderecoId)
        }

        // Buscar e popular competências
        vaga.competencias = competenciaDAO.buscarPorVaga(vaga.id)

        return vaga
    }

}
