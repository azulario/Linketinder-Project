package br.com.linketinder.ui
import spock.lang.Specification
import br.com.linketinder.dominio.Candidato

class ListarCandidatosActionTest extends Specification {
    def "label retorna texto correto"() {
        given:
        def context = new MenuContext(null, [], null, null)
        def action = new ListarCandidatosAction(context)
        expect:
        action.label() == "Listar Candidatos (perfil público)"
    }

    def "execute imprime id e competencias de todos os candidatos"() {
        given:
        def candidato1 = Mock(Candidato) {
            getId() >> 1
            obterPerfilPublico() >> new br.com.linketinder.dto.PerfilPublico("1", "CANDIDATO", ["Java", "Groovy"])
        }
        def candidato2 = Mock(Candidato) {
            getId() >> 2
            obterPerfilPublico() >> new br.com.linketinder.dto.PerfilPublico("2", "CANDIDATO", ["Python"])
        }
        def context = new MenuContext(null, [candidato1, candidato2], null, null)
        def action = new ListarCandidatosAction(context)
        def out = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))

        when:
        action.execute()

        then:
        def output = out.toString()
        output.contains("0) id = 1 competencias = [Java, Groovy]")
        output.contains("1) id = 2 competencias = [Python]")

        cleanup:
        System.setOut(System.out)
    }

    def "execute não imprime nada se lista de candidatos está vazia"() {
        given:
        def context = new MenuContext(null, [], null, null)
        def action = new ListarCandidatosAction(context)
        def out = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))

        when:
        action.execute()

        then:
        out.toString().trim() == ""

        cleanup:
        System.setOut(System.out)
    }
}
