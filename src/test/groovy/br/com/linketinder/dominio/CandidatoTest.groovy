package br.com.linketinder.dominio

import spock.lang.Specification
import br.com.linketinder.dto.PerfilPublico
import br.com.linketinder.dto.DadosPessoais

class CandidatoTest extends Specification {
    def "deve criar candidato corretamente"() {
        when:
        def cand = new Candidato("1", "João", "joao@email.com", "1234-5678", ["Java", "Groovy"])

        then:
        cand.id == "1"
        cand.nome == "João"
        cand.email == "joao@email.com"
        cand.telefone == "1234-5678"
        cand.competencias == ["Java", "Groovy"]
    }

    def "deve retornar perfil público corretamente"() {
        given:
        def cand = new Candidato("2", "Maria", "maria@email.com", "9999-8888", ["Python"])

        when:
        PerfilPublico perfil = cand.obterPerfilPublico()

        then:
        perfil.nome == "Maria"
        perfil.descricao == "Candidato"
        perfil.competencias == ["Python"]
    }

    def "deve retornar dados pessoais corretamente"() {
        given:
        def cand = new Candidato("3", "Ana", "ana@email.com", "1111-2222", ["Kotlin"])

        when:
        DadosPessoais dados = cand.obterDadosPessoais()

        then:
        dados.nomeOuRazao == "Ana"
        dados.email == "ana@email.com"
        dados.telefone == "1111-2222"
    }

    def "deve comparar candidatos por id"() {
        given:
        def c1 = new Candidato("x", "A", "a@a.com", "1", [])
        def c2 = new Candidato("x", "B", "b@b.com", "2", [])
        def c3 = new Candidato("y", "A", "a@a.com", "1", [])

        expect:
        c1 == c2
        c1 != c3
    }
}

