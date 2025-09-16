package br.com.linketinder.servico

import br.com.linketinder.dominio.Candidato
import br.com.linketinder.dominio.Empresa

class CadastroService {
    boolean adicionarCandidato(Candidato candidato, List<Candidato> candidatos) {
        if (candidatos.any { it.id == candidato.id }) {
            return false
        }
        candidatos.add(candidato)
        return true
    }

    boolean adicionarEmpresa(Empresa empresa, List<Empresa> empresas) {
        if (empresas.any { it.id == empresa.id }) {
            return false
        }
        empresas.add(empresa)
        return true
    }
}

