package br.com.linketinder.dto

import br.com.linketinder.dominio.TipoUsuario

class PerfilPublico {
    String nome
    String descricao
    List<String> competencias

    PerfilPublico(String nome, String descricao, List<String> competencias) {
        this.nome = nome
        this.descricao = descricao
        this.competencias = competencias
    }
}
