package br.com.linketinder.controller

import spock.lang.Specification
import br.com.linketinder.dominio.Candidato
import br.com.linketinder.dominio.Empresa
import br.com.linketinder.servico.SistemaMatch

class EmpresaControllerTest extends Specification {
    def "deve curtir candidato com sucesso"() {
        given:
        def candidato = new Candidato("1", "João", "joao@email.com", "1234-5678", ["Java"])
        def empresa = new Empresa("1", "EmpresaX", "contato@x.com", "9999-8888", ["Java"])
        def sistema = new SistemaMatch([candidato], [empresa])
        def controller = new EmpresaControllerImpl(sistema)

        expect:
        controller.curtirCandidato(1, 1)
    }

    def "deve implementar interface corretamente"() {
        given:
        def sistema = new SistemaMatch([], [])
        def controller = new EmpresaControllerImpl(sistema)

        expect:
        controller instanceof EmpresaController
    }
}
