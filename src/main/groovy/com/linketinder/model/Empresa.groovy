package com.linketinder.model

class Empresa implements Usuarios {
    String nome
    String email
    String cnpj
    String pais
    String estado
    String cep
    String descricao
    List<String> competencias = []
    List<Vaga> vagas = []
    List<Candidato> candidatosCurtidos

    Empresa(String nome, String email, String cnpj, String pais, String estado, String cep, String descricao, List<String> competencias) {
        this.nome = nome
        this.email = email
        this.cnpj = cnpj
        this.pais = pais
        this.estado = estado
        this.cep = cep
        this.descricao = descricao
        this.competencias = competencias
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
        } //traduzindo: se o candidato ainda não foi curtido, adiciona ele na lista de candidatos que foram curtidos
    }

    //override é usado para sobrescrever um metodo de uma classe pai ou interface implementada
    @Override
    void exibirInfo() {
        println "=" * 50
        println "EMPRESA"
        println "Nome: $nome"
        println "Email: $email"
        println "CNPJ: $cnpj"
        println "País: $pais"
        println "Estado: $estado"
        println "CEP: $cep"
        println "Descrição: $descricao"
        println "Competências buscadas: ${competencias.join(', ')}"
        println "=" * 50
    }
}
