export class Empresa {
    id;
    nomeOuRazao;
    email;
    fotoUrl;
    vagas = [];
    candidatosCurtidos = [];
    constructor(id, nomeOuRazao, email, fotoUrl) {
        this.id = id;
        this.nomeOuRazao = nomeOuRazao;
        this.email = email;
        this.fotoUrl = fotoUrl;
        this.id = id;
        this.nomeOuRazao = nomeOuRazao;
        this.email = email;
        this.fotoUrl = fotoUrl;
    }
    publicarVaga(vaga) {
        this.vagas.push(vaga);
    }
    curtirCandidato(candidato) {
        this.candidatosCurtidos.push(candidato);
    }
}
//# sourceMappingURL=Empresa.js.map