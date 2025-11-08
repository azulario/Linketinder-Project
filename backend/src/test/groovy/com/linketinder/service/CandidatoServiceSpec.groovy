package com.linketinder.service

import com.linketinder.dao.CandidatoDAO
import com.linketinder.dao.EnderecoDAO
import com.linketinder.dto.CandidatoDTO
import com.linketinder.model.Candidato
import com.linketinder.model.Endereco
import spock.lang.Specification

class CandidatoServiceSpec extends Specification {

    CandidatoService service
    CandidatoDAO candidatoDAO
    EnderecoDAO enderecoDAO

    def setup() {
        candidatoDAO = Mock(CandidatoDAO)
        enderecoDAO = Mock(EnderecoDAO)
        service = new CandidatoService(candidatoDAO, enderecoDAO)
    }

    def "deve cadastrar um candidato com sucesso"() {
        given: "um DTO de candidato válido"
        def dto = new CandidatoDTO(
            nome: "João",
            sobrenome: "Silva",
            email: "joao@email.com",
            cpf: "123.456.789-00",
            dataDeNascimento: "1990-05-15",
            descricao: "Desenvolvedor Java",
            competencias: ["Java", "Spring"],
            pais: "Brasil",
            estado: "SP",
            cidade: "São Paulo",
            cep: "01234-567"
        )

        when: "cadastrar o candidato"
        def resultado = service.cadastrar(dto)

        then: "deve retornar o candidato cadastrado"
        resultado != null
        resultado.nome == "João"
        resultado.sobrenome == "Silva"
        resultado.email == "joao@email.com"
        resultado.enderecoId == 1

        and: "deve ter chamado os DAOs"
        1 * enderecoDAO.buscarOuCriar(_ as Endereco) >> 1
        1 * candidatoDAO.inserir(_ as Candidato) >> { Candidato c -> c.id = 10; return null }
    }

    def "deve listar todos os candidatos"() {
        given: "uma lista de candidatos no DAO"
        def candidatos = [
            new Candidato("João", "Silva", "joao@email.com", "123.456.789-00", null, "Dev", ["Java"]),
            new Candidato("Maria", "Santos", "maria@email.com", "987.654.321-00", null, "Dev", ["Python"])
        ]

        when: "listar todos os candidatos"
        def resultado = service.listarTodos()

        then: "deve retornar a lista de candidatos"
        resultado.size() == 2
        resultado[0].nome == "João"
        resultado[1].nome == "Maria"

        and: "deve ter chamado o DAO"
        1 * candidatoDAO.listar() >> candidatos
    }

    def "deve buscar candidato por ID"() {
        given: "um candidato no banco"
        def candidato = new Candidato("João", "Silva", "joao@email.com", "123.456.789-00", null, "Dev", ["Java"])
        candidato.id = 1

        when: "buscar por ID"
        def resultado = service.buscarPorId(1)

        then: "deve retornar o candidato"
        resultado != null
        resultado.id == 1
        resultado.nome == "João"

        and: "deve ter chamado o DAO"
        1 * candidatoDAO.buscarPorId(1) >> candidato
    }

    def "deve formatar candidato"() {
        given: "um candidato"
        def candidato = new Candidato("João", "Silva", "joao@email.com", "123.456.789-00", null, "Dev", ["Java"])

        when: "formatar o candidato"
        def resultado = service.formatarCandidato(candidato)

        then: "deve retornar uma string formatada"
        resultado != null
        resultado.contains("João")
        resultado.contains("Silva")
    }

    def "deve formatar lista de candidatos"() {
        given: "uma lista de candidatos"
        def candidatos = [
            new Candidato("João", "Silva", "joao@email.com", "123.456.789-00", null, "Dev", ["Java"]),
            new Candidato("Maria", "Santos", "maria@email.com", "987.654.321-00", null, "Dev", ["Python"])
        ]

        when: "formatar a lista"
        def resultado = service.formatarLista(candidatos)

        then: "deve retornar uma string formatada com todos"
        resultado != null
        resultado.contains("1. João")
        resultado.contains("2. Maria")
    }

    def "deve retornar mensagem quando lista de candidatos está vazia"() {
        given: "uma lista vazia"
        def candidatos = []

        when: "formatar a lista"
        def resultado = service.formatarLista(candidatos)

        then: "deve retornar mensagem de lista vazia"
        resultado == "Nenhum candidato cadastrado."
    }
}

