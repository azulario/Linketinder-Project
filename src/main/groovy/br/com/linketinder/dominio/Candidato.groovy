package br.com.linketinder.dominio

import br.com.linketinder.dto.DadosPessoais
import br.com.linketinder.dto.PerfilPublico
import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes = ['id'])
class Candidato{
    final String id
    final String nome
    final String email
    final String telefone
    final List<String> competencias

    Candidato(String id, String nome, String email, String telefone, List<String> competencias) {
        this.id = id
        this.nome = nome
        this.email = email
        this.telefone = telefone
        this.competencias = competencias
    }

    PerfilPublico obterPerfilPublico() {
        return new PerfilPublico(id, TipoUsuario.CANDIDATO, competencias)
    }

    DadosPessoais obterDadosPessoais() {
        return new DadosPessoais(id, nome, email, telefone)
    }
}

