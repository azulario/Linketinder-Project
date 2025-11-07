package com.linketinder.dto

import groovy.transform.CompileStatic
import java.time.LocalDate

@CompileStatic
class CandidatoDTO {
    String nome
    String email
    String cpf
    LocalDate dataNascimento
    String pais
    String estado
    String cidade
    String descricao
    List<String> competencias

    List<String> validar() {
        List<String> erros = []

        if (!nome || nome.trim().isEmpty()) {
            erros.add("Nome é obrigatório.")
        }

        if (!email || !email.contains("@")) {
            erros.add("Email inválido.")

        }

        if (!cpf || cpf.trim().length() != 11) {
            erros.add("CPF deve ter 11 dígitos.")

        }

        if (!dataNascimento || dataNascimento.isAfter(LocalDate.now())) {
            erros.add("Data de nascimento inválida.")
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
