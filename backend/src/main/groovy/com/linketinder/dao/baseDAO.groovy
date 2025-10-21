package com.linketinder.dao

import com.linketinder.database.DatabaseConnection

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

abstract class BaseDAO<T> {
    protected abstract T mapearResultSet(ResultSet resultSet)

    protected List<T> executarConsulta(String sql, Object... parametros) {
        List<T> resultados = []
        Connection conn = null
        PreparedStatement pstmt = null
        ResultSet resultSet = null

        try {
            conn = DatabaseConnection.getConnection()

            pstmt = conn.prepareStatement(sql)

            parametros.eachWithIndex{ param, index ->
                pstmt.setObject(index + 1, param)
            }

            resultSet = pstmt.executeQuery()

            while (resultSet.next()) {
                resultados << mapearResultSet(resultSet)
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao executar consulta: " + e.message, e)
        } finally {
            DatabaseConnection.closeResources(conn, pstmt, resultSet)
        }

        return resultados
    }
}

protected T buscarUmObjeto(String sql, Object... parametros) {
    List<T> resultados = executarConsulta(sql, parametros)
    return resultados.isEmpty() ? null : resultados[0]
}

protected Integer executarInsert(String sql, Object... parametros) {
    Connection conn = null
    PreparedStatement pstmt = null
    ResultSet resultSet = null

    try {
        conn = DatabaseConnection.getConnection()

        pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

        parametros.eachWithIndex{ param, index ->
            pstmt.setObject(index + 1, param)
        }

        pstmt.executeUpdate()

        resultSet = pstmt.getGeneratedKeys()
        return resultSet() ? resultSet.getInt(1) : null
    } catch (Exception e) {
        throw new RuntimeException("Erro ao executar insert: " + e.message, e)
    } finally {
        DatabaseConnection.closeResources(conn, pstmt, resultSet)
    }

}

protected void executarUpdate(String sql, Object... parametros) {
    Connection conn = null
    PreparedStatement pstmt = null

    try {
        conn = DatabaseConnection.getConnection()

        pstmt = conn.prepareStatement(sql)

        parametros.eachWithIndex{ param, index ->
            pstmt.setObject(index + 1, param)
        }

        pstmt.executeUpdate()

    } catch (Exception e) {
        throw new RuntimeException("Erro ao executar update: " + e.message, e)
    } finally {
        DatabaseConnection.closeResources(conn, pstmt, null)
    }
}