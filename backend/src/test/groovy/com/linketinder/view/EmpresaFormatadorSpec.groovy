package com.linketinder.view

import com.linketinder.model.Empresa
import com.linketinder.model.Endereco
import spock.lang.Specification

class EmpresaFormatadorSpec extends Specification {

    EmpresaFormatador formatador

    def setup() {
        formatador = new EmpresaFormatador()
    }

    def "deve formatar empresa com todos os dados"() {
        given: "uma empresa completa"
        def empresa = new Empresa(
            "Tech Corp",
            "contato@techcorp.com",
            "12.345.678/0001-90",
            "Empresa de desenvolvimento de software"
        )
        empresa.id = 1
        empresa.endereco = new Endereco("Brasil", "SP", "São Paulo", "01234-567")
        empresa.endereco.id = 1

        when: "formatar a empresa"
        def resultado = formatador.formatar(empresa)

        then: "deve conter todos os dados formatados"
        resultado.contains("EMPRESA")
        resultado.contains("ID: 1")
        resultado.contains("Nome: Tech Corp")
        resultado.contains("Email: contato@techcorp.com")
        resultado.contains("CNPJ: 12.345.678/0001-90")
        resultado.contains("Descrição: Empresa de desenvolvimento de software")
        resultado.contains("Endereço:")
    }

    def "deve formatar empresa sem ID"() {
        given: "uma empresa sem ID"
        def empresa = new Empresa(
            "Dev Solutions",
            "contato@devsolutions.com",
            "98.765.432/0001-10",
            "Consultoria em TI"
        )

        when: "formatar a empresa"
        def resultado = formatador.formatar(empresa)

        then: "não deve conter ID"
        !resultado.contains("ID:")
        resultado.contains("Nome: Dev Solutions")
    }

    def "deve formatar empresa sem endereço"() {
        given: "uma empresa sem endereço"
        def empresa = new Empresa(
            "Code Masters",
            "info@codemasters.com",
            "11.222.333/0001-44",
            "Desenvolvimento de apps"
        )

        when: "formatar a empresa"
        def resultado = formatador.formatar(empresa)

        then: "não deve conter endereço"
        !resultado.contains("Endereço:")
        resultado.contains("Nome: Code Masters")
    }

    def "deve formatar empresa com descrição vazia"() {
        given: "uma empresa sem descrição"
        def empresa = new Empresa(
            "Startup XYZ",
            "hello@startupxyz.com",
            "55.666.777/0001-88",
            ""
        )

        when: "formatar a empresa"
        def resultado = formatador.formatar(empresa)

        then: "deve ter descrição vazia"
        resultado.contains("Descrição:")
        resultado.contains("Nome: Startup XYZ")
    }

    def "deve formatar empresa com descrição longa"() {
        given: "uma empresa com descrição extensa"
        def descricao = "Somos uma empresa líder em tecnologia, especializada em desenvolvimento de soluções inovadoras para o mercado corporativo. Nossa missão é transformar negócios através da tecnologia."
        def empresa = new Empresa(
            "Innovation Tech",
            "contact@innovationtech.com",
            "22.333.444/0001-99",
            descricao
        )

        when: "formatar a empresa"
        def resultado = formatador.formatar(empresa)

        then: "deve conter a descrição completa"
        resultado.contains(descricao)
        resultado.contains("Nome: Innovation Tech")
    }

    def "deve incluir linhas separadoras na formatação"() {
        given: "qualquer empresa"
        def empresa = new Empresa(
            "Simple Company",
            "simple@company.com",
            "99.888.777/0001-66",
            "Empresa simples"
        )

        when: "formatar a empresa"
        def resultado = formatador.formatar(empresa)

        then: "deve conter linhas decorativas"
        resultado.contains("=")
    }
}

