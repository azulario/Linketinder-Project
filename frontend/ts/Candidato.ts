import type { Usuario } from './Usuario';

export class Candidato implements Usuario {
    private curtidos: string[] = [];

    constructor(
        public id: string,
        public nomeOuRazao: string,
        public email: string,
        public fotoUrl: string,
        public competencias: string[]
    ) {}

    curtirPerfil(perfilId: string): void {
        if (!this.curtidos.includes(perfilId)) {
            this.curtidos.push(perfilId);
            console.log(`Candidato ${this.nomeOuRazao} curtiu o perfil ${perfilId}`);
        } else {
            console.log(`Candidato ${this.nomeOuRazao} já curtiu o perfil ${perfilId}`);
        }
    }

    getCurtidos(): string[] {
        return this.curtidos;
    }
}
