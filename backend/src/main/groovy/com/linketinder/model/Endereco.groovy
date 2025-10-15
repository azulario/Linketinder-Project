package com.linketinder.model

import java.time.LocalDateTime

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
        this.criadoEm = LocalDateTime.now()
    }

    String getEnderecoCompleto() {
        List<String> partes = []
        if (cidade) partes << cidade
        if (estado) partes << estado
        if (pais) partes << pais
        if (cep) partes << "CEP: ${cep}"
        return partes.join(", ")
    }

    @Override
    String toString() {
        return getEnderecoCompleto()
    }
}

