package com.linketinder.model

import java.time.LocalDateTime

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

    Vaga(String titulo, String descricao, List<String> competencias, Empresa empresa) {
        this.titulo = titulo
        this.descricao = descricao
        this.competencias = competencias
        this.empresa = empresa
        this.empresaId = empresa?.id
        this.candidatosCurtiram = []
        this.criadoEm = LocalDateTime.now()
    }

    void receberCurtida(Candidato candidato) {
        if (!candidatosCurtiram.contains(candidato)) {
            candidatosCurtiram.add(candidato)
        }
    }

    Integer getNumeroCurtidas() {
        return candidatosCurtiram.size()
    }

    void exibirInfo() {
        println "=" * 50
        println "VAGA"
        if (id) println "ID: $id"
        println "Título: $titulo"
        println "Empresa: ${empresa.nome}"
        if (endereco) println "Localização: ${endereco.enderecoCompleto}"
        println "Descrição: $descricao"
        println "Competências: ${competencias.join(', ')}"
        println "Curtidas: $numeroCurtidas"
        if (criadoEm) println "Criado em: $criadoEm"
        println "=" * 50
    }
}
