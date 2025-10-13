package com.linketinder.model

class Vaga {
    String titulo
    String descricao
    List<String> competencias
    Empresa empresa
    List<Candidato> candidatosCurtiram

    Vaga(String titulo, String descricao, List<String> competencias, Empresa empresa) {
        this.titulo = titulo
        this.descricao = descricao
        this.competencias = competencias
        this.empresa = empresa
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

    void exibirInfo() {
        println "=" * 50
        println "VAGA"
        println "Título: $titulo"
        println "Empresa: ${empresa.nome}"
        println "Descrição: $descricao"
        println "Competências: ${competencias.join(', ')}"
        println "Curtidas: $numeroCurtidas"
        println "=" * 50
    }

}
