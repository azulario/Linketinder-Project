abstract class PessoaImpl implements Pessoa {
    String nome
    String email
    String estado
    String cep
    String descricao
    List<String> competencias

    String getNome() { return nome }
    String getEmail() { return email }
    String getEstado() { return estado }
    String getCep() { return cep }
    String getDescricao() { return descricao }
    List<String> getCompetencias() { return competencias }
}

