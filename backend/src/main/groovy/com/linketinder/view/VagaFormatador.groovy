package com.linketinder.view

import com.linketinder.model.Vaga

class VagaFormatador implements IFormatador<Vaga> {

    @Override
    String formatar(Vaga vaga) {
        StringBuilder sb = new StringBuilder()

        sb.append("=" * 50).append("\n")
        sb.append("VAGA\n")

        if (vaga.id) {
            sb.append("ID: ${vaga.id}\n")
        }

        sb.append("Título: ${vaga.titulo}\n")

        if (vaga.empresa) {
            sb.append("Empresa: ${vaga.empresa.nome}\n")
        }

        if (vaga.endereco) {
            sb.append("Localização: ${vaga.endereco.enderecoCompleto}\n")
        }

        sb.append("Descrição: ${vaga.descricao}\n")
        sb.append("Competências: ${vaga.competencias.join(', ')}\n")
        sb.append("Curtidas: ${vaga.numeroCurtidas}\n")

        if (vaga.criadoEm) {
            sb.append("Criado em: ${vaga.criadoEm}\n")
        }

        sb.append("=" * 50)

        return sb.toString()
    }
}
