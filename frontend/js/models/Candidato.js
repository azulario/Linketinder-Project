export class Candidato {
    id;
    nomeOuRazao;
    cpfOuCnpj;
    email;
    fotoUrl;
    competencias;
    vagasCurtidas;
    constructor(id, nomeOuRazao, cpfOuCnpj, email, fotoUrl, competencias, vagasCurtidas = []) {
        this.id = id;
        this.nomeOuRazao = nomeOuRazao;
        this.cpfOuCnpj = cpfOuCnpj;
        this.email = email;
        this.fotoUrl = fotoUrl;
        this.competencias = competencias;
        this.vagasCurtidas = vagasCurtidas;
        this.id = id;
        this.nomeOuRazao = nomeOuRazao;
        this.cpfOuCnpj = cpfOuCnpj;
        this.email = email;
        this.fotoUrl = fotoUrl;
        this.competencias = competencias;
        this.vagasCurtidas = vagasCurtidas;
    }
    curtirVaga(vaga) {
        this.vagasCurtidas.push(vaga);
    }
}
//# sourceMappingURL=Candidato.js.map