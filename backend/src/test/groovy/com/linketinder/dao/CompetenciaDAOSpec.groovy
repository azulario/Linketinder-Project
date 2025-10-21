package com.linketinder.dao

import com.linketinder.model.Competencia
import spock.lang.Specification

class CompetenciaDAOSpec extends Specification {
    CompetenciaDAO competenciaDAO

    def setup() {
        competenciaDAO = new CompetenciaDAO()
    }

    def "deve listar todas as competências"() {
        when: "listo todas as competências"
        List<Competencia> competencias = competenciaDAO.listar()

        then: "deve retornar uma lista"
        competencias != null
        competencias instanceof List
    }

    def "deve buscar uma competência por ID"() {
        given: "uma nova competência é inserida"
        String nomeUnico = "Java_Test_${System.currentTimeMillis()}"
        Competencia competencia = new Competencia(nomeUnico)
        competenciaDAO.inserir(competencia)
        Integer idInserido = competencia.id

        when: "busco a competência por ID"
        Competencia resultado = competenciaDAO.buscarPorId(idInserido)

        then: "deve retornar a competência correta"
        resultado != null
        resultado.id == idInserido
        resultado.nomeCompetencia == nomeUnico

        cleanup:
        if (idInserido) {
            competenciaDAO.deletar(idInserido)
        }
    }

    def "deve retornar null ao buscar competência inexistente"() {
        when: "busco uma competência com ID que não existe"
        Competencia resultado = competenciaDAO.buscarPorId(-999)

        then: "deve retornar null"
        resultado == null
    }

    def "deve inserir uma nova competência"() {
        given: "uma nova competência"
        String nomeUnico = "Python_Test_${System.currentTimeMillis()}"
        Competencia competencia = new Competencia(nomeUnico)

        when: "insiro a competência"
        competenciaDAO.inserir(competencia)

        then: "deve ter um ID gerado"
        competencia.id != null
        competencia.id > 0

        and: "deve estar no banco de dados"
        Competencia resultado = competenciaDAO.buscarPorId(competencia.id)
        resultado != null
        resultado.nomeCompetencia == nomeUnico

        cleanup:
        if (competencia.id) {
            competenciaDAO.deletar(competencia.id)
        }
    }

    def "deve atualizar uma competência existente"() {
        given: "uma competência já cadastrada"
        String nomeUnico = "JavaScript_Test_${System.currentTimeMillis()}"
        String nomeAtualizado = "TypeScript_Test_${System.currentTimeMillis()}"
        Competencia competencia = new Competencia(nomeUnico)
        competenciaDAO.inserir(competencia)
        Integer idInserido = competencia.id

        when: "atualizo o nome da competência"
        competencia.nomeCompetencia = nomeAtualizado
        competenciaDAO.atualizar(competencia)

        then: "as alterações devem ser salvas"
        Competencia resultado = competenciaDAO.buscarPorId(idInserido)
        resultado.nomeCompetencia == nomeAtualizado

        cleanup:
        if (idInserido) {
            competenciaDAO.deletar(idInserido)
        }
    }

    def "deve deletar uma competência"() {
        given: "uma competência cadastrada"
        String nomeUnico = "Ruby_Test_${System.currentTimeMillis()}"
        Competencia competencia = new Competencia(nomeUnico)
        competenciaDAO.inserir(competencia)
        Integer idInserido = competencia.id

        when: "deleto a competência"
        competenciaDAO.deletar(idInserido)

        then: "a competência não deve mais existir"
        Competencia resultado = competenciaDAO.buscarPorId(idInserido)
        resultado == null
    }

    def "não deve criar competência com nome nulo"() {
        when: "tento criar uma competência sem nome"
        new Competencia(null)

        then: "deve lançar exceção"
        thrown(IllegalArgumentException)
    }

    def "não deve criar competência com nome vazio"() {
        when: "tento criar uma competência com nome vazio"
        new Competencia("   ")

        then: "deve lançar exceção"
        thrown(IllegalArgumentException)
    }
}

