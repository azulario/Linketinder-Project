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
    String cep
    String descricao

    List<String> validar() {
        List<String> erros = []

        if (!nome || nome.trim().isEmpty()) {
            erros.add("Nome da empresa é obrigatório.")
        }

        if (!email || !email.contains("@")) {
            erros.add("Email inválido.")
        }

        if (!cnpj || cnpj.replaceAll("\\D", "").length() != 14) {
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

        if (!cep || cep.trim().isEmpty()) {
            erros.add("CEP é obrigatório.")
        }

        if (!descricao || descricao.trim().isEmpty()) {
            erros.add("Descrição é obrigatória.")
        }

        return erros
    }
}

