import { Candidato } from "./Candidato.js";
import { Empresa } from "./Empresa.js";

// Função para salvar o tipo de perfil escolhido
export function selecionarPerfil(tipo: 'candidato' | 'empresa') {
    localStorage.setItem('perfilSelecionado', tipo);
}

window.addEventListener('DOMContentLoaded', () => {
    const btnCandidato = document.getElementById('btn-candidato');
    const btnEmpresa = document.getElementById('btn-empresa');
    if (btnCandidato) {
        btnCandidato.addEventListener('click', () => {
            selecionarPerfil('candidato');
            window.location.href = 'selecao.html';
        });
    }
    if (btnEmpresa) {
        btnEmpresa.addEventListener('click', () => {
            selecionarPerfil('empresa');
            window.location.href = 'selecao.html';
        });
    }
});

// Função para instanciar o objeto correto na tela de seleção
export function getPerfilAtivo(): Candidato | Empresa | null {
    const tipo = localStorage.getItem('perfilSelecionado');
    if (tipo === 'candidato') {
        // Exemplo: dados fictícios, pode ser substituído por dados reais
        return new Candidato('1', 'João', 'joao@email.com', 'foto.png', ['Java', 'SQL']);
    } else if (tipo === 'empresa') {
        return new Empresa('2', 'TechCorp', 'contato@techcorp.com', 'logo.png', ['Java', 'Gestão']);
    }
    return null;
}
