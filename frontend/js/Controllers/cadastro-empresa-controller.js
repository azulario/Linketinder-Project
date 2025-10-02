import { bancoDeDadosFake } from "../utils/BancoDeDadosFake.js";
import { Empresa } from "../models/Empresa.js";
import { nomeEhValido, emailEhValido, urlEhValida } from "../validation/validacao_regex.js";
// seleção dos elementos DOM
const form = document.getElementById('formEmpresa');
const nomeOuRazaoInput = document.getElementById('nomeEmpresa');
const emailInput = document.getElementById('emailEmpresa');
const fotoUrlInput = document.getElementById('fotoUrlEmpresa');
const listaEmpresasDiv = document.getElementById('listaEmpresas');
const mensagemDiv = document.getElementById('mensagemEmpresa');
// --- funções ---
//renderiza a lista de empresas na tela
function renderizarEmpresas() {
    // limpa a lista antes p/ nao duplicar
    listaEmpresasDiv.innerHTML = '';
    const empresas = bancoDeDadosFake.getEmpresas();
    if (empresas.length === 0) {
        listaEmpresasDiv.innerHTML = '<p>Nenhuma empresa cadastrada.</p>';
        return;
    }
    empresas.forEach(empresa => {
        const empresaCard = document.createElement('div');
        empresaCard.className = 'flex items-center justify-between bg-gray-50 p-3 rounded-lg shadow-sm';
        empresaCard.innerHTML = `
            <div class="flex items-center gap-3">
                <img 
                  src="${empresa.fotoUrl || `https://placehold.co/40x40/2ecc71/ffffff?text=${empresa.nomeOuRazao[0]}`}" 
                  alt="Logo de ${empresa.nomeOuRazao}" 
                  class="w-10 h-10 rounded-full object-cover border-2 border-gray-200"
                >
                <p class="font-medium text-gray-800">${empresa.nomeOuRazao}</p>
            </div>
              <button data-id="${empresa.id}" class="btn-deletar text-red-500 hover:text-red-700 transition-colors" title="Deletar empresa">
                <span class="material-icons text-lg">delete</span>
              </button>
            `;
        listaEmpresasDiv.appendChild(empresaCard);
    });
}
// exibe msg de feedback p/ usuario
function exibirMensagem(texto, tipo) {
    mensagemDiv.textContent = texto;
    mensagemDiv.className = `mt-3 rounded-lg border px-3 py-2 text-sm ${tipo === 'sucesso'
        ? 'bg-green-50 border-green-300 text-green-700'
        : 'bg-red-50 border-red-300 text-red-700'}`;
    // remove a classe hidden p/ mostrar a msg
    mensagemDiv.classList.remove('hidden');
    // remove a msg depois de 3 segundos
    setTimeout(() => {
        mensagemDiv.classList.add('hidden');
    }, 3000);
}
// lida com o cadastro de uma nova empresa
function cadastrarEmpresa(event) {
    event.preventDefault(); // previne o reload da pagina
    const nomeOuRazao = nomeOuRazaoInput.value.trim(); // remove espacos em branco
    const email = emailInput.value.trim();
    const fotoUrl = fotoUrlInput.value.trim();
    // // validação basica
    // if (!nomeOuRazao.trim() || !nomeOuRazao.trim() || !email.trim()) {
    //     exibirMensagem('Por favor, preencha o nome e o email.', 'erro');
    //     return;
    // }
    // NOVA VALIDAÇÃO COM REGEX
    if (!nomeEhValido(nomeOuRazao)) {
        exibirMensagem('Nome ou razão social inválido. Deve conter apenas letras e espaços.', 'erro');
        nomeOuRazaoInput.focus();
        return;
    }
    if (!emailEhValido(email)) {
        exibirMensagem('Email inválido. Por favor, insira um email válido.', 'erro');
        nomeOuRazaoInput.focus();
        return;
    }
    if (!urlEhValida(fotoUrl)) {
        exibirMensagem('RL da logo inválida. Use http(s)://...', 'erro');
        fotoUrlInput.focus();
        return;
    }
    // cria nova empresa
    const novaEmpresa = new Empresa(crypto.randomUUID(), // gera um id unico
    nomeOuRazao, email, fotoUrl || `https://placehold.co/40x40/2ecc71/ffffff?text=${nomeOuRazao[0]}` // placeholder se n tiver foto
    );
    bancoDeDadosFake.addEmpresa(novaEmpresa);
    form.reset(); // limpa o formulario
    nomeOuRazaoInput.focus();
    exibirMensagem('Empresa cadastrada com sucesso!', 'sucesso');
    renderizarEmpresas(); // atualiza a lista
}
// lida com a exclusão de uma empresa
function deletarEmpresa(event) {
    // event bubbling para capturar clicks no container da lista
    const target = event.target;
    const botao = target.closest('.btn-deletar'); // entra botao de deletar mais proximo
    if (botao) {
        const empresaId = botao.getAttribute('data-id');
        if (empresaId) {
            bancoDeDadosFake.deleteEmpresa(empresaId);
            renderizarEmpresas();
            exibirMensagem('Empresa removida com sucesso!', 'sucesso');
        }
    }
}
// --- event listeners ---
// listerner p/ submit do formulario
form.addEventListener('submit', cadastrarEmpresa);
// listener p/ botoes de deletar (event bubbling)
listaEmpresasDiv.addEventListener('click', deletarEmpresa);
// renderiza a lista inicial assim que carrega a pagina
document.addEventListener('DOMContentLoaded', renderizarEmpresas);
//# sourceMappingURL=cadastro-empresa-controller.js.map