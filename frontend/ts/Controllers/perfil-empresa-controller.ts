import {bancoDeDadosFake} from "../utils/BancoDeDadosFake.js";

// declarando Chart do CDN
declare const Chart: any;

// elementos DOM
const tabelaCandidatosBody = document.getElementById('tabelaCandidatos') as HTMLTableSectionElement;
const canvasGrafico = document.getElementById('graficoCompetencias') as HTMLCanvasElement;
const statTotalVagasEl = document.getElementById('statTotalVagas') as HTMLElement | null;
const statTotalCandidatosEl = document.getElementById('statTotalCandidatos') as HTMLElement | null;

// funcoes auxiliares
function atualizarEstatisticas() {
    const totalVagas = bancoDeDadosFake.getVagas().length;
    const totalCandidatos = bancoDeDadosFake.getCandidatos().length;
    if (statTotalVagasEl) statTotalVagasEl.textContent = String(totalVagas);
    if (statTotalCandidatosEl) statTotalCandidatosEl.textContent = String(totalCandidatos);
}

// renderiza a tabela de candidatos (anonimos)
function renderizarTabelaCandidatos() {
    // limpa a tabela antes p/ nao duplicar
    tabelaCandidatosBody.innerHTML = '';

    const candidatos = bancoDeDadosFake.getCandidatos();

    if (candidatos.length === 0) {
        const linhaVazia = `
        <tr>
            <td colspan="2" class="p-3 text-center text-gray-500">Nenhum candidato cadastrado.</td>
        </tr>
        `;
        tabelaCandidatosBody.innerHTML = linhaVazia;
        return;
    }

    candidatos.forEach(candidato => {
        const linha = document.createElement('tr');
        linha.className = 'align-top hover:bg-gray-50 transition-colors';

        // lista de competencias
        const competenciasHtml = `
            <ul class="list-disc pl-5 space-y-1">
                ${candidato.competencias.map(c => `<li>${c}</li>`).join('')}
            </ul>
        `;

        // como nao temos formacao no modelo atual, exibimos placeholder
        linha.innerHTML = `
            <td class="p-3 border-b border-gray-100 text-gray-700">Não informado</td>
            <td class="p-3 border-b border-gray-100 text-gray-700">${competenciasHtml}</td>
        `;

        tabelaCandidatosBody.appendChild(linha);
    });
}

// processa dos candidatos para o gráfico
function renderizarGraficoCompetencias() {
    const candidatos = bancoDeDadosFake.getCandidatos();

    const contagem: { [key: string]: number } = {};
    candidatos.forEach(candidato => {
        candidato.competencias.forEach(competencia => {
            contagem[competencia] = (contagem[competencia] || 0) + 1;
        });
    });

    const labels = Object.keys(contagem);
    const data = Object.values(contagem);

    const contexto = canvasGrafico.getContext('2d');
    if (contexto) {
        new Chart(contexto, {
            type: 'bar',
            data: {
                labels,
                datasets: [{
                    label: 'Frequência de Competências',
                    data,
                    backgroundColor: 'rgba(59, 130, 246, 0.7)',
                    borderColor: 'rgba(59, 130, 246, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                scales: {
                    y: { beginAtZero: true, ticks: { stepSize: 1 } }
                },
                responsive: true,
                maintainAspectRatio: false
            }
        });
    }
}

// inicializacao ao carregar a pagina
document.addEventListener('DOMContentLoaded', () => {
    atualizarEstatisticas();
    renderizarTabelaCandidatos();
    renderizarGraficoCompetencias();
});
