import type { Usuario } from './Usuario';
import type { Vaga } from './Vaga';
export declare class Candidato implements Usuario {
    id: string;
    nomeOuRazao: string;
    email: string;
    fotoUrl: string;
    competencias: string[];
    vagasCurtidas: Vaga[];
    constructor(id: string, nomeOuRazao: string, email: string, fotoUrl: string, competencias: string[], vagasCurtidas?: Vaga[]);
    curtirVaga(vaga: Vaga): void;
}
//# sourceMappingURL=Candidato.d.ts.map