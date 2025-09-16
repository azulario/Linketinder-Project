package br.com.linketinder.servico

import br.com.linketinder.dominio.Candidato
import br.com.linketinder.dominio.Empresa
import br.com.linketinder.dto.DadosPessoais
import br.com.linketinder.dto.PerfilPublico

import java.util.Collections
import java.util.HashMap
import java.util.HashSet
import java.util.List
import java.util.Map
import java.util.Optional
import java.util.Set

class SistemaMatch {
    private final Map<String, Set<String>> likesCandParaEmpresa = new HashMap<>()
    private final Map<String, Set<String>> likesEmpParaCandidato = new HashMap<>()

    List<Candidato> candidatos = []
    List<Empresa> empresas = []

    SistemaMatch(List<Candidato> candidatos, List<Empresa> empresas) {
        this.candidatos = candidatos
        this.empresas = empresas
    }

    SistemaMatch() {}

    PerfilPublico verPerfilPublico(Candidato candidato) {
        candidato.obterPerfilPublico()
    }

    PerfilPublico verPerfilPublico(Empresa empresa) {
        empresa.obterPerfilPublico()
    }

    void curtir(Candidato candidato, Empresa empresa) {
        likesCandParaEmpresa.computeIfAbsent(candidato.getId(), { new HashSet<>() }).add(empresa.getId())
    }

    void curtir(Empresa empresa, Candidato candidato) {
        likesEmpParaCandidato.computeIfAbsent(empresa.getId(), { new HashSet<>() }).add(candidato.getId())
    }
    // Verifica se houve um match entre o candidato e a empresa.
    // Um match ocorre quando o candidato curtiu a empresa e a empresa também curtiu o candidato

    boolean deuMatch(Candidato candidato, Empresa empresa) {
        boolean candCurt = likesCandParaEmpresa.getOrDefault(candidato.getId(), Collections.emptySet()).contains(empresa.getId())
        boolean empCurtiu = likesEmpParaCandidato.getOrDefault(empresa.getId(), Collections.emptySet()).contains(candidato.getId())
        return candCurt && empCurtiu
    }

    Optional<DadosPessoais> dadossPosMatch(Object solicitante, Object outro) {
        if (solicitante instanceof Candidato && outro instanceof Empresa) {
            Candidato candidato = (Candidato) solicitante
            Empresa empresa = (Empresa) outro
            return deuMatch(candidato, empresa) ? Optional.of(candidato.obterDadosPessoais()) : Optional.empty()
        }
        if (solicitante instanceof Empresa && outro instanceof Candidato) {
            Empresa empresa = (Empresa) solicitante
            Candidato candidato = (Candidato) outro
            return deuMatch(candidato, empresa) ? Optional.of(empresa.obterDadosPessoais()) : Optional.empty()
        }
        return Optional.empty()
    }
}
