class Empresa extends PessoaImpl {
    String cnpj
    String pais

    Empresa(String nome, String email, String estado, String cep, String descricao, List<String> competencias, String cnpj, String pais) {
        this.nome = nome
        this.email = email
        this.estado = estado
        this.cep = cep
        this.descricao = descricao
        this.competencias = competencias
        this.cnpj = cnpj
        this.pais = pais
    }
}

