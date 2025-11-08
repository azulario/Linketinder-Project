package com.linketinder.dao

import com.linketinder.database.DatabaseConnection
import com.linketinder.model.Empresa
import com.linketinder.model.Endereco
import spock.lang.Specification
import java.sql.Connection


class EmpresaDAOSpec extends Specification {

    EmpresaDAO dao
    Connection conn

    def setup() {
        dao = new EmpresaDAO()
        conn = DatabaseConnection.getConnection()

        try {
            conn.createStatement().execute("TRUNCATE TABLE empresas CASCADE")
        } catch (Exception e) {

            try {
                conn.createStatement().execute("DELETE FROM empresas")
            } catch (Exception ex) {

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
        Empresa empresa = new Empresa(
            "Tech Solutions",
            "contato@techsolutions.com",
            "12.345.678/0001-90",
            "Empresa de tecnologia e inovação"
        )
        empresa.endereco = new Endereco("Brasil", "SP", "São Paulo", "01310-100")

        when: "inserir a empresa no banco"
        dao.inserir(empresa)

        then: "a empresa deve ter um ID gerado"
        empresa.id != null
        empresa.id > 0
    }

    def "deve listar todas as empresas cadastradas"() {
        given: "duas empresas cadastradas no banco"
        Empresa empresa1 = new Empresa(
            "DataCorp",
            "contato@datacorp.com",
            "11.222.333/0001-44",
            "Análise de dados e BI"
        )
        empresa1.endereco = new Endereco("Brasil", "RJ", "Rio de Janeiro", "20000-000")

        Empresa empresa2 = new Empresa(
            "WebDev SA",
            "contato@webdev.com",
            "55.666.777/0001-88",
            "Desenvolvimento web"
        )
        empresa2.endereco = new Endereco("Brasil", "MG", "Belo Horizonte", "30000-000")

        dao.inserir(empresa1)
        dao.inserir(empresa2)

        when: "listar todas as empresas"
        List<Empresa> empresas = dao.listar()

        then: "deve retornar as 2 empresas cadastradas"
        empresas.size() == 2
        empresas*.nome.containsAll(["DataCorp", "WebDev SA"])
    }

    def "deve buscar uma empresa por ID"() {
        given: "uma empresa cadastrada no banco"
        Empresa empresa = new Empresa(
            "CloudTech",
            "contato@cloudtech.com",
            "99.888.777/0001-66",
            "Soluções em cloud"
        )
        empresa.endereco = new Endereco("Brasil", "SP", "Campinas", "13000-000")
        dao.inserir(empresa)

        when: "buscar a empresa pelo ID"
        Empresa empresaEncontrada = dao.buscarPorId(empresa.id)

        then: "deve retornar a empresa correta"
        empresaEncontrada != null
        empresaEncontrada.id == empresa.id
        empresaEncontrada.nome == "CloudTech"
        empresaEncontrada.cnpj == "99.888.777/0001-66"
    }

    def "deve retornar null ao buscar empresa inexistente"() {
        when: "buscar uma empresa com ID inexistente"
        Empresa empresa = dao.buscarPorId(99999)

        then: "deve retornar null"
        empresa == null
    }

    def "deve atualizar os dados de uma empresa"() {
        given: "uma empresa cadastrada no banco"
        Empresa empresa = new Empresa(
            "OldName Corp",
            "old@email.com",
            "11.111.111/0001-11",
            "Descrição antiga"
        )
        empresa.endereco = new Endereco("Brasil", "RJ", "Rio de Janeiro", "20000-000")
        dao.inserir(empresa)

        when: "atualizar os dados da empresa"
        empresa.nome = "NewName Corp"
        empresa.email = "new@email.com"
        empresa.descricao = "Descrição nova"
        empresa.endereco = new Endereco("Brasil", "SP", "São Paulo", "01000-000")
        dao.atualizar(empresa)

        and: "buscar a empresa atualizada"
        Empresa empresaAtualizada = dao.buscarPorId(empresa.id)

        then: "os dados devem estar atualizados"
        empresaAtualizada.nome == "NewName Corp"
        empresaAtualizada.email == "new@email.com"
        empresaAtualizada.descricao == "Descrição nova"
    }

    def "deve deletar uma empresa do banco"() {
        given: "uma empresa cadastrada no banco"
        Empresa empresa = new Empresa(
            "ToDelete Inc",
            "delete@company.com",
            "22.222.222/0001-22",
            "Empresa para deletar"
        )
        empresa.endereco = new Endereco("Brasil", "BA", "Salvador", "40000-000")
        dao.inserir(empresa)
        Integer empresaId = empresa.id

        when: "deletar a empresa"
        dao.deletar(empresaId)

        and: "tentar buscar a empresa deletada"
        Empresa empresaDeletada = dao.buscarPorId(empresaId)

        then: "não deve encontrar a empresa"
        empresaDeletada == null
    }

    def "deve listar empresas criadas com sucesso"() {
        given: "duas empresas inseridas"
        Empresa emp1 = new Empresa(
            "Empresa A",
            "a@empresa.com",
            "10.000.000/0001-10",
            "Descrição A"
        )
        emp1.endereco = new Endereco("Brasil", "SP", "São Paulo", "01000-000")

        Empresa emp2 = new Empresa(
            "Empresa B",
            "b@empresa.com",
            "20.000.000/0001-20",
            "Descrição B"
        )
        emp2.endereco = new Endereco("Brasil", "RJ", "Rio de Janeiro", "20000-000")

        dao.inserir(emp1)
        dao.inserir(emp2)

        when: "listar empresas"
        List<Empresa> lista = dao.listar()

        then: "deve ter 2 empresas"
        lista.size() == 2
    }

    def "não deve inserir empresa com CNPJ nulo"() {
        given: "uma empresa sem CNPJ"
        Empresa empresa = new Empresa(
            "Sem CNPJ",
            "email@test.com",
            null,
            "Descrição"
        )
        empresa.endereco = new Endereco("Brasil", "SP", "São Paulo", "01000-000")

        when: "tentar inserir a empresa"
        dao.inserir(empresa)

        then: "deve lançar exceção"
        thrown(RuntimeException)
    }
}
