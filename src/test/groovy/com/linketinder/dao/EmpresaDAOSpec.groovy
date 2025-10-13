package com.linketinder.dao

import com.linketinder.database.DatabaseConnection
import com.linketinder.model.Empresa
import spock.lang.Specification
import java.sql.Connection

/**
 * EmpresaDAOSpec - Testes TDD para EmpresaDAO
 *
 * Testa todas as operações CRUD de empresas no banco de dados:
 * - Inserir empresa
 * - Listar todas as empresas
 * - Buscar empresa por ID
 * - Atualizar empresa
 * - Deletar empresa
 *
 * IMPORTANTE: Os testes usam o banco real PostgreSQL
 */
class EmpresaDAOSpec extends Specification {

    EmpresaDAO dao
    Connection conn

    def setup() {
        dao = new EmpresaDAO()
        conn = DatabaseConnection.getConnection()

        // Limpar apenas a tabela empresas
        // CASCADE vai deletar automaticamente vagas e curtidas relacionadas
        try {
            conn.createStatement().execute("TRUNCATE TABLE empresas CASCADE")
        } catch (Exception e) {
            // Se TRUNCATE falhar, tenta DELETE simples
            try {
                conn.createStatement().execute("DELETE FROM empresas")
            } catch (Exception ex) {
                // Ignora se a tabela não existir ainda
                println "Aviso: Não foi possível limpar a tabela empresas: ${ex.message}"
            }
        }
    }

    def cleanup() {
        if (conn != null && !conn.isClosed()) {
            conn.close()
        }
    }

    def "deve inserir uma nova empresa no banco de dados"() {
        given: "uma empresa válida"
        def empresa = new Empresa(
            "Tech Solutions",
            "contato@techsolutions.com",
            "12.345.678/0001-90",
            "Brasil",
            "SP",
            "01310-100",
            "Empresa de tecnologia e inovação",
            ["Java", "Spring", "AWS"]
        )

        when: "inserir a empresa no banco"
        dao.inserir(empresa)

        then: "a empresa deve ter um ID gerado"
        empresa.id != null
        empresa.id > 0
    }

    def "deve listar todas as empresas cadastradas"() {
        given: "duas empresas cadastradas no banco"
        def empresa1 = new Empresa(
            "DataCorp",
            "contato@datacorp.com",
            "11.222.333/0001-44",
            "Brasil",
            "RJ",
            "20000-000",
            "Análise de dados e BI",
            ["Python", "SQL", "Tableau"]
        )
        def empresa2 = new Empresa(
            "WebDev SA",
            "contato@webdev.com",
            "55.666.777/0001-88",
            "Brasil",
            "MG",
            "30000-000",
            "Desenvolvimento web",
            ["JavaScript", "React", "Node.js"]
        )

        dao.inserir(empresa1)
        dao.inserir(empresa2)

        when: "listar todas as empresas"
        def empresas = dao.listar()

        then: "deve retornar as 2 empresas cadastradas"
        empresas.size() == 2
        empresas*.nome.containsAll(["DataCorp", "WebDev SA"])
    }

    def "deve buscar uma empresa por ID"() {
        given: "uma empresa cadastrada no banco"
        def empresaOriginal = new Empresa(
            "DesignHub",
            "contato@designhub.com",
            "99.888.777/0001-66",
            "Brasil",
            "PR",
            "80000-000",
            "Agência de design criativo",
            ["Figma", "Adobe", "UI/UX"]
        )
        dao.inserir(empresaOriginal)
        def idGerado = empresaOriginal.id

        when: "buscar a empresa pelo ID"
        def empresaEncontrada = dao.buscarPorId(idGerado)

        then: "deve retornar a empresa correta"
        empresaEncontrada != null
        empresaEncontrada.id == idGerado
        empresaEncontrada.nome == "DesignHub"
        empresaEncontrada.cnpj == "99.888.777/0001-66"
    }

    def "deve retornar null ao buscar empresa inexistente"() {
        given: "um ID que não existe no banco"
        def idInexistente = 99999

        when: "buscar pelo ID inexistente"
        def empresa = dao.buscarPorId(idInexistente)

        then: "deve retornar null"
        empresa == null
    }

    def "deve atualizar os dados de uma empresa"() {
        given: "uma empresa cadastrada no banco"
        def empresa = new Empresa(
            "CodeFactory",
            "contato@codefactory.com",
            "12.312.312/0001-34",
            "Brasil",
            "BA",
            "40000-000",
            "Fábrica de software",
            ["Java", "C#", ".NET"]
        )
        dao.inserir(empresa)

        when: "atualizar os dados da empresa"
        empresa.nome = "CodeFactory Brasil"
        empresa.email = "contato@codefactory.com.br"
        empresa.descricao = "Fábrica de software e consultoria"
        dao.atualizar(empresa)

        and: "buscar a empresa atualizada"
        def empresaAtualizada = dao.buscarPorId(empresa.id)

        then: "os dados devem estar atualizados"
        empresaAtualizada.nome == "CodeFactory Brasil"
        empresaAtualizada.email == "contato@codefactory.com.br"
        empresaAtualizada.descricao == "Fábrica de software e consultoria"
    }

    def "deve deletar uma empresa do banco"() {
        given: "uma empresa cadastrada no banco"
        def empresa = new Empresa(
            "TestLab",
            "contato@testlab.com",
            "77.788.799/0001-00",
            "Brasil",
            "RS",
            "90000-000",
            "Laboratório de testes",
            ["Selenium", "JUnit", "TestNG"]
        )
        dao.inserir(empresa)
        def idParaDeletar = empresa.id

        when: "deletar a empresa"
        dao.deletar(idParaDeletar)

        and: "tentar buscar a empresa deletada"
        def empresaDeletada = dao.buscarPorId(idParaDeletar)

        then: "não deve encontrar a empresa"
        empresaDeletada == null
    }

    def "deve listar empresas criadas com sucesso"() {
        given: "uma empresa cadastrada"
        Empresa empresa = new Empresa(
            "FullStack Corp",
            "contato@fullstack.com",
            "44.455.466/0001-77",
            "Brasil",
            "SC",
            "88000-000",
            "Desenvolvimento full stack",
            ["JavaScript", "TypeScript", "React", "Angular", "Vue"]
        )
        dao.inserir(empresa)

        when: "listar as empresas"
        List<Empresa> empresas = dao.listar()
        Empresa empresaEncontrada = empresas.find { it.id == empresa.id }

        then: "deve retornar a empresa cadastrada"
        empresaEncontrada != null
        empresaEncontrada.nome == "FullStack Corp"
        empresaEncontrada.email == "contato@fullstack.com"
        // Nota: Competências de empresas não são persistidas no banco (apenas vagas têm competências)
    }


