package com.linketinder.model

import java.time.LocalDate
import java.time.LocalDateTime

class Candidato implements Usuarios {
    Integer id
    String nome
    String sobrenome
    String email
    String cpf
    String senha
    LocalDate dataDeNascimento
    String estado
    String cep
    String descricao
    LocalDateTime criadoEm
    List<String> competencias = []
    List<Vaga> vagasCurtidas = []

    // Construtor único - usado pelo Menu e pelo DAO
    Candidato(String nome, String sobrenome, String email, String cpf, LocalDate dataDeNascimento, String estado, String cep, String descricao, List<String> competencias) {
        this.nome = nome
        this.sobrenome = sobrenome
        this.email = email
        this.cpf = cpf
        this.dataDeNascimento = dataDeNascimento
        this.estado = estado
        this.cep = cep
        this.descricao = descricao
        this.competencias = competencias
        this.criadoEm = LocalDateTime.now()
    }

    // metodo para calcular idade dinamicamente
    Integer getIdade() {
        if (dataDeNascimento == null) return null
        return LocalDate.now().getYear() - dataDeNascimento.getYear()
    }

    void curtirVaga(Vaga vaga) {
        if (!vagasCurtidas.contains(vaga)) {
            vagasCurtidas.add(vaga)
            vaga.receberCurtida(this)
        }
    }

    Integer getTotalVagasCurtidas() {
        return vagasCurtidas.size()
    }

    @Override
    void exibirInfo() {
        println "=" * 50
        println "CANDIDATO"
        if (id) println "ID: $id"
        println "Nome: $nome"
        println "Sobrenome: $sobrenome"
        println "Email: $email"
        println "CPF: $cpf"
        println "Idade: $idade"
        println "Data de Nascimento: $dataDeNascimento"
        println "Estado: $estado"
        println "CEP: $cep"
        println "Descrição: $descricao"
        println "Competências: ${competencias.join(', ')}"
        if (criadoEm) println "Cadastrado em: $criadoEm"
        println "=" * 50

    }

}
