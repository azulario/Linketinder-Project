package br.com.linketinder.dto


class DadosPessoais {
    final String nomeOuRazao
    final String email
    final String telefone

    DadosPessoais(String nomeOuRazao, String email, String telefone) {
        this.nomeOuRazao = nomeOuRazao
        this.email = email
        this.telefone = telefone
    }
}