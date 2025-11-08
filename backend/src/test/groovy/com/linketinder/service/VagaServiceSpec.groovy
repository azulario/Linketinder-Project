package com.linketinder.service

import com.linketinder.dao.EmpresaDAO
import com.linketinder.dao.VagaDAO
import com.linketinder.dto.VagaDTO
import com.linketinder.model.Empresa
import com.linketinder.model.Vaga
import spock.lang.Specification

class VagaServiceSpec extends Specification {

    VagaService service
    VagaDAO vagaDAO
    EmpresaDAO empresaDAO

    def setup() {
        vagaDAO = Mock(VagaDAO)
        empresaDAO = Mock(EmpresaDAO)
        service = new VagaService(vagaDAO, empresaDAO)
    }

    def "deve cadastrar uma vaga com sucesso"() {
        given: "um DTO de vaga válido"
        def dto = new VagaDTO(
            titulo: "Desenvolvedor Java",
            descricao: "Vaga para desenvolvedor Java Sênior",
            competencias: ["Java", "Spring Boot", "SQL"],
            empresaId: 1
        )

        and: "uma empresa existente"
        def empresa = new Empresa("Tech Corp", "tech@email.com", "12.345.678/0001-90", "Tech")
        empresa.id = 1

        when: "cadastrar a vaga"
        def resultado = service.cadastrar(dto)

        then: "deve retornar a vaga cadastrada"
        resultado != null
        resultado.titulo == "Desenvolvedor Java"
        resultado.descricao == "Vaga para desenvolvedor Java Sênior"
        resultado.empresaId == 1
        resultado.competencias.size() == 3

        and: "deve ter validado a empresa e inserido a vaga"
        1 * empresaDAO.buscarPorId(1) >> empresa
        1 * vagaDAO.inserir(_ as Vaga) >> { Vaga v -> v.id = 10; return null }
    }

    def "deve listar todas as vagas"() {
        given: "uma lista de vagas no DAO"
        def vagas = [
            new Vaga("Dev Java", "Vaga Java", 1),
            new Vaga("Dev Python", "Vaga Python", 2)
        ]

        when: "listar todas as vagas"
        def resultado = service.listarTodas()

        then: "deve retornar a lista de vagas"
        resultado.size() == 2
        resultado[0].titulo == "Dev Java"
        resultado[1].titulo == "Dev Python"

        and: "deve ter chamado o DAO"
        1 * vagaDAO.listar() >> vagas
    }

    def "deve buscar vaga por ID"() {
        given: "uma vaga no banco"
        def vaga = new Vaga("Dev Java", "Vaga Java", 1)
        vaga.id = 1

        when: "buscar por ID"
        def resultado = service.buscarPorId(1)

        then: "deve retornar a vaga"
        resultado != null
        resultado.id == 1
        resultado.titulo == "Dev Java"

        and: "deve ter chamado o DAO"
        1 * vagaDAO.buscarPorId(1) >> vaga
    }

    def "deve listar vagas por empresa"() {
        given: "vagas de uma empresa específica"
        def vagas = [
            new Vaga("Dev Java", "Vaga Java", 1),
            new Vaga("Dev Spring", "Vaga Spring", 1)
        ]

        when: "listar vagas por empresa"
        def resultado = service.listarPorEmpresa(1)

        then: "deve retornar apenas vagas da empresa"
        resultado.size() == 2
        resultado.every { it.empresaId == 1 }

        and: "deve ter chamado o DAO"
        1 * vagaDAO.listarPorEmpresa(1) >> vagas
    }

    def "deve lançar exceção ao cadastrar vaga com empresa inexistente"() {
        given: "um DTO de vaga válido mas empresa inexistente"
        def dto = new VagaDTO(
            titulo: "Desenvolvedor Java",
            descricao: "Vaga para desenvolvedor Java Sênior",
            competencias: ["Java"],
            empresaId: 999
        )

        when: "tentar cadastrar a vaga"
        service.cadastrar(dto)

        then: "deve lançar exceção"
        def exception = thrown(IllegalArgumentException)
        exception.message.contains("Empresa não encontrada")

        and: "deve ter tentado buscar a empresa"
        1 * empresaDAO.buscarPorId(999) >> null
        0 * vagaDAO.inserir(_)
    }
}

