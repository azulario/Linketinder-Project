import { bancoDeDadosFake } from "../utils/BancoDeDadosFake.js";
import { Candidato } from "../models/Candidato.js";
import { nomeEhValido, emailEhValido, urlEhValida, validarCompetencias } from "../validation/validacao_regex.js";
// seleção dos elementos DOM
const form = document.getElementById('formCandidato');
const nomeInput = document.getElementById('nome');
const emailInput = document.getElementById('email');
const fotoUrlInput = document.getElementById('fotoUrl');
const competenciasInput = document.getElementById('competencias');
const listaCandidatosDiv = document.getElementById('listaCandidatos');
const mensagemDiv = document.getElementById('mensagem');
// --- funções ---
// renderiza a lista de candidatos na tela
function renderizarCandidatos() {
    // limpa a lista antes p/ nao duplicar
    listaCandidatosDiv.innerHTML = '';
    const candidatos = bancoDeDadosFake.getCandidatos();
    if (candidatos.length === 0) {
        listaCandidatosDiv.innerHTML = '<p class="text-gray-500 text-sm">Nenhum candidato cadastrado ainda.</p>';
        return;
    }
    candidatos.forEach(candidato => {
        const candidatoCard = document.createElement('div');
        candidatoCard.className = 'flex items-center justify-between bg-gray-50 p-3 rounded-lg shadow-sm';
        candidatoCard.innerHTML = `
        <div class="flex items-center gap-3">
            <img 
              src="${candidato.fotoUrl || `https://placehold.co/40x40/3498db/ffffff?text=${candidato.nomeOuRazao[0]}`}" 
              alt="Foto de ${candidato.nomeOuRazao}" 
              class="w-10 h-10 rounded-full object-cover border-2 border-gray-200"
            >
            <p class="font-medium text-gray-800">${candidato.nomeOuRazao}</p>
        </div>
        <button data-id="${candidato.id}" class="btn-deletar text-red-500 hover:text-red-700 transition-colors" title="Deletar candidato">
            <span class="material-icons text-lg">delete</span>
        </button>
        `;
        listaCandidatosDiv.appendChild(candidatoCard);
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
// lida com cadastro de um novo candidato
//submit event é um evento nativo do JS que ocorre quando um formulário é enviado
// (por exemplo, quando o usuário clica em um botão de envio ou pressiona a tecla Enter dentro de um campo de entrada)
function cadastrarCandidato(event) {
    event.preventDefault(); // previne o comportamento padrão do form (recarregar a página)
    const nome = nomeInput.value.trim(); // remove espacos em branco
    const email = emailInput.value.trim();
    const fotoUrl = fotoUrlInput.value.trim();
    const competenciasTexto = competenciasInput.value.trim();
    // validação simples
    // if (!nome.trim() || !email.trim()) {
    //     exibirMensagem('Por favor, preencha o nome e email.', 'erro');
    //     return;
    // }
    // VALIDAÇÕES MELHORADAS COM REGEX
    if (!nomeEhValido(nome)) {
        exibirMensagem('Nome inválido. Use apenas letras/acentos e espaços (mínimo 2 caracteres).', 'erro');
        return;
    }
    if (!emailEhValido(email)) {
        exibirMensagem('Email inválido. Verifique o formato e tente novamente.', 'erro');
        return;
    }
    if (!urlEhValida(fotoUrl)) {
        exibirMensagem('URL da foto inválida. Deve começar com http:// ou https://', 'erro');
        return;
    }
    const competenciasValidacao = validarCompetencias(competenciasTexto);
    if (!competenciasValidacao.valid) {
        exibirMensagem('Competências inválidas. Separe por vírgula e use 2–30 caracteres por item.', 'erro');
        return;
    }
    // transforma a string de competências em um array, removendo espaços extras
    const competencias = competenciasTexto.split(',').map(c => c.trim()).filter(c => c); // c é cada competência no array
    const novoCandidato = new Candidato(crypto.randomUUID(), nome, email, fotoUrl, competencias);
    bancoDeDadosFake.addCandidato(novoCandidato);
    form.reset(); // limpa o formulario
    nomeInput.focus();
    exibirMensagem('Candidato cadastrado com sucesso!', 'sucesso');
    renderizarCandidatos(); // atualiza a lista
}
// lida com a exclusão de um candidato
function deletarCandidato(event) {
    // event bubbling para capturar clicks no container da lista
    const target = event.target;
    const botao = target.closest('.btn-deletar'); // entra botao de deletar mais proximo
    if (botao) {
        const candidatoId = botao.getAttribute('data-id');
        if (candidatoId) {
            bancoDeDadosFake.deleteCandidato(candidatoId);
            renderizarCandidatos();
            exibirMensagem('Candidato removido com sucesso!', 'sucesso');
        }
    }
}
// --- event listeners ---
// listerner p/ submit do formulario
form.addEventListener('submit', cadastrarCandidato);
// listener p/ clicks na lista de candidatos (delegação de eventos)
listaCandidatosDiv.addEventListener('click', deletarCandidato);
document.addEventListener('DOMContentLoaded', renderizarCandidatos); // renderiza a lista inicial assim que carrega a pagina
//# sourceMappingURL=cadastro-candidato-controller.js.map