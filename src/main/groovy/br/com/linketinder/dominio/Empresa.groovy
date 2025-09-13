package br.com.linketinder.dominio

import br.com.linketinder.dto.DadosPessoais
import br.com.linketinder.dto.PerfilPublico
import groovy.transform.EqualsAndHashCode



@EqualsAndHashCode(includes = ['id'])
class Empresa {
    final String id
    final String razaoSocial
    final String email
    final String telefone
    final List<String> competenciasBuscadas

    Empresa(String id, String razaoSocial, String email, String telefone, List<String> competenciasBuscadas) {
        this.id = id
        this.razaoSocial = razaoSocial
        this.email = email
        this.telefone = telefone
        this.competenciasBuscadas = new ArrayList<>(competenciasBuscadas)
    }

    PerfilPublico obterPerfilPublico() {
       new PerfilPublico(id, TipoUsuario.EMPRESA as String,competenciasBuscadas)
    }

    DadosPessoais obterDadosPessoais() {
        new DadosPessoais(id,razaoSocial,email,telefone)
    }
}


