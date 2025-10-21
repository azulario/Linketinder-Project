package com.linketinder.model

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.time.LocalDateTime

@EqualsAndHashCode(includes = ['id'])
@ToString(includeNames = true, includeFields = true)
class Competencia {
    Integer id
    String nomeCompetencia
    LocalDateTime criadoEm

    Competencia(String nomeCompetencia) {
        if (!nomeCompetencia?.trim()) {
            throw new IllegalArgumentException("Nome da competência não pode ser vazio")
        }
        this.nomeCompetencia = nomeCompetencia.trim()
    }
}

