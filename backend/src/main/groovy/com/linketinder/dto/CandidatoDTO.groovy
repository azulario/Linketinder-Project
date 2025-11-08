package com.linketinder.dto

import groovy.transform.CompileStatic

@CompileStatic
class CandidatoDTO {
    String nome
    String sobrenome
    String email
    String cpf
    String dataDeNascimento  // String porque vem do formulário
    String cep
    String pais
    String estado
    String cidade
    String descricao
    List<String> competencias = []

    List<String> validar() {
        List<String> erros = []

        if (!nome || nome.trim().isEmpty()) {
            erros.add("Nome é obrigatório.")
        }

        if (!sobrenome || sobrenome.trim().isEmpty()) {
            erros.add("Sobrenome é obrigatório.")
        }

        if (!email || !email.contains("@")) {
            erros.add("Email inválido.")
        }

        if (!cpf || cpf.replaceAll("\\D", "").length() != 11) {
            erros.add("CPF deve ter 11 dígitos.")
        }

        if (!dataDeNascimento || dataDeNascimento.trim().isEmpty()) {
            erros.add("Data de nascimento é obrigatória.")
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

        if (!competencias || competencias.isEmpty()) {
            erros.add("Pelo menos uma competência é obrigatória.")
        }

        return erros

    }
}
