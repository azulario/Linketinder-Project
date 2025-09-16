package br.com.linketinder.dominio

import spock.lang.Specification
import br.com.linketinder.dto.PerfilPublico
import br.com.linketinder.dto.DadosPessoais

class EmpresaTest extends Specification {
    def "deve criar empresa corretamente"() {
        when:
        def emp = new Empresa("1", "EmpresaX", "contato@x.com", "1234-5678", ["Java", "Groovy"])

        then:
        emp.id == "1"
        emp.razaoSocial == "EmpresaX"
        emp.email == "contato@x.com"
        emp.telefone == "1234-5678"
        emp.competenciasBuscadas == ["Java", "Groovy"]
    }

    def "deve retornar perfil público corretamente"() {
        given:
        def emp = new Empresa("2", "EmpresaY", "contato@y.com", "9999-8888", ["Python"])

        when:
        PerfilPublico perfil = emp.obterPerfilPublico()

        then:
        perfil.nome == "EmpresaY"
        perfil.descricao == "Empresa"
        perfil.competencias == ["Python"]
    }

    def "deve retornar dados pessoais corretamente"() {
        given:
        def emp = new Empresa("3", "EmpresaZ", "contato@z.com", "1111-2222", ["Kotlin"])

        when:
        DadosPessoais dados = emp.obterDadosPessoais()

        then:
        dados.nomeOuRazao == "EmpresaZ"
        dados.email == "contato@z.com"
        dados.telefone == "1111-2222"
    }

    def "deve comparar empresas por id"() {
        given:
        def e1 = new Empresa("x", "A", "a@a.com", "1", [])
        def e2 = new Empresa("x", "B", "b@b.com", "2", [])
        def e3 = new Empresa("y", "A", "a@a.com", "1", [])

        expect:
        e1 == e2
        e1 != e3
    }
}
