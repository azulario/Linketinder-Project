import { getPerfilSelecionado, setPerfilSelecionado, saveUsuario } from './utils/storage.js';
import { listUsuarios } from './utils/storage.js';
import { removeUsuario } from './utils/storage.js';
function byId(id) {
    const element = document.getElementById(id);
    if (!element)
        throw new Error(`Elemento #${id} não encontrado`);
    return element;
}
function normalizeCompetencias(inputTexto) {
    return inputTexto
        .split(',')
        .map(fragmento => fragmento.trim())
        .filter(Boolean);
}
function render(tipoPerfil) {
    const tituloCadastroEl = byId('titulo-cadastro');
    const labelNomeEl = byId('label-nome');
    const toggleTextEl = byId('toggle-text');
    if (tipoPerfil === 'candidato') {
        tituloCadastroEl.textContent = 'Cadastro de Candidato';
        labelNomeEl.textContent = 'Nome';
        toggleTextEl.textContent = 'Sou Empresa';
    }
    else {
        tituloCadastroEl.textContent = 'Cadastro de Empresa';
        labelNomeEl.textContent = 'Razão Social';
        toggleTextEl.textContent = 'Sou Candidato';
    }
}
function criarItemAnonimo(usuario, tipo) {
    const li = document.createElement('li');
    li.className = 'flex items-start justify-between gap-3 bg-gray-50 rounded-lg p-3';
    const descricao = document.createElement('div');
    descricao.className = 'text-sm text-gray-700';
    const titulo = document.createElement('div');
    titulo.className = 'font-medium text-gray-800';
    titulo.textContent = tipo === 'candidato' ? 'Candidato Anônimo' : 'Empresa Anônima';
    const comps = document.createElement('div');
    comps.className = 'text-xs text-gray-500';
    const compsTxt = usuario.competencias && usuario.competencias.length
        ? `Skills: ${usuario.competencias.slice(0, 5).join(', ')}`
        : 'Skills: -';
    comps.textContent = compsTxt;
    descricao.appendChild(titulo);
    descricao.appendChild(comps);
    const acoes = document.createElement('div');
    acoes.className = 'flex items-center gap-2';
    const btnExcluir = document.createElement('button');
    btnExcluir.type = 'button';
    btnExcluir.className = 'px-3 py-1 text-xs rounded bg-red-600 text-white hover:bg-red-700';
    btnExcluir.textContent = 'Excluir';
    btnExcluir.addEventListener('click', () => {
        const confirmar = confirm('Deseja excluir este cadastro?');
        if (!confirmar)
            return;
        const ok = removeUsuario(tipo, usuario.id);
        if (ok) {
            renderListasAnonimas();
        }
        else {
            alert('Não foi possível excluir. Tente novamente.');
        }
    });
    acoes.appendChild(btnExcluir);
    li.appendChild(descricao);
    li.appendChild(acoes);
    return li;
}
function renderListasAnonimas() {
    const ulCandidatos = byId('lista-candidatos');
    const ulEmpresas = byId('lista-empresas');
    // Limpa listas
    ulCandidatos.innerHTML = '';
    ulEmpresas.innerHTML = '';
    const candidatos = listUsuarios('candidato');
    const empresas = listUsuarios('empresa');
    if (!candidatos.length) {
        const li = document.createElement('li');
        li.className = 'text-sm text-gray-500';
        li.textContent = 'Nenhum candidato cadastrado.';
        ulCandidatos.appendChild(li);
    }
    else {
        for (const cand of candidatos) {
            ulCandidatos.appendChild(criarItemAnonimo(cand, 'candidato'));
        }
    }
    if (!empresas.length) {
        const li = document.createElement('li');
        li.className = 'text-sm text-gray-500';
        li.textContent = 'Nenhuma empresa cadastrada.';
        ulEmpresas.appendChild(li);
    }
    else {
        for (const emp of empresas) {
            ulEmpresas.appendChild(criarItemAnonimo(emp, 'empresa'));
        }
    }
}
function init() {
    let tipoPerfil = getPerfilSelecionado() ?? 'candidato';
    setPerfilSelecionado(tipoPerfil);
    render(tipoPerfil);
    // Renderiza listas existentes logo ao carregar
    renderListasAnonimas();
    const botaoTogglePerfilEl = byId('toggle-perfil');
    botaoTogglePerfilEl.addEventListener('click', () => {
        tipoPerfil = tipoPerfil === 'candidato' ? 'empresa' : 'candidato';
        setPerfilSelecionado(tipoPerfil);
        render(tipoPerfil);
    });
    const formCadastroEl = byId('form-cadastro');
    formCadastroEl.addEventListener('submit', (event) => {
        event.preventDefault();
        const inputNomeEl = byId('nome');
        const inputEmailEl = byId('email');
        const inputFotoUrlEl = byId('fotoUrl');
        const inputCompetenciasEl = byId('competencias');
        const nomeOuRazao = (inputNomeEl.value || '').trim();
        const email = (inputEmailEl.value || '').trim();
        const fotoUrlValor = (inputFotoUrlEl.value || '').trim();
        const competenciasTexto = (inputCompetenciasEl.value || '').trim();
        if (!nomeOuRazao || !email) {
            alert('Preencha ao menos Nome/Razão Social e E-mail.');
            return;
        }
        const payload = {
            nomeOuRazao,
            email,
            competencias: normalizeCompetencias(competenciasTexto),
        };
        if (fotoUrlValor)
            payload.fotoUrl = fotoUrlValor;
        saveUsuario(tipoPerfil, payload);
        // Atualiza a listagem anônima após salvar
        renderListasAnonimas();
        // Limpa o formulário e mantém na página para continuar gerenciando (CRUD)
        inputNomeEl.value = '';
        inputEmailEl.value = '';
        inputFotoUrlEl.value = '';
        inputCompetenciasEl.value = '';
        alert(`${tipoPerfil === 'candidato' ? 'Candidato' : 'Empresa'} cadastrado(a)!`);
    });
}
window.addEventListener('DOMContentLoaded', init);
//# sourceMappingURL=Cadastro.js.map