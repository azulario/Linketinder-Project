import { Candidato } from "../models/Candidato.js";
import { Vaga } from "../models/Vaga.js";
import { Empresa } from "../models/Empresa.js";

// Simulação de um banco de dados em memória

export class BancoDeDadosFake {
    private candidatos: Candidato[] = [];
    private empresa: Empresa[] = [];
    private vagas: Vaga[] = [];

    constructor() {
        //o metodo seed popula o banco de dados falso com d     ados iniciais
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
        // remove as vagas associadas à empresa deletada
        this.vagas = this.vagas.filter(v => v.empresa.id !== id);
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
        const candidato5 = new Candidato('uuid-candidato-5', "Eduardo Lima", "edudev@exemplo,com", "https://placehold.co/400x400/34495e/ffffff?text=EL", ['Java, Spring, Microservices']);
        const candidato6 = new Candidato('uuid-candidato-6', "Fernanda Gomes", "fer@exemplo.com", "https://placehold.co/400x400/e67e22/ffffff?text=FG", ['UX Design, Prototipagem, Pesquisa de Usuário']);
        const candidato7 = new Candidato('uuid-candidato-7', "Gabriel Rocha", "biel@email.com", "https://placehold.co/400x400/95a5a6/ffffff?text=GR", ['Marketing Digital, SEO, Google Analytics']);
        const candidato8 = new Candidato('uuid-candidato-8', "Helena Martins", "helenadesign", "https://placehold.co/400x400/d35400/ffffff?text=HM", ['Design Gráfico, Illustrator, Branding']);
        const candidato9 = new Candidato('uuid-candidato-9', "Igor Fernandes", "idevg@email.com", "https://placehold.co/400x400/7f8c8d/ffffff?text=IF", ['DevOps, AWS, Docker']);
        const candidato10 = new Candidato('uuid-candidato-10', "Juliana Alves", "fullstack@email.com", "https://placehold.co/400x400/16a085/ffffff?text=JA", ['JavaScript, Python, SQL']);


        // add aos arrays
        this.empresa.push(empresa1, empresa2);
        this.vagas.push(vaga1, vaga2, vaga3, vaga4);
        this.candidatos.push(candidato1, candidato2, candidato3, candidato4, candidato5, candidato6, candidato7, candidato8, candidato9, candidato10);
    }
}

// exporta uma instancia unica do banco de dados falso
export const bancoDeDadosFake = new BancoDeDadosFake();
