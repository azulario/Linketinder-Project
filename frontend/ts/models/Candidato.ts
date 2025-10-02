import type { Usuario } from './Usuario';
import type { Vaga } from './Vaga';

export class Candidato implements Usuario {

    constructor(public id: string, public nomeOuRazao: string, public cpfOuCnpj: string , public email: string, public fotoUrl: string, public competencias: string[],
    public vagasCurtidas: Vaga[] = []
    ) {
        this.id = id;
        this.nomeOuRazao = nomeOuRazao;
        this.cpfOuCnpj = cpfOuCnpj;
        this.email = email;
        this.fotoUrl = fotoUrl;
        this.competencias = competencias;
        this.vagasCurtidas = vagasCurtidas;
    }

    curtirVaga(vaga : Vaga) {
        this.vagasCurtidas.push(vaga);
    }

}
