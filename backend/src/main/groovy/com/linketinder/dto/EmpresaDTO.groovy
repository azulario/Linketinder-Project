package com.linketinder.dto

import groovy.transform.CompileStatic

@CompileStatic
class EmpresaDTO {
    String nome
    String email
    String cnpj
    String pais
    String estado
    String cidade
    String descricao
    List<String> competenciasRequeridas

    List<String> validar() {
        List<String> erros = []

        if (!nome || nome.trim().isEmpty()) {
            erros.add("Nome é obrigatório.")
        }

        if (!email || !email.contains("@")) {
            erros.add("Email inválido.")
        }

        if (!cnpj || cnpj.trim().length() != 14) {
            erros.add("CNPJ deve ter 14 dígitos.")
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

        if (!descricao || descricao.trim().isEmpty()) {
            erros.add("Descrição é obrigatória.")
        }

        if (!competenciasRequeridas || competenciasRequeridas.isEmpty()) {
            erros.add("Pelo menos uma competência requerida é obrigatória.")
        }

        return erros
    }
}
