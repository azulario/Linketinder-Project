package com.linketinder.dao

import com.linketinder.database.DatabaseConnection
import com.linketinder.model.Vaga
import com.linketinder.model.Empresa

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.time.LocalDateTime

class VagaDAO {

    private static final String SQL_INSERIR = """
        INSERT INTO vagas (nome_vaga, descricao, empresa_id, endereco_id, criado_em)
        VALUES (?, ?, ?, ?, ?)
    """
    private static final String SQL_LISTAR = "SELECT * FROM vagas ORDER BY idVagas"
    private static final String SQL_LISTAR_POR_EMPRESA = "SELECT * FROM vagas WHERE empresa_id = ? ORDER BY idVagas"
    private static final String SQL_BUSCAR_POR_ID = "SELECT * FROM vagas WHERE idVagas = ?"
    private static final String SQL_ATUALIZAR = """
        UPDATE vagas 
        SET nome_vaga = ?, descricao = ?, empresa_id = ?, endereco_id = ?
        WHERE idVagas = ?
    """
    private static final String SQL_DELETAR = "DELETE FROM vagas WHERE idVagas = ?"
    private static final String SQL_INSERIR_COMPETENCIA = "INSERT INTO competencias_vagas (competencia_id, vaga_id) VALUES (?, ?)"
    private static final String SQL_DELETAR_COMPETENCIAS = "DELETE FROM competencias_vagas WHERE vaga_id = ?"

    private final EmpresaDAO empresaDAO = new EmpresaDAO()
    private final CompetenciaDAO competenciaDAO = new CompetenciaDAO()
    private final EnderecoDAO enderecoDAO = new EnderecoDAO()

