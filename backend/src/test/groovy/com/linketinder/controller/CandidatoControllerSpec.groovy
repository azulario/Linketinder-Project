package com.linketinder.controller

import com.linketinder.dto.CandidatoDTO
import com.linketinder.model.Candidato
import com.linketinder.service.CandidatoService
import spock.lang.Specification
import java.time.LocalDate

class CandidatoControllerSpec extends Specification {

    CandidatoController controller
    CandidatoService service

    def setup() {
        service = Mock(CandidatoService)
        controller = new CandidatoController(service)
    }

    def "deve cadastrar candidato com sucesso"() {
        given: "um DTO válido"
        def dto = new CandidatoDTO(
            nome: "João",
            sobrenome: "Silva",
            email: "joao@email.com",
            cpf: "123.456.789-00",
            dataDeNascimento: "1990-05-15",
            descricao: "Desenvolvedor",
            competencias: ["Java"],
            pais: "Brasil",
            estado: "SP",
            cidade: "São Paulo",
            cep: "01234-567"
        )

        and: "o candidato esperado"
        def candidato = new Candidato("João", "Silva", "joao@email.com", "123.456.789-00",
            LocalDate.of(1990, 5, 15), "Desenvolvedor", ["Java"])
        candidato.id = 1

        when: "cadastrar o candidato"
        def resultado = controller.cadastrar(dto)

        then: "deve retornar sucesso"
        resultado.sucesso == true
        resultado.mensagem == "Candidato cadastrado com sucesso"
        resultado.candidato == candidato

        and: "deve ter chamado o service"
        1 * service.cadastrar(dto) >> candidato
    }

    def "deve retornar erro ao cadastrar com dados inválidos"() {
        given: "um DTO inválido"
        def dto = new CandidatoDTO(
            nome: "",
            sobrenome: "",
            email: "email-invalido",
            cpf: "123",
            dataDeNascimento: "1990-05-15",
            descricao: "",
            competencias: [],
            pais: "",
            estado: "",
            cidade: "",
            cep: ""
        )

        when: "tentar cadastrar"
        def resultado = controller.cadastrar(dto)

        then: "deve retornar erro de validação"
        resultado.sucesso == false
        resultado.mensagem == "Dados inválidos"
        resultado.erros != null
        resultado.erros.size() > 0

        and: "não deve chamar o service"
        0 * service.cadastrar(_)
    }

    def "deve listar todos os candidatos com sucesso"() {
        given: "candidatos no service"
        def candidatos = [
            new Candidato("João", "Silva", "joao@email.com", "123.456.789-00", null, "Dev", ["Java"]),
            new Candidato("Maria", "Santos", "maria@email.com", "987.654.321-00", null, "Dev", ["Python"])
        ]

        when: "listar candidatos"
        def resultado = controller.listarTodos()

        then: "deve retornar lista com sucesso"
        resultado.sucesso == true
        resultado.candidatos.size() == 2

        and: "deve ter chamado o service"
        1 * service.listarTodos() >> candidatos
    }

    def "deve retornar mensagem quando não há candidatos"() {
        given: "nenhum candidato no service"
        def candidatos = []

        when: "listar candidatos"
        def resultado = controller.listarTodos()

        then: "deve retornar mensagem de lista vazia"
        resultado.sucesso == true
        resultado.candidatos == []

        and: "deve ter chamado o service"
        1 * service.listarTodos() >> candidatos
    }

    def "deve buscar candidato por ID com sucesso"() {
        given: "um candidato existe"
        def candidato = new Candidato("João", "Silva", "joao@email.com", "123.456.789-00", null, "Dev", ["Java"])
        candidato.id = 1

        when: "buscar candidato"
        def resultado = controller.buscarPorId(1)

        then: "deve retornar o candidato"
        resultado.sucesso == true
        resultado.candidato == candidato

        and: "deve ter chamado o service"
        1 * service.buscarPorId(1) >> candidato
    }

    def "deve retornar erro quando candidato não existe"() {
        when: "buscar candidato"
        def resultado = controller.buscarPorId(999)

        then: "deve retornar erro"
        resultado.sucesso == false
        resultado.mensagem == "Candidato não encontrado"

        and: "service retorna null"
        1 * service.buscarPorId(999) >> null
    }

    def "deve tratar exceção ao cadastrar"() {
        given: "um DTO válido"
        def dto = new CandidatoDTO(
            nome: "João",
            sobrenome: "Silva",
            email: "joao@email.com",
            cpf: "123.456.789-00",
            dataDeNascimento: "1990-05-15",
            descricao: "Dev",
            competencias: ["Java"],
            pais: "Brasil",
            estado: "SP",
            cidade: "São Paulo",
            cep: "01234-567"
        )

        when: "tentar cadastrar"
        def resultado = controller.cadastrar(dto)

        then: "deve retornar erro"
        resultado.sucesso == false
        resultado.mensagem.contains("Erro ao cadastrar candidato")

        and: "o service lança exceção"
        1 * service.cadastrar(dto) >> { throw new RuntimeException("Erro no banco") }
    }
}
