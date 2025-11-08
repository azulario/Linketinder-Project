package com.linketinder.controller

import com.linketinder.dto.EmpresaDTO
import com.linketinder.model.Empresa
import com.linketinder.service.EmpresaService
import spock.lang.Specification

class EmpresaControllerSpec extends Specification {

    EmpresaController controller
    EmpresaService service

    def setup() {
        service = Mock(EmpresaService)
        controller = new EmpresaController(service)
    }

    def "deve cadastrar empresa com sucesso"() {
        given: "um DTO válido"
        def dto = new EmpresaDTO(
            nome: "Tech Corp",
            email: "contato@techcorp.com",
            cnpj: "12.345.678/0001-90",
            descricao: "Empresa de tecnologia",
            pais: "Brasil",
            estado: "SP",
            cidade: "São Paulo",
            cep: "01234-567"
        )

        and: "o empresa esperada"
        def empresa = new Empresa("Tech Corp", "contato@techcorp.com", "12.345.678/0001-90",
            "Empresa de tecnologia")
        empresa.id = 1

        when: "cadastrar a empresa"
        def resultado = controller.cadastrarEmpresa(dto)

        then: "deve retornar sucesso"
        resultado.sucesso == true
        resultado.mensagem == "Empresa cadastrada com sucesso"
        resultado.empresa == empresa

        and: "deve ter chamado o service"
        1 * service.cadastrar(dto) >> empresa
    }

    def "deve retornar erro ao cadastrar com dados inválidos"() {
        given: "um DTO inválido"
        def dto = new EmpresaDTO(
            nome: "",
            email: "email-invalido",
            cnpj: "123",
            descricao: "",
            pais: "",
            estado: "",
            cidade: "",
            cep: ""
        )

        when: "tentar cadastrar"
        def resultado = controller.cadastrarEmpresa(dto)

        then: "deve retornar erro de validação"
        resultado.sucesso == false
        resultado.mensagem == "Dados inválidos"
        resultado.erros != null
        resultado.erros.size() > 0

        and: "não deve chamar o service"
        0 * service.cadastrar(_)
    }

    def "deve listar todas as empresas com sucesso"() {
        given: "empresas no service"
        def empresas = [
            new Empresa("Tech Corp", "tech@email.com", "12.345.678/0001-90", "Tech"),
            new Empresa("Dev Solutions", "dev@email.com", "98.765.432/0001-10", "Consultoria")
        ]

        when: "listar empresas"
        def resultado = controller.listarTodas()

        then: "deve retornar lista com sucesso"
        resultado.sucesso == true
        resultado.empresas.size() == 2

        and: "deve ter chamado o service"
        1 * service.listarTodas() >> empresas
    }

    def "deve retornar mensagem quando não há empresas"() {
        given: "nenhuma empresa no service"
        def empresas = []

        when: "listar empresas"
        def resultado = controller.listarTodas()

        then: "deve retornar mensagem de lista vazia"
        resultado.sucesso == true
        resultado.empresas == []

        and: "deve ter chamado o service"
        1 * service.listarTodas() >> empresas
    }

    def "deve buscar empresa por ID com sucesso"() {
        given: "uma empresa existe"
        def empresa = new Empresa("Tech Corp", "tech@email.com", "12.345.678/0001-90", "Tech")
        empresa.id = 1

        when: "buscar empresa"
        def resultado = controller.buscarPorId(1)

        then: "deve retornar a empresa"
        resultado.sucesso == true
        resultado.empresa == empresa

        and: "deve ter chamado o service"
        1 * service.buscarPorId(1) >> empresa
    }

    def "deve retornar erro quando empresa não existe"() {
        when: "buscar empresa"
        def resultado = controller.buscarPorId(999)

        then: "deve retornar erro"
        resultado.sucesso == false
        resultado.mensagem == "Empresa não encontrada"

        and: "service retorna null"
        1 * service.buscarPorId(999) >> null
    }

    def "deve tratar exceção ao cadastrar"() {
        given: "um DTO válido"
        def dto = new EmpresaDTO(
            nome: "Tech Corp",
            email: "contato@techcorp.com",
            cnpj: "12.345.678/0001-90",
            descricao: "Empresa de tecnologia",
            pais: "Brasil",
            estado: "SP",
            cidade: "São Paulo",
            cep: "01234-567"
        )

        when: "tentar cadastrar"
        def resultado = controller.cadastrarEmpresa(dto)

        then: "deve retornar erro"
        resultado.sucesso == false
        resultado.mensagem.contains("Erro ao cadastrar empresa")

        and: "o service lança exceção"
        1 * service.cadastrar(dto) >> { throw new RuntimeException("Erro no banco") }
    }
}

