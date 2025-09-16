package br.com.linketinder.dto

import spock.lang.Specification

class PerfilPublicoTest extends Specification {
    def "deve criar perfil público corretamente"() {
        when:
        def perfil = new PerfilPublico("João", "Candidato", ["Java", "Groovy"])

        then:
        perfil.nome == "João"
        perfil.descricao == "Candidato"
        perfil.competencias == ["Java", "Groovy"]
    }

    def "deve exibir corretamente as competências"() {
        given:
        def competencias = ["Python", "Django", "SQL"]
        def perfil = new PerfilPublico("Maria", "Empresa", competencias)

        expect:
        perfil.competencias == competencias
        perfil.competencias.size() == 3
        perfil.competencias.contains("Django")
    }

    def "não deve expor dados pessoais sensíveis"() {
        when:
        def perfil = new PerfilPublico("Carlos", "Empresa", ["Kotlin"])

        then:
        !perfil.hasProperty("email")
        !perfil.hasProperty("telefone")
        !perfil.hasProperty("cpf")
        !perfil.hasProperty("cnpj")
        !perfil.hasProperty("cep")
    }
}
