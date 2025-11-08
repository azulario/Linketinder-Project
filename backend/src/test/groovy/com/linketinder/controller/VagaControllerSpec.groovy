package com.linketinder.controller

import com.linketinder.dto.VagaDTO
import com.linketinder.model.Vaga
import com.linketinder.service.VagaService
import spock.lang.Specification

class VagaControllerSpec extends Specification {

    VagaController controller
    VagaService service

    def setup() {
        service = Mock(VagaService)
        controller = new VagaController(service)
    }

    def "deve cadastrar vaga com sucesso"() {
        given: "um DTO válido"
        def dto = new VagaDTO(
            titulo: "Desenvolvedor Java",
            descricao: "Vaga para dev Java Sênior",
            competencias: ["Java", "Spring Boot"],
            empresaId: 1
        )

        and: "a vaga esperada"
        def vaga = new Vaga("Desenvolvedor Java", "Vaga para dev Java Sênior", 1)
        vaga.id = 1
        vaga.competencias = ["Java", "Spring Boot"]

        when: "cadastrar a vaga"
        def resultado = controller.cadastrarVaga(dto)

        then: "deve retornar sucesso"
        resultado.sucesso == true
        resultado.mensagem == "Vaga cadastrada com sucesso"
        resultado.vaga == vaga

        and: "deve ter chamado o service"
        1 * service.cadastrar(dto) >> vaga
    }

    def "deve retornar erro ao cadastrar com dados inválidos"() {
        given: "um DTO inválido"
        def dto = new VagaDTO(
            titulo: "",
            descricao: "",
            competencias: [],
            empresaId: null
        )

        when: "tentar cadastrar"
        def resultado = controller.cadastrarVaga(dto)

        then: "deve retornar erro de validação"
        resultado.sucesso == false
        resultado.mensagem == "Dados inválidos"
        resultado.erros != null
        resultado.erros.size() > 0

        and: "não deve chamar o service"
        0 * service.cadastrar(_)
    }

    def "deve listar todas as vagas com sucesso"() {
        given: "vagas no service"
        def vagas = [
            new Vaga("Dev Java", "Vaga Java", 1),
            new Vaga("Dev Python", "Vaga Python", 2)
        ]

        when: "listar vagas"
        def resultado = controller.listarTodas()

        then: "deve retornar lista com sucesso"
        resultado.sucesso == true
        resultado.vagas.size() == 2

        and: "deve ter chamado o service"
        1 * service.listarTodas() >> vagas
    }

    def "deve retornar mensagem quando não há vagas"() {
        given: "nenhuma vaga no service"
        def vagas = []

        when: "listar vagas"
        def resultado = controller.listarTodas()

        then: "deve retornar mensagem de lista vazia"
        resultado.sucesso == true
        resultado.vagas == []

        and: "deve ter chamado o service"
        1 * service.listarTodas() >> vagas
    }

    def "deve buscar vaga por ID com sucesso"() {
        given: "uma vaga existe"
        def vaga = new Vaga("Dev Java", "Vaga Java", 1)
        vaga.id = 1

        when: "buscar vaga"
        def resultado = controller.buscarPorId(1)

        then: "deve retornar a vaga"
        resultado.sucesso == true
        resultado.vaga == vaga

        and: "deve ter chamado o service"
        1 * service.buscarPorId(1) >> vaga
    }

    def "deve retornar erro quando vaga não existe"() {
        when: "buscar vaga"
        def resultado = controller.buscarPorId(999)

        then: "deve retornar erro"
        resultado.sucesso == false
        resultado.mensagem == "Vaga não encontrada"

        and: "service retorna null"
        1 * service.buscarPorId(999) >> null
    }

    def "deve listar vagas por empresa com sucesso"() {
        given: "vagas de uma empresa"
        def vagas = [
            new Vaga("Dev Java", "Vaga Java", 1),
            new Vaga("Dev Spring", "Vaga Spring", 1)
        ]

        when: "listar vagas por empresa"
        def resultado = controller.listarPorEmpresa(1)

        then: "deve retornar vagas da empresa"
        resultado.sucesso == true
        resultado.vagas.size() == 2

        and: "deve ter chamado o service"
        1 * service.listarPorEmpresa(1) >> vagas
    }

    def "deve tratar exceção ao cadastrar"() {
        given: "um DTO válido"
        def dto = new VagaDTO(
            titulo: "Dev Java",
            descricao: "Vaga Java",
            competencias: ["Java"],
            empresaId: 1
        )

        when: "tentar cadastrar"
        def resultado = controller.cadastrarVaga(dto)

        then: "deve retornar erro"
        resultado.sucesso == false
        resultado.mensagem.contains("Erro ao cadastrar vaga")

        and: "o service lança exceção"
        1 * service.cadastrar(dto) >> { throw new RuntimeException("Erro no banco") }
    }
}

