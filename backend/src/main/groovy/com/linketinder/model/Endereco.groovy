package com.linketinder.model

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.time.LocalDateTime

@EqualsAndHashCode(includes = ['id'])
@ToString(includeNames = true, includeFields = true)
class Endereco {
    Integer id
    String cep
    String estado
    String cidade
    String pais
    LocalDateTime criadoEm

    Endereco(String pais, String estado, String cidade, String cep) {
        this.pais = pais
        this.estado = estado
        this.cidade = cidade
        this.cep = cep
    }

    String getEnderecoCompleto() {
        List<String> partes = []
        if (cidade) partes << cidade
        if (estado) partes << estado
        if (pais) partes << pais
        if (cep) partes << "CEP: ${cep}"
        return partes.join(", ")
    }
}

