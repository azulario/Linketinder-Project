package com.linketinder.model

import java.time.LocalDateTime

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

    // Construtor único - usado pelo Menu e pelo DAO
    Empresa(String nome, String email, String cnpj, String descricao) {
        this.nome = nome
        this.email = email
        this.cnpj = cnpj
        this.descricao = descricao
        this.vagas = []
        this.candidatosCurtidos = []
        this.criadoEm = LocalDateTime.now()
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

    //override é usado para sobrescrever um metodo de uma classe pai ou interface implementada
    @Override
    void exibirInfo() {
        println "=" * 50
        println "EMPRESA"
        if (id) println "ID: $id"
        println "Nome: $nome"
        println "Email: $email"
        println "CNPJ: $cnpj"
        if (endereco) println "Endereço: ${endereco.enderecoCompleto}"
        println "Descrição: $descricao"
        if (criadoEm) println "Cadastrado em: $criadoEm"
        println "=" * 50
    }
}
