package com.linketinder.dao

import com.linketinder.model.Empresa


import java.sql.ResultSet
import java.time.LocalDateTime


class EmpresaDAO extends BaseDAO<Empresa> {
    private static final String SQL_LISTAR = "SELECT * FROM empresas ORDER BY idempresas"
    private static final String SQL_BUSCAR_POR_ID = "SELECT * FROM empresas WHERE idempresas = ?"
    private static final String SQL_INSERIR = """
        INSERT INTO empresas (
            nome_empresa,
            cnpj,
            email,
            endereco_id,
            descricao,
            senha,
            criado_em
        )
        VALUES (?, ?, ?, ?, ?, ?, ?)
"""
    private static final String SQL_ATUALIZAR = """
        UPDATE empresas 
        SET nome_empresa = ?, cnpj = ?, email = ?, endereco_id = ?, descricao = ?, senha = ?
        WHERE idempresas = ?
"""
    private static final String SQL_DELETAR = "DELETE FROM empresas WHERE idempresas = ?"

    private final EnderecoDAO enderecoDAO = new EnderecoDAO()

    List<Empresa> listar() {
        return executarConsulta(SQL_LISTAR).collect { empresa ->
            addEndereco(empresa)
        }
    }

    Empresa buscarPorId(Integer id) {
        Empresa empresa = buscarUmObjeto(SQL_BUSCAR_POR_ID, id)
        return empresa ? addEndereco(empresa) : null
    }

    void inserir(Empresa empresa) {
        Integer enderecoId = empresa.endereco ?
                enderecoDAO.buscarOuCriar(empresa.endereco) : null

        empresa.id = executarInsert(SQL_INSERIR,
                empresa.nome,
                empresa.cnpj,
                empresa.email,
                enderecoId,
                empresa.descricao,
                empresa.senha ?: "senha123",
                LocalDateTime.now()
        )
    }

    void atualizar(Empresa empresa) {
        Integer enderecoId = empresa.endereco ?
                enderecoDAO.buscarOuCriar(empresa.endereco) : empresa.enderecoId

        executarUpdate(SQL_ATUALIZAR,
                empresa.nome,
                empresa.cnpj,
                empresa.email,
                enderecoId,
                empresa.descricao,
                empresa.senha ?: "senha123",
                empresa.id
        )
    }

    void deletar(Integer id) {
        executarUpdate(SQL_DELETAR, id)
    }

    @Override
    protected Empresa mapearResultSet(ResultSet resultSet) {
        Empresa empresa = new Empresa(
                resultSet.getString("nome_empresa"),
                resultSet.getString("email"),
                resultSet.getString("cnpj"),
                resultSet.getString("descricao")
        )
        empresa.id = resultSet.getInt("idempresas")
        empresa.enderecoId = resultSet.getObject("endereco_id") as Integer
        return empresa
    }

    private Empresa addEndereco(Empresa empresa) {
        if (empresa.enderecoId) {
            empresa.endereco = enderecoDAO.buscarPorId(empresa.enderecoId)
        }
        return empresa
    }
}
