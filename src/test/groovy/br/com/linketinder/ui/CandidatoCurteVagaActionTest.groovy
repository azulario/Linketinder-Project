package br.com.linketinder.ui
import br.com.linketinder.controller.CandidatoController
import spock.lang.Specification

class CandidatoCurteVagaActionTest extends Specification {
    def "deve delegar ao controller e retornar true"() {
        given:
        def controller = Mock(CandidatoController) {
            1 * curtirVaga("1", "2") >> true
        }
        def action = new CandidatoCurteVagaAction(controller)

        expect:
        action.executar(1, 2)
    }

    def "deve lançar exceção se idCandidato for nulo"() {
        given:
        def controller = Mock(CandidatoController)
        def action = new CandidatoCurteVagaAction(controller)

        when:
        action.executar(null, 2L)

        then:
        def e = thrown(IllegalArgumentException)
        e.message.contains("idCandidato não pode ser nulo")
    }

    def "deve lançar exceção se idVaga for nulo"() {
        given:
        def controller = Mock(CandidatoController)
        def action = new CandidatoCurteVagaAction(controller)

        when:
        action.executar(1L, null)

        then:
        def e = thrown(IllegalArgumentException)
        e.message.contains("idVaga não pode ser nulo")
    }

    def "deve lançar exceção se idCandidato for negativo"() {
        given:
        def controller = Mock(CandidatoController)
        def action = new CandidatoCurteVagaAction(controller)

        when:
        action.executar(-1L, 2L)

        then:
        def e = thrown(IllegalArgumentException)
        e.message.contains("idCandidato deve ser um número positivo")
    }

    def "deve lançar exceção se idVaga for string não numérica"() {
        given:
        def controller = Mock(CandidatoController)
        def action = new CandidatoCurteVagaAction(controller)

        when:
        action.executar(1L, "abc")

        then:
        def e = thrown(IllegalArgumentException)
        e.message.contains("idVaga deve ser um número válido")
    }
}
