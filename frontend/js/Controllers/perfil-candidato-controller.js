import { bancoDeDadosFake } from "../utils/BancoDeDadosFake.js";
const listaVagasDiv = document.getElementById('listaVagas');
function renderizarVagas() {
    if (!listaVagasDiv)
        return;
    listaVagasDiv.innerHTML = '';
    const vagas = bancoDeDadosFake.getVagas();
    if (vagas.length === 0) {
        listaVagasDiv.innerHTML = '<p class="text-gray-500 text-sm">Nenhuma vaga disponível no momento.</p>';
        return;
    }
    vagas.forEach(vaga => {
        const card = document.createElement('div');
        card.className = 'bg-white rounded-lg shadow-md p-6 hover:shadow-xl transition';
        const competenciasHtml = vaga.competencias
            .map(comp => `<span class="bg-purple-100 text-purple-800 px-2 py-1 rounded text-xs">${comp}</span>`)
            .join('');
        card.innerHTML = `
      <h3 class="text-lg font-bold text-gray-800 mb-2">${vaga.titulo}</h3>
      <p class="text-sm text-gray-500 mb-3">Empresa: Anônimo</p>
      <div class="flex flex-wrap gap-2 mb-4">${competenciasHtml}</div>
      <button class="w-full bg-pink-500 hover:bg-pink-600 text-white py-2 rounded-lg font-medium transition" disabled title="Função de curtir será implementada futuramente">
        Curtir
      </button>
    `;
        listaVagasDiv.appendChild(card);
    });
}
// inicialização
document.addEventListener('DOMContentLoaded', renderizarVagas);
//# sourceMappingURL=perfil-candidato-controller.js.map