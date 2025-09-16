package br.com.linketinder.controller

import br.com.linketinder.dominio.Candidato
import br.com.linketinder.dominio.Empresa
import br.com.linketinder.servico.SistemaMatch

class CandidatoControllerImpl implements CandidatoController {
    private final SistemaMatch sistemaMatch

    CandidatoControllerImpl(SistemaMatch sistemaMatch) {
        this.sistemaMatch = sistemaMatch
    }

    @Override
    boolean curtirVaga(String idCandidato, String idVaga) {
        // Aceita id numérico, string, com ou sem prefixo
        def normalizar = { String id ->
            id?.replaceAll(/^cand-|^emp-/, "")
        }
        def idCandStr = idCandidato.toString()
        def idEmpStr = idVaga.toString()
        def candidato = sistemaMatch.candidatos.find {
            normalizar(it.id) == normalizar(idCandStr)
        }
        def empresa = sistemaMatch.empresas.find {
            normalizar(it.id) == normalizar(idEmpStr)
        }
        if (candidato && empresa) {
            sistemaMatch.curtir(candidato, empresa)
            println "Candidato ${candidato.id} curtiu a vaga (empresa) ${empresa.id}."
            return true
        }
        println "Candidato ou empresa não encontrado. IDs buscados: ${idCandidato} / ${idVaga}"
        return false
    }

    // Sobrecarga para aceitar int, compatível com testes e chamadas do sistema
    boolean curtirVaga(int idCandidato, int idVaga) {
        return curtirVaga(idCandidato.toString(), idVaga.toString())
    }
}
