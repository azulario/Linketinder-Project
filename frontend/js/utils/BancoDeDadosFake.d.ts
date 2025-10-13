import { Candidato } from "../models/Candidato.js";
import { Vaga } from "../models/Vaga.js";
import { Empresa } from "../models/Empresa.js";
export declare class BancoDeDadosFake {
    private candidatos;
    private empresas;
    private vagas;
    private readonly storageKeyCandidatos;
    private readonly storageKeyEmpresas;
    constructor();
    /**
     * Salva as listas de candidatos e empresas no localStorage.
     * O localStorage só armazena texto, então convertemos os objetos para uma string JSON.
     */
    private salvarNoLocalStorage;
    /**
     * Carrega os dados do localStorage.
     * Se existirem dados, eles são convertidos de string JSON de volta para objetos.
     */
    private carregarDoLocalStorage;
    getCandidatos(): Candidato[];
    addCandidato(candidato: Candidato): void;
    deleteCandidato(id: string): void;
    getEmpresas(): Empresa[];
    addEmpresa(empresa: Empresa): void;
    deleteEmpresa(id: string): void;
    getVagas(): Vaga[];
    private seed;
}
export declare const bancoDeDadosFake: BancoDeDadosFake;
//# sourceMappingURL=BancoDeDadosFake.d.ts.map