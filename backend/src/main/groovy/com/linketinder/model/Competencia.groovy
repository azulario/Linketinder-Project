package com.linketinder.model

import groovy.transform.ToString
import groovy.transform.EqualsAndHashCode

@ToString(includeNames = true, includePackage = false)
@EqualsAndHashCode
class Competencia {
    Integer idCompetencias
    String nomeCompetencia
    Date criadoEm

    Competencia(String nomeCompetencia) {
        this.nomeCompetencia = nomeCompetencia
        this.criadoEm = new Date()
    }

    Competencia(Integer idCompetencias, String nomeCompetencia, Date criadoEm) {
        this.idCompetencias = idCompetencias
        this.nomeCompetencia = nomeCompetencia
        this.criadoEm = criadoEm
    }

    void exibirInfo() {
        println "Competência: ${nomeCompetencia}"
        println "ID: ${idCompetencias ?: 'Não cadastrada'}"
    }
}

