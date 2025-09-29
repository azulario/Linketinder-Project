export interface Usuario {
    id: string;
    nomeOuRazao: string;
    email: string;
    fotoUrl: string;
    competencias: string[];
    curtirPerfil(perfilId: string): void;
}