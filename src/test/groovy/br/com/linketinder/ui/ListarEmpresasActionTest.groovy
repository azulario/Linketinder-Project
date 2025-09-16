package br.com.linketinder.ui

import spock.lang.Specification
import br.com.linketinder.dominio.Empresa

class ListarEmpresasActionTest extends Specification {
    def "executa ação e imprime empresas corretamente"() {
        given:
        def empresa1 = Mock(Empresa) {
            getId() >> "emp-1"
            getCompetenciasBuscadas() >> ["Java", "Groovy"]
        }
        def empresa2 = Mock(Empresa) {
            getId() >> "emp-2"
            getCompetenciasBuscadas() >> ["Python"]
        }
        def context = new MenuContext(null, [], [empresa1, empresa2], null)
        def action = new ListarEmpresasAction(context)
        def out = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))

        when:
        action.execute()

        then:
        def output = out.toString()
        output.contains("ID: emp-1 | Competências desejadas: Java, Groovy")
        output.contains("ID: emp-2 | Competências desejadas: Python")

        cleanup:
        System.setOut(System.out)
    }

    def "executa ação com lista vazia imprime mensagem apropriada"() {
        given:
        def context = new MenuContext(null, [], [], null)
        def action = new ListarEmpresasAction(context)
        def out = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))

        when:
        action.execute()

        then:
        out.toString().contains("Nenhuma empresa cadastrada.")

        cleanup:
        System.setOut(System.out)
    }
}
