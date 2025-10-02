import type { Usuario } from './Usuario';
import type { Vaga } from './Vaga';
import type { Candidato } from './Candidato';

export class Empresa implements Usuario {
    vagas: Vaga[] = [];
    candidatosCurtidos: Candidato[] = [];

    constructor(public id: string, public nomeOuRazao: string, public cpfOuCnpj: string, public email: string, public fotoUrl: string,
    ) {
        this.id = id;
        this.nomeOuRazao = nomeOuRazao;
        this.cpfOuCnpj = cpfOuCnpj;
        this.email = email;
        this.fotoUrl = fotoUrl;
    }

    publicarVaga(vaga: Vaga) {
        this.vagas.push(vaga);
    }

    curtirCandidato(candidato: Candidato) {
        this.candidatosCurtidos.push(candidato);
    }

}
