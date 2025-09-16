package br.com.linketinder.ui
import spock.lang.Specification

class ConsoleUtilsTest extends Specification {
    def "Testar método de conversão de string para inteiro com entrada válida"() {
        expect:
        ConsoleUtils.parseIntInput("42") == 42
        ConsoleUtils.parseIntInput("  7  ") == 7
    }

    def "Testar método de conversão de string para inteiro com entrada inválida"() {
        expect:
        ConsoleUtils.parseIntInput("abc") == -1
        ConsoleUtils.parseIntInput("") == -1
        ConsoleUtils.parseIntInput(null) == -1
        ConsoleUtils.parseIntInput("123abc") == -1
    }

    def "Testar método de conversão de string para inteiro com fallback customizado"() {
        expect:
        ConsoleUtils.parseIntInput("abc", 99) == 99
        ConsoleUtils.parseIntInput("", 77) == 77
        ConsoleUtils.parseIntInput(null, 55) == 55
    }

    def "Testar método de validação de índice"() {
        expect:
        ConsoleUtils.validateIndex(0, 5) == true
        ConsoleUtils.validateIndex(4, 5) == true
        ConsoleUtils.validateIndex(-1, 5) == false
        ConsoleUtils.validateIndex(5, 5) == false
        ConsoleUtils.validateIndex(10, 5) == false
        ConsoleUtils.validateIndex(0, 0) == false
    }
}
