import { Candidato } from "./models/Candidato";
import { Empresa } from "./models/Empresa";

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
            window.location.href = 'cadastro.html';
        });
    }
    if (btnEmpresa) {
        btnEmpresa.addEventListener('click', () => {
            selecionarPerfil('empresa');
            window.location.href = 'cadastro.html';
        });
    }
});

