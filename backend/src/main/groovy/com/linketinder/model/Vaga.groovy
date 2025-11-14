package com.linketinder.model

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.time.LocalDateTime

@EqualsAndHashCode(includes = ['id'])
@ToString(includeNames = true, includeFields = true)
class Vaga {
    Integer id
    String titulo
    String descricao
    Integer enderecoId
    Endereco endereco
    Integer empresaId
    LocalDateTime criadoEm
    List<String> competencias
    Empresa empresa
    List<Candidato> candidatosCurtiram = []

    Vaga() {
    }

    Vaga(String titulo, String descricao, List<String> competencias, Empresa empresa) {
        this.titulo = titulo
        this.descricao = descricao
        this.competencias = competencias
        this.empresa = empresa
        this.empresaId = empresa?.id
        this.candidatosCurtiram = []
        this.criadoEm = LocalDateTime.now()
    }

    Vaga(String titulo, String descricao, Empresa empresa) {
        this.titulo = titulo
        this.descricao = descricao
        this.empresa = empresa
        this.empresaId = empresa?.id
        this.competencias = []
        this.candidatosCurtiram = []
        this.criadoEm = LocalDateTime.now()
    }


    Vaga(String titulo, String descricao, Integer empresaId) {
        this.titulo = titulo
        this.descricao = descricao
        this.empresaId = empresaId
        this.competencias = []
        this.candidatosCurtiram = []
    }

    void receberCurtida(Candidato candidato) {
        if (!candidatosCurtiram.contains(candidato)) {
            candidatosCurtiram.add(candidato)
        }
    }

    Integer getNumeroCurtidas() {
        return candidatosCurtiram.size()
    }
}
