package com.linketinder.dto

import groovy.transform.CompileStatic


@CompileStatic
class VagaDTO {
    String titulo
    String descricao
    String pais
    String estado
    String cidade
    List<String> competenciasRequeridas

    List<String> validar() {
        List<String> erros = []

        if (!titulo || titulo.trim().isEmpty()) {
            erros.add("Título é obrigatório.")
        }

        if (!descricao || descricao.trim().isEmpty()) {
            erros.add("Descrição é obrigatória.")
        }

        if (!pais || pais.trim().isEmpty()) {
            erros.add("País é obrigatório.")
        }

        if (!estado || estado.trim().isEmpty()) {
            erros.add("Estado é obrigatório.")
        }

        if (!cidade || cidade.trim().isEmpty()) {
            erros.add("Cidade é obrigatória.")
        }

        if (!competenciasRequeridas || competenciasRequeridas.isEmpty()) {
            erros.add("Pelo menos uma competência requerida é obrigatória.")
        }

        return erros
    }
}
