package com.linketinder.view

import com.linketinder.model.Candidato

/**
 * Formatador de texto para Candidato
 * Responsabilidade única: formatar candidatos para exibição em console
 * Aplica Single Responsibility Principle (SRP)
 */
class CandidatoFormatador implements IFormatador<Candidato> {

    @Override
    String formatar(Candidato candidato) {
        StringBuilder sb = new StringBuilder()

        sb.append("=" * 50).append("\n")
        sb.append("CANDIDATO\n")

        if (candidato.id) {
            sb.append("ID: ${candidato.id}\n")
        }

        sb.append("Nome: ${candidato.nome} ${candidato.sobrenome}\n")
        sb.append("Email: ${candidato.email}\n")
        sb.append("CPF: ${candidato.cpf}\n")
        sb.append("Idade: ${candidato.idade}\n")
        sb.append("Data de Nascimento: ${candidato.dataDeNascimento}\n")

        if (candidato.endereco) {
            sb.append("Endereço: ${candidato.endereco.enderecoCompleto}\n")
        }

        sb.append("Descrição: ${candidato.descricao}\n")
        sb.append("Competências: ${candidato.competencias.join(', ')}\n")

        if (candidato.criadoEm) {
            sb.append("Cadastrado em: ${candidato.criadoEm}\n")
        }

        sb.append("=" * 50)

        return sb.toString()
    }
}

