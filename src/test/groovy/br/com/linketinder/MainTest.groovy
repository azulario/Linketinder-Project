package br.com.linketinder

import spock.lang.Specification
import br.com.linketinder.dominio.Candidato
import br.com.linketinder.dominio.Empresa
import br.com.linketinder.servico.SistemaMatch
import br.com.linketinder.ui.MenuContext

class MainTest extends Specification {
    def "deve inicializar o sistema corretamente"() {
        when:
        Main.main([] as String[])

        then:
        Main.sistema instanceof SistemaMatch
        Main.candidatos != null && Main.candidatos.size() == 5
        Main.empresas != null && Main.empresas.size() == 5
        Main.context instanceof MenuContext
    }
    // [x] Testar integração entre componentes
    // [x] Testar menu principal
    // [x] Testar refatoração para manter o main enxuto

    def "deve exibir menu principal e encerrar ao digitar 0"() {
        given:
        def input = new ByteArrayInputStream("0\n".bytes)
        System.setIn(input)
        def out = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))

        when:
        Main.main([] as String[])

        then:
        def output = out.toString()
        output.contains("Menu Principal:")
        output.contains("0 - Sair")
        output.contains("Saindo...")

        cleanup:
        System.setIn(System.in)
        System.setOut(System.out)
    }

    def "deve listar empresas ao escolher a opção 2"() {
        given:
        def input = new ByteArrayInputStream("2\n0\n".bytes) // lista empresas e depois sai
        System.setIn(input)
        def out = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))

        when:
        Main.main([] as String[])

        then:
        def output = out.toString()
        output.contains("Lista de Empresas")
        output.contains("ID: emp-1 | Competências desejadas: Java, Spring Framework")
        output.contains("ID: emp-2 | Competências desejadas: Angular, JavaScript")
        output.contains("ID: emp-3 | Competências desejadas: Python, Django")
        output.contains("ID: emp-4 | Competências desejadas: AWS, Docker")
        output.contains("ID: emp-5 | Competências desejadas: Kotlin, Flutter")

        cleanup:
        System.setIn(System.in)
        System.setOut(System.out)
    }
}
