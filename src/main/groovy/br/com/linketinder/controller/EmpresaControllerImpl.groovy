package br.com.linketinder.controller

import br.com.linketinder.dominio.Candidato
import br.com.linketinder.dominio.Empresa
import br.com.linketinder.servico.SistemaMatch

class EmpresaControllerImpl implements EmpresaController {
    private final SistemaMatch sistemaMatch

    EmpresaControllerImpl(SistemaMatch sistemaMatch) {
        this.sistemaMatch = sistemaMatch
    }

    @Override
    boolean curtirCandidato(int idEmpresa, int idCandidato) {
        // Normaliza ids para aceitar tanto número quanto prefixo
        def normalizar = { String id ->
            id?.replaceAll(/^cand-|^emp-/, "")
        }
        def idEmpStr = idEmpresa.toString()
        def idCandStr = idCandidato.toString()
        def empresa = sistemaMatch.empresas.find { normalizar(it.id) == normalizar(idEmpStr) }
        def candidato = sistemaMatch.candidatos.find { normalizar(it.id) == normalizar(idCandStr) }
        if (empresa && candidato) {
            sistemaMatch.curtir(empresa, candidato)
            println "Empresa ${empresa.id} curtiu o candidato ${candidato.id}."
            return true
        }
        println "Empresa ou candidato não encontrado. IDs buscados: ${idEmpresa} / ${idCandidato}"
        return false
    }
}
