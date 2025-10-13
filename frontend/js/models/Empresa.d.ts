import type { Usuario } from './Usuario';
import type { Vaga } from './Vaga';
import type { Candidato } from './Candidato';
export declare class Empresa implements Usuario {
    id: string;
    nomeOuRazao: string;
    cpfOuCnpj: string;
    email: string;
    fotoUrl: string;
    vagas: Vaga[];
    candidatosCurtidos: Candidato[];
    constructor(id: string, nomeOuRazao: string, cpfOuCnpj: string, email: string, fotoUrl: string);
    publicarVaga(vaga: Vaga): void;
    curtirCandidato(candidato: Candidato): void;
}
//# sourceMappingURL=Empresa.d.ts.map