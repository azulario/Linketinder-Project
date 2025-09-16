package br.com.linketinder.ui

import spock.lang.Specification

class ExitActionTest extends Specification {
    def "Testar label da ação de sair"() {
        expect:
        new ExitAction().label() == "Sair"
    }
}
