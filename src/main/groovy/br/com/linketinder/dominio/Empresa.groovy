package br.com.linketinder.dominio

import br.com.linketinder.dto.DadosPessoais
import br.com.linketinder.dto.PerfilPublico
import groovy.transform.EqualsAndHashCode



@EqualsAndHashCode(includes = ['id'])
class Empresa {
    String id
    String razaoSocial
    String email
    String telefone
    List<String> competenciasBuscadas

    Empresa() {}

    Empresa(String id, String razaoSocial, String email, String telefone, List<String> competenciasBuscadas) {
        this.id = id
        this.razaoSocial = razaoSocial
        this.email = email
        this.telefone = telefone
        this.competenciasBuscadas = new ArrayList<>(competenciasBuscadas)
    }

    PerfilPublico obterPerfilPublico() {
       new PerfilPublico(razaoSocial, "Empresa", competenciasBuscadas)
    }

    DadosPessoais obterDadosPessoais() {
        new DadosPessoais(razaoSocial, email, telefone)
    }

    String getId() { return this.id }
    String getRazaoSocial() { return this.razaoSocial }
    String getEmail() { return this.email }
    String getTelefone() { return this.telefone }
    List<String> getCompetenciasBuscadas() { return this.competenciasBuscadas }
}
