class Curtida {
    Candidato candidato
    Empresa empresa
    String competencia // Vaga ou skill relacionada à curtida
    boolean match = false

    Curtida(Candidato candidato, Empresa empresa, String competencia) {
        this.candidato = candidato
        this.empresa = empresa
        this.competencia = competencia
    }
}