    /**
     * Insere uma nova vaga no banco de dados
     * @param vaga - objeto Vaga a ser inserido
     */
    void inserir(Vaga vaga) {
        Connection conn = null
        PreparedStatement statement = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()

            Integer enderecoId = null
            if (vaga.endereco) {
                enderecoId = enderecoDAO.buscarOuCriar(vaga.endereco)
                vaga.enderecoId = enderecoId
            }

            statement = conn.prepareStatement(SQL_INSERIR, Statement.RETURN_GENERATED_KEYS)

            statement.setString(1, vaga.titulo)
            statement.setString(2, vaga.descricao)
            statement.setInt(3, vaga.empresaId)
            statement.setObject(4, enderecoId)
            statement.setObject(5, LocalDateTime.now())

            statement.executeUpdate()

            resultSet = statement.getGeneratedKeys()
            if (resultSet.next()) {
                vaga.id = resultSet.getInt(1)
            }

            inserirCompetencias(vaga.id, vaga.competencias, conn)

        } catch (Exception e) {
            throw new RuntimeException("Erro ao inserir vaga: ${e.message}", e)
        } finally {
            DatabaseConnection.closeResources(conn, statement, resultSet)
        }
    }

    List<Vaga> listar() {
        List<Vaga> vagas = []
        Connection conn = null
        Statement statement = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.createStatement()
            resultSet = statement.executeQuery(SQL_LISTAR)

            while (resultSet.next()) {
                Vaga vaga = mapearVaga(resultSet, conn)
                vaga.competencias = competenciaDAO.buscarPorVaga(vaga.id, conn)

                if (vaga.enderecoId) {
                    vaga.endereco = enderecoDAO.buscarPorId(vaga.enderecoId)
                }

                vagas << vaga
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar vagas: ${e.message}", e)
        } finally {
            DatabaseConnection.closeResources(conn, statement, resultSet)
        }
        return vagas
    }

    List<Vaga> listarPorEmpresa(Integer empresaId) {
        List<Vaga> vagas = []
        Connection conn = null
        PreparedStatement statement = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(SQL_LISTAR_POR_EMPRESA)
            statement.setInt(1, empresaId)
            resultSet = statement.executeQuery()

            while (resultSet.next()) {
                Vaga vaga = mapearVaga(resultSet, conn)
                vaga.competencias = competenciaDAO.buscarPorVaga(vaga.id, conn)

                if (vaga.enderecoId) {
                    vaga.endereco = enderecoDAO.buscarPorId(vaga.enderecoId)
                }

                vagas << vaga
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar vagas por empresa: ${e.message}", e)
        } finally {
            DatabaseConnection.closeResources(conn, statement, resultSet)
        }
        return vagas
    }


    Vaga buscarPorId(Integer id) {
        Vaga vaga = null
        Connection conn = null
        PreparedStatement statement = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()
            statement = conn.prepareStatement(SQL_BUSCAR_POR_ID)
            statement.setInt(1, id)
            resultSet = statement.executeQuery()

            if (resultSet.next()) {
                vaga = mapearVaga(resultSet, conn)
                vaga.competencias = competenciaDAO.buscarPorVaga(vaga.id, conn)

                if (vaga.enderecoId) {
                    vaga.endereco = enderecoDAO.buscarPorId(vaga.enderecoId)
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar vaga por ID: ${e.message}", e)
        } finally {
            DatabaseConnection.closeResources(conn, statement, resultSet)
        }
        return vaga
    }

    void atualizar(Vaga vaga) {
        Connection conn = null
        PreparedStatement statement = null

        try {
            conn = DatabaseConnection.getConnection()

            Integer enderecoId = vaga.enderecoId
            if (vaga.endereco) {
                enderecoId = enderecoDAO.buscarOuCriar(vaga.endereco)
                vaga.enderecoId = enderecoId
            }

            statement = conn.prepareStatement(SQL_ATUALIZAR)

            statement.setString(1, vaga.titulo)
            statement.setString(2, vaga.descricao)
            statement.setInt(3, vaga.empresaId)
            statement.setObject(4, enderecoId)
            statement.setInt(5, vaga.id)

            statement.executeUpdate()

            atualizarCompetencias(vaga.id, vaga.competencias, conn)

        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar vaga: ${e.message}", e)
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
            throw new RuntimeException("Erro ao deletar vaga: ${e.message}", e)
        } finally {
            DatabaseConnection.closeResources(conn, statement, null)
        }
    }


    private Vaga mapearVaga(ResultSet rs, Connection conn) {
        Integer vagaId = rs.getInt("idvagas")
        Integer empresaId = rs.getInt("empresa_id")

        // Buscar empresa
        Empresa empresa = buscarEmpresaPorId(empresaId, conn)

        Vaga vaga = new Vaga(
            rs.getString("nome_vaga"),
            rs.getString("descricao"),
            [], // competências serão carregadas separadamente
            empresa
        )

        vaga.id = vagaId
        vaga.enderecoId = rs.getObject("endereco_id") as Integer
        vaga.criadoEm = rs.getTimestamp("criado_em")?.toLocalDateTime()

        return vaga
    }


    private Empresa buscarEmpresaPorId(Integer empresaId, Connection conn) {
        String sql = "SELECT * FROM empresas WHERE idEmpresas = ?"
        PreparedStatement statement = null
        ResultSet rs = null

        try {
            statement = conn.prepareStatement(sql)
            statement.setInt(1, empresaId)
            rs = statement.executeQuery()

            if (rs.next()) {
                Empresa empresa = new Empresa(
                    rs.getString("nome_empresa"),
                    rs.getString("email"),
                    rs.getString("cnpj"),
                    rs.getString("descricao")
                )
                empresa.id = empresaId
                empresa.enderecoId = rs.getObject("endereco_id") as Integer
                empresa.senha = rs.getString("senha")
                empresa.criadoEm = rs.getTimestamp("criado_em")?.toLocalDateTime()

                if (empresa.enderecoId) {
                    empresa.endereco = enderecoDAO.buscarPorId(empresa.enderecoId)
                }

                return empresa
            }
        } finally {
            rs?.close()
            statement?.close()
        }

        return null
    }

    private void inserirCompetencias(Integer vagaId, List<String> competencias, Connection conn) {
        if (!competencias) return

        competencias.each { competencia ->
            Integer competenciaId = competenciaDAO.buscarOuCriar(competencia, conn)

            PreparedStatement compStmt = conn.prepareStatement(SQL_INSERIR_COMPETENCIA)
            compStmt.setInt(1, competenciaId)
            compStmt.setInt(2, vagaId)
            compStmt.executeUpdate()
            compStmt.close()
        }
    }

    private void atualizarCompetencias(Integer vagaId, List<String> competencias, Connection conn) {
        deletarCompetencias(vagaId, conn)
        inserirCompetencias(vagaId, competencias, conn)
    }

    private void deletarCompetencias(Integer vagaId, Connection conn) {
        PreparedStatement statement = conn.prepareStatement(SQL_DELETAR_COMPETENCIAS)
        statement.setInt(1, vagaId)
        statement.executeUpdate()
        statement.close()
    }
}
