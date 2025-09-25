package br.com.linketinder.dominio

import br.com.linketinder.dto.DadosPessoais
import br.com.linketinder.dto.PerfilPublico
import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes = ['id'])
class Candidato{
    String id
    String nome
    String email
    String telefone
    List<String> competencias

    Candidato(String id, String nome, String email, String telefone, List<String> competencias) {
        this.id = id
        this.nome = nome
        this.email = email
        this.telefone = telefone
        this.competencias = competencias
    }

    PerfilPublico obterPerfilPublico() {
        return new PerfilPublico(nome, "Candidato", competencias)
    }

    DadosPessoais obterDadosPessoais() {
        return new DadosPessoais(nome, email, telefone)
    }

    String getId() { return this.id }
    String getNome() { return this.nome }
    String getEmail() { return this.email }
    String getTelefone() { return this.telefone }
    List<String> getCompetencias() { return this.competencias }
}
