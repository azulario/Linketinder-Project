package com.linketinder.service

import com.linketinder.dao.EmpresaDAO
import com.linketinder.dao.EnderecoDAO
import com.linketinder.dto.EmpresaDTO
import com.linketinder.model.Empresa
import com.linketinder.model.Endereco
import spock.lang.Specification

class EmpresaServiceSpec extends Specification {

    EmpresaService service
    EmpresaDAO empresaDAO
    EnderecoDAO enderecoDAO

    def setup() {
        empresaDAO = Mock(EmpresaDAO)
        enderecoDAO = Mock(EnderecoDAO)
        service = new EmpresaService(empresaDAO, enderecoDAO)
    }

    def "deve cadastrar uma empresa com sucesso"() {
        given: "um DTO de empresa válido"
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

        when: "cadastrar a empresa"
        def resultado = service.cadastrar(dto)

        then: "deve retornar a empresa cadastrada"
        resultado != null
        resultado.nome == "Tech Corp"
        resultado.email == "contato@techcorp.com"
        resultado.cnpj == "12.345.678/0001-90"
        resultado.enderecoId == 1

        and: "deve ter chamado os DAOs"
        1 * enderecoDAO.buscarOuCriar(_ as Endereco) >> 1
        1 * empresaDAO.inserir(_ as Empresa) >> { Empresa e -> e.id = 10; return null }
    }

    def "deve listar todas as empresas"() {
        given: "uma lista de empresas no DAO"
        def empresas = [
            new Empresa("Tech Corp", "tech@email.com", "12.345.678/0001-90", "Empresa tech"),
            new Empresa("Dev Solutions", "dev@email.com", "98.765.432/0001-10", "Consultoria")
        ]

        when: "listar todas as empresas"
        def resultado = service.listarTodas()

        then: "deve retornar a lista de empresas"
        resultado.size() == 2
        resultado[0].nome == "Tech Corp"
        resultado[1].nome == "Dev Solutions"

        and: "deve ter chamado o DAO"
        1 * empresaDAO.listar() >> empresas
    }

    def "deve buscar empresa por ID"() {
        given: "uma empresa no banco"
        def empresa = new Empresa("Tech Corp", "tech@email.com", "12.345.678/0001-90", "Empresa tech")
        empresa.id = 1

        when: "buscar por ID"
        def resultado = service.buscarPorId(1)

        then: "deve retornar a empresa"
        resultado != null
        resultado.id == 1
        resultado.nome == "Tech Corp"

        and: "deve ter chamado o DAO"
        1 * empresaDAO.buscarPorId(1) >> empresa
    }
}

