package com.linketinder.dao

import com.linketinder.database.DatabaseConnection
import com.linketinder.model.Empresa

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.time.LocalDateTime

/**
 * EmpresaDAO - Data Access Object para a entidade Empresa
 *
 * Responsável por todas as operações de banco de dados relacionadas a empresas:
 * - CREATE (inserir)
 * - READ (listar, buscarPorId)
 * - UPDATE (atualizar)
 * - DELETE (deletar)
 */
class EmpresaDAO {

    /**
     * Insere uma nova empresa no banco de dados
     * @param empresa - objeto Empresa a ser inserido
     */
    void inserir(Empresa empresa) {
        // TODO: Implementar inserção de empresa no banco
        // 1. Abrir conexão com o banco
        // 2. Preparar SQL INSERT com PreparedStatement
        // 3. Preencher os parâmetros (nome, email, cnpj, etc)
        // 4. Executar o INSERT
        // 5. Recuperar o ID gerado pelo banco
        // 6. Atribuir o ID ao objeto empresa
        // 7. Fechar conexão
    }

    /**
     * Lista todas as empresas cadastradas no banco
     * @return List<Empresa> - lista com todas as empresas
     */
    List<Empresa> listar() {
        // TODO: Implementar listagem de empresas
        // 1. Abrir conexão
        // 2. Executar SELECT * FROM empresas
        // 3. Percorrer o ResultSet
        // 4. Para cada linha, criar um objeto Empresa
        // 5. Adicionar empresa na lista
        // 6. Fechar conexão
        // 7. Retornar lista
        return []
    }

    /**
     * Busca uma empresa específica pelo ID
     * @param id - ID da empresa
     * @return Empresa - objeto encontrado ou null
     */
    Empresa buscarPorId(Integer id) {
        // TODO: Implementar busca por ID
        // 1. Abrir conexão
        // 2. Preparar SELECT WHERE id = ?
        // 3. Executar query
        // 4. Se encontrou, criar objeto Empresa
        // 5. Fechar conexão
        // 6. Retornar empresa ou null
        return null
    }

    /**
     * Atualiza os dados de uma empresa existente
     * @param empresa - objeto com dados atualizados
     */
    void atualizar(Empresa empresa) {
        // TODO: Implementar atualização de empresa
        // 1. Abrir conexão
        // 2. Preparar UPDATE SET ... WHERE id = ?
        // 3. Preencher parâmetros
        // 4. Executar UPDATE
        // 5. Fechar conexão
    }

    /**
     * Remove uma empresa do banco de dados
     * @param id - ID da empresa a ser removida
     */
    void deletar(Integer id) {
        // TODO: Implementar exclusão de empresa
        // 1. Abrir conexão
        // 2. Preparar DELETE WHERE id = ?
        // 3. Executar DELETE
        // 4. Fechar conexão
    }
}

