package com.linketinder.dao

import com.linketinder.database.DatabaseConnection
import com.linketinder.model.Vaga
import com.linketinder.model.Empresa
import com.linketinder.model.Endereco
import spock.lang.Specification
import java.sql.Connection


class VagaDAOSpec extends Specification {

    VagaDAO vagaDao
    EmpresaDAO empresaDao
    Connection conn


    def setup() {
        vagaDao = new VagaDAO()
        empresaDao = new EmpresaDAO()
        conn = DatabaseConnection.getConnection()


        conn.createStatement().execute("DELETE FROM competencias_vagas")
        conn.createStatement().execute("DELETE FROM vagas")
        conn.createStatement().execute("DELETE FROM empresas")
    }

    def cleanup() {
        if (conn != null && !conn.isClosed()) {
            conn.close()
        }
    }

    def "deve inserir uma nova vaga no banco de dados"() {
        given: "uma empresa e uma vaga válida"
        def empresa = new Empresa(
            "TechCorp",
            "contato@techcorp.com",
            "12.345.678/0001-90",
            "Empresa de tecnologia"
        )
        empresa.endereco = new Endereco("Brasil", "SP", "São Paulo", "01234-567")
        empresaDao.inserir(empresa)

        def vaga = new Vaga(
            "Desenvolvedor Java Senior",
            "Vaga para desenvolvedor com experiência em Java",
            ["Java", "Spring", "PostgreSQL"],
            empresa
        )
        vaga.endereco = new Endereco("Brasil", "SP", "São Paulo", "01234-567")
        vaga.empresaId = empresa.id // Garantir que o empresaId está setado

        when: "inserir a vaga no banco"
        vagaDao.inserir(vaga)

        then: "a vaga deve ter um ID gerado"
        vaga.id != null
        vaga.id > 0
    }

    def "deve listar todas as vagas cadastradas"() {
        given: "duas empresas e suas vagas cadastradas no banco"
        def empresa1 = new Empresa(
            "TechCorp",
            "contato@techcorp.com",
            "12.345.678/0001-90",
            "Empresa de tecnologia"
        )
        empresa1.endereco = new Endereco("Brasil", "SP", "São Paulo", "01234-567")
        empresaDao.inserir(empresa1)

        def empresa2 = new Empresa(
            "DataCorp",
            "contato@datacorp.com",
            "98.765.432/0001-10",
            "Empresa de dados"
        )
        empresa2.endereco = new Endereco("Brasil", "RJ", "Rio de Janeiro", "98765-432")
        empresaDao.inserir(empresa2)

        def vaga1 = new Vaga(
            "Desenvolvedor Java",
            "Vaga para Java",
            ["Java", "Spring"],
            empresa1
        )
        vaga1.endereco = new Endereco("Brasil", "SP", "São Paulo", "01234-567")
        vaga1.empresaId = empresa1.id

        def vaga2 = new Vaga(
            "Analista de Dados",
            "Vaga para análise de dados",
            ["Python", "SQL"],
            empresa2
        )
        vaga2.endereco = new Endereco("Brasil", "RJ", "Rio de Janeiro", "98765-432")
        vaga2.empresaId = empresa2.id

        vagaDao.inserir(vaga1)
        vagaDao.inserir(vaga2)

        when: "listar todas as vagas"
        def vagas = vagaDao.listar()

        then: "deve retornar as 2 vagas cadastradas"
        vagas.size() == 2
        vagas*.titulo.containsAll(["Desenvolvedor Java", "Analista de Dados"])
    }

    def "deve listar vagas por empresa"() {
        given: "duas empresas com suas vagas"
        def empresa1 = new Empresa(
            "TechCorp",
            "contato@techcorp.com",
            "12.345.678/0001-90",
            "Empresa de tecnologia"
        )
        empresa1.endereco = new Endereco("Brasil", "SP", "São Paulo", "01234-567")
        empresaDao.inserir(empresa1)

        def empresa2 = new Empresa(
            "DataCorp",
            "contato@datacorp.com",
            "98.765.432/0001-10",
            "Empresa de dados"
        )
        empresa2.endereco = new Endereco("Brasil", "RJ", "Rio de Janeiro", "98765-432")
        empresaDao.inserir(empresa2)

        def vaga1 = new Vaga("Vaga 1 TechCorp", "Descrição 1", ["Java"], empresa1)
        vaga1.empresaId = empresa1.id
        vaga1.endereco = new Endereco("Brasil", "SP", "São Paulo", "01234-567")

        def vaga2 = new Vaga("Vaga 2 TechCorp", "Descrição 2", ["Spring"], empresa1)
        vaga2.empresaId = empresa1.id
        vaga2.endereco = new Endereco("Brasil", "SP", "São Paulo", "01234-567")

        def vaga3 = new Vaga("Vaga 1 DataCorp", "Descrição 3", ["Python"], empresa2)
        vaga3.empresaId = empresa2.id
        vaga3.endereco = new Endereco("Brasil", "RJ", "Rio de Janeiro", "98765-432")

        vagaDao.inserir(vaga1)
        vagaDao.inserir(vaga2)
        vagaDao.inserir(vaga3)

        when: "listar vagas da empresa1"
        def vagasEmpresa1 = vagaDao.listarPorEmpresa(empresa1.id)

        then: "deve retornar apenas as 2 vagas da empresa1"
        vagasEmpresa1.size() == 2
        vagasEmpresa1*.titulo.containsAll(["Vaga 1 TechCorp", "Vaga 2 TechCorp"])
    }

