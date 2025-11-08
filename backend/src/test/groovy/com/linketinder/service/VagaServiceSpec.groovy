package com.linketinder.service

import com.linketinder.dao.VagaDAO
import com.linketinder.dto.VagaDTO
import com.linketinder.model.Vaga
import spock.lang.Specification

class VagaServiceSpec extends Specification {

    VagaService service
    VagaDAO vagaDAO

    def setup() {
        vagaDAO = Mock(VagaDAO)

        service = new VagaService()
        service.vagaDAO = vagaDAO
    }

    def "deve cadastrar uma vaga com sucesso"() {
        given: "um DTO de vaga válido"
        def dto = new VagaDTO(
            titulo: "Desenvolvedor Java",
            descricao: "Vaga para desenvolvedor Java Sênior",
            competencias: ["Java", "Spring Boot", "SQL"],
            empresaId: 1
        )

        and: "o vagaDAO insere com sucesso"
        vagaDAO.inserir(_ as Vaga) >> { Vaga v ->
            v.id = 10
        }

        when: "cadastrar a vaga"
        def resultado = service.cadastrar(dto)

        then: "deve retornar a vaga cadastrada"
        resultado != null
        resultado.titulo == "Desenvolvedor Java"
        resultado.descricao == "Vaga para desenvolvedor Java Sênior"
        resultado.empresaId == 1
        resultado.competencias.size() == 3

        and: "deve ter chamado o DAO"
        1 * vagaDAO.inserir(_ as Vaga)
    }

    def "deve listar todas as vagas"() {
        given: "uma lista de vagas no DAO"
        def vagas = [
            new Vaga("Dev Java", "Vaga Java", 1),
            new Vaga("Dev Python", "Vaga Python", 2)
        ]
        vagaDAO.listar() >> vagas

        when: "listar todas as vagas"
        def resultado = service.listarTodas()

        then: "deve retornar a lista de vagas"
        resultado.size() == 2
        resultado[0].titulo == "Dev Java"
        resultado[1].titulo == "Dev Python"

        and: "deve ter chamado o DAO"
        1 * vagaDAO.listar()
    }

    def "deve buscar vaga por ID"() {
        given: "uma vaga no banco"
        def vaga = new Vaga("Dev Java", "Vaga Java", 1)
        vaga.id = 1
        vagaDAO.buscarPorId(1) >> vaga

        when: "buscar por ID"
        def resultado = service.buscarPorId(1)

        then: "deve retornar a vaga"
        resultado != null
        resultado.id == 1
        resultado.titulo == "Dev Java"

        and: "deve ter chamado o DAO"
        1 * vagaDAO.buscarPorId(1)
    }

    def "deve listar vagas por empresa"() {
        given: "vagas de uma empresa específica"
        def vagas = [
            new Vaga("Dev Java", "Vaga Java", 1),
            new Vaga("Dev Spring", "Vaga Spring", 1)
        ]
        vagaDAO.listarPorEmpresa(1) >> vagas

        when: "listar vagas por empresa"
        def resultado = service.listarPorEmpresa(1)

        then: "deve retornar apenas vagas da empresa"
        resultado.size() == 2
        resultado.every { it.empresaId == 1 }

        and: "deve ter chamado o DAO"
        1 * vagaDAO.listarPorEmpresa(1)
    }
}

