import { Candidato } from "../models/Candidato";
import { Vaga } from "../models/Vaga";
import { Empresa } from "../models/Empresa";
import {c} from "vite/dist/node/types.d-aGj9QkWt";

// Simulação de um banco de dados em memória

export class BancoDeDadosFake {
    private candidatos: Candidato[] = [];
    private empresa: Empresa[] = [];
    private vagas: Vaga[] = [];

    constructor() {
        //o metodo seed popula o banco de dados falso com dados iniciais
        this.seed();
    }

    // metodos publicos pra acessar os dados

    public getCandidatos = (): Candidato[] => this.candidatos;
    public getEmpresas = (): Empresa[] => this.empresa;
    public getVagas = (): Vaga[] => this.vagas;


// add um novo candidato a lista
    public addCandidato = (candidato: Candidato): void => {
        this.candidatos.push(candidato);
        console.log('Candidato no banco de dados: ', this.candidatos);
    };

// add uma nova empresa a lista
    public addEmpresa = (empresa: Empresa): void => {
        this.empresa.push(empresa);
        console.log('Empresa no banco de dados: ', this.empresa);
    };

// deleta um candidato pelo id
    public deleteCandidato = (id: string): void => {
        // filtra a lista de candidatos removendo o candidato com o id especificado
        this.candidatos = this.candidatos.filter(c => c.id !== id);
    };

// deleta uma empresa pelo id
    public deleteEmpresa = (id: string): void => {
        // filtra a lista de empresas removendo a empresa com o id especificado
        this.empresa = this.empresa.filter(e => e.id !== id);
        this.vagas = this.vagas.filter(v => v.id !== id); // remove as vagas associadas a empresa deletada
    };

// Dados iniciais (SEED)
    private seed(): void {
        // empresas
        const empresa1 = new Empresa("uuid-empresa-1", "Tech Solutions", "contato@techsolution.com", "https://placehold.co/400x400/3498db/ffffff?text=TS");
        const empresa2 = new Empresa("uuid-empresa-2", "Innovatech", "rh@inovacorp.com", "https://placehold.co/400x400/2ecc71/ffffff?text=IT");

        // vagas
        const vaga1 = new Vaga('uuid-vaga-1', "Desenvolvedor Frontend", ['Figma, Pacote Adobe, CSS'], empresa2);
        const vaga2 = new Vaga('uuid-vaga-2', "Analista de Dados",  ['Excel, SQL, Python'], empresa2);
        const vaga3 = new Vaga('uuid-vaga-3', "Gerente de Projetos",  ['Scrum, Kanban, MS Project'], empresa1);
        const vaga4 = new Vaga('uuid-vaga-4', "Especialista em Cibersegurança",  ['Firewalls, Criptografia, Análise de Vulnerabilidades'], empresa1);

        // candidatos
        const candidato1 = new Candidato('uuid-candidato-1', "Ana Silva", "ana.silva@exemplo.com", "https://placehold.co/400x400/e74c3c/ffffff?text=AS", ['JavaScript, React, Node.js']);
        const candidato2 = new Candidato('uuid-candidato-2', "Bruno Souza", "bruno.souza@email.com", "https://placehold.co/400x400/f1c40f/ffffff?text=BS", ['Python, R, SQL']);
        const candidato3 = new Candidato('uuid-candidato-3', "Carla Pereira", "carla.p@email.com", "https://placehold.co/400x400/9b59b6/ffffff?text=CP", ['Gestão de Projetos, Scrum, Kanban']);
        const candidato4 = new Candidato('uuid-candidato-4', "Daniel Costa", "d.c@email.com", "https://placehold.co/400x400/1abc9c/ffffff?text=DC", ['Segurança da Informação, Redes, Criptografia']);


        // add aos arrays
        this.empresa.push(empresa1, empresa2);
        this.vagas.push(vaga1, vaga2, vaga3, vaga4);
        this.candidatos.push(candidato1, candidato2, candidato3, candidato4);
    }
}

// exporta uma instancia unica do banco de dados falso
export const bancoDeDadosFake = new BancoDeDadosFake();


