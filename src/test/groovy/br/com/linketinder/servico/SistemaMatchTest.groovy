package br.com.linketinder.servico

import spock.lang.Specification
import br.com.linketinder.dominio.Candidato
import br.com.linketinder.dominio.Empresa
import br.com.linketinder.ui.MenuContext
import br.com.linketinder.ui.ListarMatchesAction

class SistemaMatchTest extends Specification {
    def "deve registrar match recíproco entre candidato e empresa"() {
        given:
        def candidato = new Candidato("1", "João", "joao@email.com", "1234-5678", ["Java"])
        def empresa = new Empresa("1", "EmpresaX", "contato@x.com", "9999-8888", ["Java"])
        def sistema = new SistemaMatch([candidato], [empresa])

        when: "Candidato curte a empresa e empresa curte o candidato"
        sistema.curtir(candidato, empresa)
        sistema.curtir(empresa, candidato)

        then: "O sistema reconhece o match"
        sistema.deuMatch(candidato, empresa)
    }

    def "deve listar matches corretamente"() {
        given:
        def candidato = new Candidato("1", "João", "joao@email.com", "1234-5678", ["Java"])
        def empresa = new Empresa("1", "EmpresaX", "contato@x.com", "9999-8888", ["Java"])
        def sistema = new SistemaMatch([candidato], [empresa])
        def context = new MenuContext(sistema, [candidato], [empresa], null)
        def action = new ListarMatchesAction(context)
        sistema.curtir(candidato, empresa)
        sistema.curtir(empresa, candidato)
        def out = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))

        when:
        action.execute()

        then:
        out.toString().contains("João e EmpresaX são um match!")

        cleanup:
        System.setOut(System.out)
    }
}

