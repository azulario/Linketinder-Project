package com.linketinder.dto

import groovy.transform.CompileStatic

@CompileStatic
class VagaDTO {
    String titulo
    String descricao
    Integer empresaId
    List<String> competencias = []

    List<String> validar() {
        List<String> erros = []

        if (!titulo || titulo.trim().isEmpty()) {
            erros.add("Título da vaga é obrigatório.")
        }

        if (!descricao || descricao.trim().isEmpty()) {
            erros.add("Descrição da vaga é obrigatória.")
        }

        if (empresaId == null || empresaId <= 0) {
            erros.add("ID da empresa inválido.")
        }

        if (!competencias || competencias.isEmpty()) {
            erros.add("Pelo menos uma competência é obrigatória.")
        }

        return erros
    }
}

