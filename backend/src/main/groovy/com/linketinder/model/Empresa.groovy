package com.linketinder.model

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.time.LocalDateTime

@EqualsAndHashCode(includes = ['id'])
@ToString(includeNames = true, includeFields = true)
class Empresa implements Usuarios {
    Integer id
    String nome
    String email
    String cnpj
    String senha
    Integer enderecoId
    Endereco endereco
    String descricao
    LocalDateTime criadoEm
    List<Vaga> vagas = []
    List<Candidato> candidatosCurtidos = []

    Empresa(String nome, String email, String cnpj, String descricao) {
        this.nome = nome
        this.email = email
        this.cnpj = cnpj
        this.descricao = descricao
        this.vagas = []
        this.candidatosCurtidos = []
    }

    void adicionarVaga(Vaga vaga) {
        vagas.add(vaga)
    }

    Integer getNumeroVagas() {
        return vagas.size()
    }

    Integer getNumeroCurtidas() {
        return candidatosCurtidos.size()
    }

    void curtirCandidato(Candidato candidato) {
        if (!candidatosCurtidos.contains(candidato)) {
            candidatosCurtidos.add(candidato)
        }
    }
}
