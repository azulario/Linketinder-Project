package com.linketinder.view

import com.linketinder.model.Empresa

/**
 * Formatador de texto para Empresa
 * Responsabilidade única: formatar empresas para exibição em console
 * Aplica Single Responsibility Principle (SRP)
 */
class EmpresaFormatador implements IFormatador<Empresa> {

    @Override
    String formatar(Empresa empresa) {
        StringBuilder sb = new StringBuilder()

        sb.append("=" * 50).append("\n")
        sb.append("EMPRESA\n")

        if (empresa.id) {
            sb.append("ID: ${empresa.id}\n")
        }

        sb.append("Nome: ${empresa.nome}\n")
        sb.append("Email: ${empresa.email}\n")
        sb.append("CNPJ: ${empresa.cnpj}\n")

        if (empresa.endereco) {
            sb.append("Endereço: ${empresa.endereco.enderecoCompleto}\n")
        }

        sb.append("Descrição: ${empresa.descricao}\n")

        if (empresa.criadoEm) {
            sb.append("Cadastrado em: ${empresa.criadoEm}\n")
        }

        sb.append("=" * 50)

        return sb.toString()
    }
}