    def "deve buscar uma vaga por ID"() {
        given: "uma vaga cadastrada no banco"
        def empresa = new Empresa(
            "TechCorp",
            "contato@techcorp.com",
            "12.345.678/0001-90",
            "Empresa de tecnologia"
        )
        empresa.endereco = new Endereco("Brasil", "MG", "Belo Horizonte", "01234-567")
        empresaDao.inserir(empresa)

        def vaga = new Vaga(
            "Desenvolvedor Python",
            "Vaga para desenvolvedor Python",
            ["Python", "Django"],
            empresa
        )
        vaga.endereco = new Endereco("Brasil", "MG", "Belo Horizonte", "30000-000")
        vaga.empresaId = empresa.id
        vagaDao.inserir(vaga)

        when: "buscar a vaga pelo ID"
        def vagaEncontrada = vagaDao.buscarPorId(vaga.id)

        then: "deve retornar a vaga correta"
        vagaEncontrada != null
        vagaEncontrada.titulo == "Desenvolvedor Python"
        vagaEncontrada.empresa.nome == "TechCorp"
    }

    def "deve retornar null ao buscar vaga inexistente"() {
        when: "buscar um ID que não existe"
        def vaga = vagaDao.buscarPorId(99999)

        then: "deve retornar null"
        vaga == null
    }

    def "deve atualizar os dados de uma vaga"() {
        given: "uma vaga cadastrada"
        def empresa = new Empresa(
            "TechCorp",
            "contato@techcorp.com",
            "12.345.678/0001-90",
            "Empresa de tecnologia"
        )
        empresa.endereco = new Endereco("Brasil", "SP", "São Paulo", "01234-567")
        empresaDao.inserir(empresa)

        def vaga = new Vaga(
            "Desenvolvedor Junior",
            "Vaga para junior",
            ["Java"],
            empresa
        )
        vaga.endereco = new Endereco("Brasil", "SP", "São Paulo", "01234-567")
        vaga.empresaId = empresa.id
        vagaDao.inserir(vaga)

        when: "atualizar os dados da vaga"
        vaga.titulo = "Desenvolvedor Senior"
        vaga.descricao = "Vaga para senior com experiência"
        vaga.endereco = new Endereco("Brasil", "RJ", "Rio de Janeiro", "20000-000")
        vaga.competencias = ["Java", "Spring", "Microservices"]
        vagaDao.atualizar(vaga)

        and: "buscar a vaga atualizada"
        def vagaAtualizada = vagaDao.buscarPorId(vaga.id)

        then: "os dados devem estar atualizados"
        vagaAtualizada.titulo == "Desenvolvedor Senior"
        vagaAtualizada.descricao == "Vaga para senior com experiência"
        vagaAtualizada.competencias.size() == 3
        vagaAtualizada.competencias.contains("Microservices")
    }

    def "deve deletar uma vaga do banco"() {
        given: "uma vaga cadastrada"
        def empresa = new Empresa(
            "TechCorp",
            "contato@techcorp.com",
            "12.345.678/0001-90",
            "Empresa de tecnologia"
        )
        empresa.endereco = new Endereco("Brasil", "SP", "São Paulo", "01234-567")
        empresaDao.inserir(empresa)

        def vaga = new Vaga(
            "Vaga Temporária",
            "Vaga que será deletada",
            ["Test"],
            empresa
        )
        vaga.empresaId = empresa.id
        vaga.endereco = new Endereco("Brasil", "SP", "São Paulo", "01234-567")
        vagaDao.inserir(vaga)
        def idVaga = vaga.id

        when: "deletar a vaga"
        vagaDao.deletar(idVaga)

        and: "tentar buscar a vaga deletada"
        def vagaDeletada = vagaDao.buscarPorId(idVaga)

        then: "não deve encontrar a vaga"
        vagaDeletada == null
    }

    def "deve listar vagas com suas competências"() {
        given: "uma vaga com múltiplas competências"
        def empresa = new Empresa(
            "TechCorp",
            "contato@techcorp.com",
            "12.345.678/0001-90",
            "Empresa de tecnologia"
        )
        empresa.endereco = new Endereco("Brasil", "SP", "São Paulo", "01234-567")
        empresaDao.inserir(empresa)

        def vaga = new Vaga(
            "Arquiteto de Software",
            "Vaga para arquiteto",
            ["Java", "Microservices", "AWS", "Docker", "Kubernetes"],
            empresa
        )
        vaga.endereco = new Endereco("Brasil", "SP", "São Paulo", "01234-567")
        vaga.empresaId = empresa.id
        vagaDao.inserir(vaga)

        when: "listar as vagas"
        def vagas = vagaDao.listar()

        then: "deve retornar as competências corretamente"
        vagas.size() == 1
        def vagaEncontrada = vagas[0]
        vagaEncontrada.competencias.size() == 5
        vagaEncontrada.competencias.containsAll(["Java", "Microservices", "AWS", "Docker", "Kubernetes"])
    }

    def "não deve inserir vaga sem empresa"() {
        given: "uma vaga sem empresa válida"
        def vaga = new Vaga(
            "Vaga Inválida",
            "Vaga sem empresa",
            ["Java"],
            null
        )

        when: "tentar inserir a vaga"
        vagaDao.inserir(vaga)

        then: "deve lançar uma exceção"
        thrown(Exception)
    }
}
