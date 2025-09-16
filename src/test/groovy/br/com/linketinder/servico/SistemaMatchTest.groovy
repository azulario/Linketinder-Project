package br.com.linketinder.servico

import spock.lang.Specification
import br.com.linketinder.dominio.Candidato
import br.com.linketinder.dominio.Empresa
import br.com.linketinder.dto.PerfilPublico

class SistemaMatchTest extends Specification {
    def "deve registrar curtida de candidato para empresa"() {
        given:
        def candidato = new br.com.linketinder.dominio.Candidato("c1", "João", "joao@email.com", "9999-9999", ["Java"])
        def empresa = new br.com.linketinder.dominio.Empresa("e1", "Empresa X", "empresa@email.com", "8888-8888", ["Java"])
        def sistema = new SistemaMatch()

        when:
        sistema.curtir(candidato, empresa)

        then:
        // O candidato curtiu a empresa, então o match só ocorre se a empresa também curtir
        !sistema.deuMatch(candidato, empresa)
        // Agora a empresa curte o candidato
        sistema.curtir(empresa, candidato)
        sistema.deuMatch(candidato, empresa)
    }

    def "deve registrar curtida de empresa para candidato"() {
        given:
        def candidato = new br.com.linketinder.dominio.Candidato("c2", "Maria", "maria@email.com", "8888-8888", ["Groovy"])
        def empresa = new br.com.linketinder.dominio.Empresa("e2", "Empresa Y", "empresa2@email.com", "7777-7777", ["Groovy"])
        def sistema = new SistemaMatch()

        when:
        sistema.curtir(empresa, candidato)

        then:
        // A empresa curtiu o candidato, mas o match só ocorre se o candidato também curtir
        !sistema.deuMatch(candidato, empresa)
        // Agora o candidato curte a empresa
        sistema.curtir(candidato, empresa)
        sistema.deuMatch(candidato, empresa)
    }

    def "deve identificar match apenas quando ambos curtiram"() {
        given:
        def candidato = new br.com.linketinder.dominio.Candidato("c3", "Carlos", "carlos@email.com", "7777-7777", ["Python"])
        def empresa = new br.com.linketinder.dominio.Empresa("e3", "Empresa Z", "empresa3@email.com", "6666-6666", ["Python"])
        def sistema = new SistemaMatch()

        when:
        sistema.curtir(candidato, empresa)
        sistema.curtir(empresa, candidato)

        then:
        sistema.deuMatch(candidato, empresa)
    }

    def "não deve identificar match se apenas um curtiu"() {
        given:
        def candidato = new br.com.linketinder.dominio.Candidato("c4", "Ana", "ana@email.com", "5555-5555", ["Kotlin"])
        def empresa = new br.com.linketinder.dominio.Empresa("e4", "Empresa W", "empresa4@email.com", "4444-4444", ["Kotlin"])
        def sistema = new SistemaMatch()

        when:
        sistema.curtir(candidato, empresa)

        then:
        !sistema.deuMatch(candidato, empresa)

        when:
        sistema = new SistemaMatch()
        sistema.curtir(empresa, candidato)

        then:
        !sistema.deuMatch(candidato, empresa)
    }

    def "deve retornar perfil público do candidato"() {
        given:
        def perfil = new PerfilPublico("João", "Candidato", ["Java"])
        def candidato = Mock(Candidato) { obterPerfilPublico() >> perfil }
        def sistema = new SistemaMatch()

        when:
        def result = sistema.verPerfilPublico(candidato)

        then:
        result == perfil
    }

    def "deve retornar perfil público da empresa"() {
        given:
        def perfil = new PerfilPublico("Empresa X", "Empresa", ["Groovy"])
        def empresa = Mock(Empresa) { obterPerfilPublico() >> perfil }
        def sistema = new SistemaMatch()

        when:
        def result = sistema.verPerfilPublico(empresa)

        then:
        result == perfil
    }

    def "não deve expor dados sensíveis ao retornar perfil público"() {
        // TODO: Implementar
    }

    def "deve integrar corretamente com objetos de domínio"() {
        // TODO: Implementar
    }
}
