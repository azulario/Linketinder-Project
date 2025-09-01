class Candidato extends PessoaImpl {
    String cpf
    int idade

    Candidato(String nome, String email, String estado, String cep, String descricao, List<String> competencias, String cpf, int idade) {
        this.nome = nome
        this.email = email
        this.estado = estado
        this.cep = cep
        this.descricao = descricao
        this.competencias = competencias
        this.cpf = cpf
        this.idade = idade
    }
}

