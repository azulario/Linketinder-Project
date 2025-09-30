import { Empresa } from "./Empresa";

 export class Vaga {
    id: string;
    titulo: string;
    competencias: string[];
    empresa: Empresa;
    constructor(id: string, titulo: string, competencias: string[] , empresa: Empresa) {
        this.id = id;
        this.titulo = titulo;
        this.competencias = competencias;
        this.empresa = empresa;
    }
}