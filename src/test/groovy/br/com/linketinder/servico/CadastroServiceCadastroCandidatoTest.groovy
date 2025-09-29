package br.com.linketinder.servico

import br.com.linketinder.dominio.Candidato
import spock.lang.Specification

/**
 * Autor: Azulario
 * Teste TDD para cadastro de novo candidato no CadastroService
 */
class CadastroServiceCadastroCandidatoTest extends Specification {
    def "deve adicionar novo candidato à lista"() {
        given:
        def candidatos = []
        def candidato = new Candidato("cand-99", "NovoCandidato", "novo@cand.com", "8888-8888", ["Groovy"])
        def service = new CadastroService()

        when:
        boolean resultado = service.adicionarCandidato(candidato, candidatos)

        then:
        resultado
        candidatos.contains(candidato)
    }

    def "não deve adicionar candidato com id já existente"() {
        given:
        def candidatoExistente = new Candidato("cand-1", "CandX", "x@x.com", "1111-1111", ["Java"])
        def candidatos = [candidatoExistente]
        def candidatoDuplicado = new Candidato("cand-1", "CandY", "y@y.com", "2222-2222", ["Groovy"])
        def service = new CadastroService()

        when:
        boolean resultado = service.adicionarCandidato(candidatoDuplicado, candidatos)

        then:
        !resultado
        candidatos.size() == 1
        candidatos[0].nome == "CandX"
    }
}
