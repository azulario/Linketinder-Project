package br.com.linketinder.dto

import spock.lang.Specification

class DadosPessoaisTest extends Specification {
    def "deve criar dados pessoais corretamente"() {
        when:
        def dados = new DadosPessoais("João", "joao@email.com", "1234-5678")

        then:
        dados.nomeOuRazao == "João"
        dados.email == "joao@email.com"
        dados.telefone == "1234-5678"
    }

    def "deve validar campos obrigatórios não nulos"() {
        when:
        def dados = new DadosPessoais("Maria", "maria@email.com", "9999-8888")

        then:
        dados.nomeOuRazao
        dados.email
        dados.telefone
    }

    def "deve integrar com Candidato e Empresa"() {
        given:
        def candidato = new br.com.linketinder.dominio.Candidato("1", "Ana", "ana@email.com", "1111-2222", ["Java"])
        def empresa = new br.com.linketinder.dominio.Empresa("2", "EmpresaX", "contato@empresax.com", "3333-4444", ["Groovy"])

        when:
        def dadosCand = candidato.obterDadosPessoais()
        def dadosEmp = empresa.obterDadosPessoais()

        then:
        dadosCand.nomeOuRazao == "Ana"
        dadosCand.email == "ana@email.com"
        dadosCand.telefone == "1111-2222"
        dadosEmp.nomeOuRazao == "EmpresaX"
        dadosEmp.email == "contato@empresax.com"
        dadosEmp.telefone == "3333-4444"
    }
}
