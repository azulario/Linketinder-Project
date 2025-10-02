import { Candidato } from "../models/Candidato.js";
import { Vaga } from "../models/Vaga.js";
import { Empresa } from "../models/Empresa.js";
// Simulação de um banco de dados em memória
export class BancoDeDadosFake {
    candidatos = [];
    empresas = [];
    vagas = [];
    // Chaves para salvar os dados no localStorage. É uma boa prática para evitar erros de digitação.
    storageKeyCandidatos = 'linketinder_candidatos';
    storageKeyEmpresas = 'linketinder_empresas';
    constructor() {
        this.carregarDoLocalStorage();
        // Se não houver nada no localStorage, usamos os dados de exemplo (seed).
        if (this.candidatos.length === 0 && this.empresas.length === 0) {
            this.seed();
            this.salvarNoLocalStorage(); // Salva os dados de exemplo na primeira vez
        }
    }
    // --- MÉTODOS DE PERSISTÊNCIA ---
    /**
     * Salva as listas de candidatos e empresas no localStorage.
     * O localStorage só armazena texto, então convertemos os objetos para uma string JSON.
     */
    salvarNoLocalStorage() {
        localStorage.setItem(this.storageKeyCandidatos, JSON.stringify(this.candidatos));
        localStorage.setItem(this.storageKeyEmpresas, JSON.stringify(this.empresas));
    }
    /**
     * Carrega os dados do localStorage.
     * Se existirem dados, eles são convertidos de string JSON de volta para objetos.
     */
    carregarDoLocalStorage() {
        const candidatosData = localStorage.getItem(this.storageKeyCandidatos);
        const empresasData = localStorage.getItem(this.storageKeyEmpresas);
        if (candidatosData) {
            this.candidatos = JSON.parse(candidatosData);
        }
        if (empresasData) {
            this.empresas = JSON.parse(empresasData);
        }
    }
    // --- MÉTODOS PARA CANDIDATOS ---
    getCandidatos() {
        return this.candidatos;
    }
    addCandidato(candidato) {
        this.candidatos.push(candidato);
        this.salvarNoLocalStorage(); // <--- SALVA A ALTERAÇÃO
    }
    deleteCandidato(id) {
        this.candidatos = this.candidatos.filter(c => c.id !== id);
        this.salvarNoLocalStorage(); // <--- SALVA A ALTERAÇÃO
    }
    // --- MÉTODOS PARA EMPRESAS ---
    getEmpresas() {
        return this.empresas;
    }
    addEmpresa(empresa) {
        this.empresas.push(empresa);
        this.salvarNoLocalStorage(); // <--- SALVA A ALTERAÇÃO
    }
    deleteEmpresa(id) {
        this.empresas = this.empresas.filter(e => e.id !== id);
        this.salvarNoLocalStorage(); // <--- SALVA A ALTERAÇÃO
    }
    // --- MÉTODOS PARA VAGAS ---
    getVagas() {
        // Por enquanto, as vagas são geradas em memória a partir das empresas
        const vagasDasEmpresas = [];
        this.empresas.forEach(empresa => {
            // Exemplo: cada empresa tem 2 vagas fixas
            vagasDasEmpresas.push(new Vaga(crypto.randomUUID(), `Vaga Frontend na ${empresa.nomeOuRazao}`, ['React', 'TypeScript'], empresa));
            vagasDasEmpresas.push(new Vaga(crypto.randomUUID(), `Vaga Backend na ${empresa.nomeOuRazao}`, ['Java', 'Node.js'], empresa));
        });
        return vagasDasEmpresas;
    }
    // Dados iniciais (SEED)
    seed() {
        // empresas
        const empresa1 = new Empresa("uuid-empresa-1", "Tech Solutions", "12.345.678/0001-10", "contato@techsolution.com", "https://placehold.co/400x400/3498db/ffffff?text=TS");
        const empresa2 = new Empresa("uuid-empresa-2", "Innovatech", "98.765.432/0001-55", "rh@inovacorp.com", "https://placehold.co/400x400/2ecc71/ffffff?text=IT");
        // vagas
        const vaga1 = new Vaga('uuid-vaga-1', "Desenvolvedor Frontend", ['Figma', 'Pacote Adobe', 'CSS'], empresa2);
        const vaga2 = new Vaga('uuid-vaga-2', "Analista de Dados", ['Excel', 'SQL', 'Python'], empresa2);
        const vaga3 = new Vaga('uuid-vaga-3', "Gerente de Projetos", ['Scrum', 'Kanban', 'MS Project'], empresa1);
        const vaga4 = new Vaga('uuid-vaga-4', "Especialista em Cibersegurança", ['Firewalls', 'Criptografia', 'Análise de Vulnerabilidades'], empresa1);
        // candidatos
        const candidato1 = new Candidato('uuid-candidato-1', "Ana Silva", "529.982.247-25", "ana.silva@exemplo.com", "https://placehold.co/400x400/e74c3c/ffffff?text=AS", ['JavaScript', 'React', 'Node.js']);
        const candidato2 = new Candidato('uuid-candidato-2', "Bruno Souza", "145.382.206-20", "bruno.souza@email.com", "https://placehold.co/400x400/f1c40f/ffffff?text=BS", ['Python', 'R', 'SQL']);
        const candidato3 = new Candidato('uuid-candidato-3', "Carla Pereira", "987.654.321-00", "carla.p@email.com", "https://placehold.co/400x400/9b59b6/ffffff?text=CP", ['Gestão de Projetos', 'Scrum', 'Kanban']);
        const candidato4 = new Candidato('uuid-candidato-4', "Daniel Costa", "123.456.789-09", "d.c@email.com", "https://placehold.co/400x400/1abc9c/ffffff?text=DC", ['Segurança da Informação', 'Redes', 'Criptografia']);
        // add aos arrays
        this.empresas.push(empresa1, empresa2);
        this.vagas.push(vaga1, vaga2, vaga3, vaga4);
        this.candidatos.push(candidato1, candidato2, candidato3, candidato4);
    }
}
// exporta uma instancia unica do banco de dados falso
export const bancoDeDadosFake = new BancoDeDadosFake();
//# sourceMappingURL=BancoDeDadosFake.js.map