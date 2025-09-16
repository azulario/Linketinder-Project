package br.com.linketinder.ui
import spock.lang.Specification

class EmpresaCurteCandidatoActionTest extends Specification {
    def "deve delegar ao controller e retornar true"() {
        given:
        def controller = Mock(br.com.linketinder.controller.EmpresaController) {
            1 * curtirCandidato(1, 2) >> true
        }
        def action = new EmpresaCurteCandidatoAction(controller)

        expect:
        action.executar(1, 2)
    }

    def "deve lançar exceção se idEmpresa for nulo"() {
        given:
        def controller = Mock(br.com.linketinder.controller.EmpresaController)
        def action = new EmpresaCurteCandidatoAction(controller)

        when:
        action.executar(null, 2)

        then:
        def e = thrown(IllegalArgumentException)
        e.message.contains("idEmpresa não pode ser nulo")
    }

    def "deve lançar exceção se idCandidato for nulo"() {
        given:
        def controller = Mock(br.com.linketinder.controller.EmpresaController)
        def action = new EmpresaCurteCandidatoAction(controller)

        when:
        action.executar(1, null)

        then:
        def e = thrown(IllegalArgumentException)
        e.message.contains("idCandidato não pode ser nulo")
    }

    def "deve lançar exceção se idEmpresa for negativo"() {
        given:
        def controller = Mock(br.com.linketinder.controller.EmpresaController)
        def action = new EmpresaCurteCandidatoAction(controller)

        when:
        action.executar(-1, 2)

        then:
        def e = thrown(IllegalArgumentException)
        e.message.contains("idEmpresa deve ser um número positivo")
    }

    def "deve lançar exceção se idCandidato for string não numérica"() {
        given:
        def controller = Mock(br.com.linketinder.controller.EmpresaController)
        def action = new EmpresaCurteCandidatoAction(controller)

        when:
        action.executar(1, "abc")

        then:
        def e = thrown(IllegalArgumentException)
        e.message.contains("idCandidato deve ser um número válido")
    }
}
