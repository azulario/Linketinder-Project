package br.com.linketinder.ui

import spock.lang.Specification

class MenuActionTest extends Specification {
    def "deve executar o método execute de uma MenuAction"() {
        given:
        def action = Mock(MenuAction)
        action.label() >> "Ação Teste"

        when:
        action.execute()

        then:
        1 * action.execute()
    }

    def "deve retornar o label correto"() {
        given:
        def action = Mock(MenuAction)
        action.label() >> "Minha Ação"

        expect:
        action.label() == "Minha Ação"
    }

    // Teste de integração simplificado: verifica se o método execute é chamado corretamente
    def "deve chamar execute da action correta manualmente"() {
        given:
        def action1 = Mock(MenuAction)
        def action2 = Mock(MenuAction)
        def actions = ["1": action1, "2": action2]

        when:
        actions["1"].execute()

        then:
        1 * action1.execute()
        0 * action2.execute()
    }
}
