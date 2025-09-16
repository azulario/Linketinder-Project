package br.com.linketinder.ui

import spock.lang.Specification
import br.com.linketinder.dominio.Candidato
import br.com.linketinder.dominio.Empresa
import br.com.linketinder.servico.SistemaMatch

class ListarMatchesActionTest extends Specification {
    def "deve imprimir todos os matches existentes"() {
        given:
        def candidato1 = Mock(Candidato) { getId() >> "c1"; getNome() >> "Alice" }
        def candidato2 = Mock(Candidato) { getId() >> "c2"; getNome() >> "Bob" }
        def empresa1 = Mock(Empresa) { getId() >> "e1"; getRazaoSocial() >> "Empresa X" }
        def empresa2 = Mock(Empresa) { getId() >> "e2"; getRazaoSocial() >> "Empresa Y" }
        def sistema = Mock(SistemaMatch)
        sistema.deuMatch(candidato1, empresa1) >> true
        sistema.deuMatch(candidato1, empresa2) >> false
        sistema.deuMatch(candidato2, empresa1) >> false
        sistema.deuMatch(candidato2, empresa2) >> true
        def context = new MenuContext(sistema, [candidato1, candidato2], [empresa1, empresa2], null)
        def action = new ListarMatchesAction(context)
        def out = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))

        when:
        action.execute()

        then:
        def output = out.toString()
        output.contains("Alice e Empresa X são um match!")
        output.contains("Bob e Empresa Y são um match!")
        !output.contains("Alice e Empresa Y são um match!")
        !output.contains("Bob e Empresa X são um match!")

        cleanup:
        System.setOut(System.out)
    }

    def "deve imprimir mensagem apropriada quando não houver matches"() {
        given:
        def candidato = Mock(Candidato) { getId() >> "c1"; getNome() >> "Alice" }
        def empresa = Mock(Empresa) { getId() >> "e1"; getRazaoSocial() >> "Empresa X" }
        def sistema = Mock(SistemaMatch)
        sistema.deuMatch(_, _) >> false
        def context = new MenuContext(sistema, [candidato], [empresa], null)
        def action = new ListarMatchesAction(context)
        def out = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))

        when:
        action.execute()

        then:
        out.toString().contains("Nenhum match encontrado.")

        cleanup:
        System.setOut(System.out)
    }
}
