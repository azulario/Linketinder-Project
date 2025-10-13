package com.linketinder.dao

import com.linketinder.database.DatabaseConnection
import com.linketinder.model.Vaga
import com.linketinder.model.Empresa

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.time.LocalDateTime

/**
 * VagaDAO - Data Access Object para a entidade Vaga
 *
 * Responsável por todas as operações de banco de dados relacionadas a vagas:
 * - CREATE (inserir)
 * - READ (listar, listarPorEmpresa, buscarPorId)
 * - UPDATE (atualizar)
 * - DELETE (deletar)
 */
class VagaDAO {

    /**
     * Insere uma nova vaga no banco de dados
     * @param vaga - objeto Vaga a ser inserido
     */
    void inserir(Vaga vaga) {
        // TODO: Implementar inserção de vaga no banco
        // 1. Abrir conexão com o banco
        // 2. Preparar SQL INSERT com PreparedStatement
        // 3. Preencher os parâmetros (titulo, descricao, empresa_id, etc)
        // 4. Executar o INSERT
        // 5. Recuperar o ID gerado pelo banco
        // 6. Atribuir o ID ao objeto vaga
        // 7. ATENÇÃO: Inserir competências na tabela N:N competencias_vagas
        // 8. Fechar conexão


    }

    /**
     * Lista todas as vagas cadastradas no banco
     * @return List<Vaga> - lista com todas as vagas
     */
    List<Vaga> listar() {
        // TODO: Implementar listagem de vagas
        // 1. Abrir conexão
        // 2. Executar SELECT * FROM vagas JOIN empresas
        // 3. Percorrer o ResultSet
        // 4. Para cada linha, criar um objeto Vaga
        // 5. Buscar competências da vaga (tabela N:N)
        // 6. Adicionar vaga na lista
        // 7. Fechar conexão
        // 8. Retornar lista
        return []
    }

    /**
     * Lista todas as vagas de uma empresa específica
     * @param empresaId - ID da empresa
     * @return List<Vaga> - lista de vagas da empresa
     */
    List<Vaga> listarPorEmpresa(Integer empresaId) {
        // TODO: Implementar listagem de vagas por empresa
        // 1. Abrir conexão
        // 2. Preparar SELECT WHERE empresa_id = ?
        // 3. Executar query
        // 4. Percorrer ResultSet e criar objetos Vaga
        // 5. Fechar conexão
        // 6. Retornar lista
        return []
    }

    /**
     * Busca uma vaga específica pelo ID
     * @param id - ID da vaga
     * @return Vaga - objeto encontrado ou null
     */
    Vaga buscarPorId(Integer id) {
        // TODO: Implementar busca por ID
        // 1. Abrir conexão
        // 2. Preparar SELECT WHERE id = ?
        // 3. Executar query
        // 4. Se encontrou, criar objeto Vaga
        // 5. Buscar competências da vaga
        // 6. Fechar conexão
        // 7. Retornar vaga ou null
        return null
    }

    /**
     * Atualiza os dados de uma vaga existente
     * @param vaga - objeto com dados atualizados
     */
    void atualizar(Vaga vaga) {
        // TODO: Implementar atualização de vaga
        // 1. Abrir conexão
        // 2. Preparar UPDATE SET ... WHERE id = ?
        // 3. Preencher parâmetros
        // 4. Executar UPDATE
        // 5. Atualizar competências (deletar antigas e inserir novas)
        // 6. Fechar conexão
    }

    /**
     * Remove uma vaga do banco de dados
     * @param id - ID da vaga a ser removida
     */
    void deletar(Integer id) {
        // TODO: Implementar exclusão de vaga
        // 1. Abrir conexão
        // 2. Deletar competências associadas (tabela N:N)
        // 3. Preparar DELETE WHERE id = ?
        // 4. Executar DELETE
        // 5. Fechar conexão
    }
}
package com.linketinder.dao

import com.linketinder.database.DatabaseConnection
import com.linketinder.model.Candidato

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * CandidatoDAO - Data Access Object para a entidade Candidato
 *
 * Responsável por todas as operações de banco de dados relacionadas a candidatos:
 * - CREATE (inserir)
 * - READ (listar, buscarPorId)
 * - UPDATE (atualizar)
 * - DELETE (deletar)
 */
class CandidatoDAO {

    /**
     * Insere um novo candidato no banco de dados
     * @param candidato - objeto Candidato a ser inserido
     */
    void inserir(Candidato candidato) {
        // TODO: Implementar inserção de candidato no banco
        // 1. Abrir conexão com o banco
        // 2. Preparar SQL INSERT com PreparedStatement
        // 3. Preencher os parâmetros (nome, email, cpf, etc)
        // 4. Executar o INSERT
        // 5. Recuperar o ID gerado pelo banco
        // 6. Atribuir o ID ao objeto candidato
        // 7. Fechar conexão
    }

    /**
     * Lista todos os candidatos cadastrados no banco
     * @return List<Candidato> - lista com todos os candidatos
     */
    List<Candidato> listar() {
        // TODO: Implementar listagem de candidatos
        // 1. Abrir conexão
        // 2. Executar SELECT * FROM candidatos
        // 3. Percorrer o ResultSet
        // 4. Para cada linha, criar um objeto Candidato
        // 5. Adicionar candidato na lista
        // 6. Fechar conexão
        // 7. Retornar lista
        return []
    }

    /**
     * Busca um candidato específico pelo ID
     * @param id - ID do candidato
     * @return Candidato - objeto encontrado ou null
     */
    Candidato buscarPorId(Integer id) {
        // TODO: Implementar busca por ID
        // 1. Abrir conexão
        // 2. Preparar SELECT WHERE id = ?
        // 3. Executar query
        // 4. Se encontrou, criar objeto Candidato
        // 5. Fechar conexão
        // 6. Retornar candidato ou null
        return null
    }

    /**
     * Atualiza os dados de um candidato existente
     * @param candidato - objeto com dados atualizados
     */
    void atualizar(Candidato candidato) {
        // TODO: Implementar atualização de candidato
        // 1. Abrir conexão
        // 2. Preparar UPDATE SET ... WHERE id = ?
        // 3. Preencher parâmetros
        // 4. Executar UPDATE
        // 5. Fechar conexão
    }

    /**
     * Remove um candidato do banco de dados
     * @param id - ID do candidato a ser removido
     */
    void deletar(Integer id) {
        // TODO: Implementar exclusão de candidato
        // 1. Abrir conexão
        // 2. Preparar DELETE WHERE id = ?
        // 3. Executar DELETE
        // 4. Fechar conexão
    }
}

