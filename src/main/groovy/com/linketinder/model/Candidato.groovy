package com.linketinder.model

class Candidato implements Usuarios {
    String nome
    String email
    String cpf
    Integer idade
    String estado
    String cep
    String descricao
    List<String> competencias = []

    Candidato(String nome, String email, String cpf, Integer idade, String estado, String cep, String descricao, List<String> competencias) {
        this.nome = nome
        this.email = email
        this.cpf = cpf
        this.idade = idade
        this.estado = estado
        this.cep = cep
        this.descricao = descricao
        this.competencias = competencias

    }

    @Override
    void exibirInfo() {
        println "=" * 50
        println "CANDIDATO"
        println "Nome: $nome"
        println "Email: $email"
        println "CPF: $cpf"
        println "Idade: $idade"
        println "Estado: $estado"
        println "CEP: $cep"
        println "Descrição: $descricao"
        println "Competências: ${competencias.join(', ')}"
        println "=" * 50

    }

}
