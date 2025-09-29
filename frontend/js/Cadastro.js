import { getPerfilSelecionado, setPerfilSelecionado, saveUsuario } from './utils/storage.js';
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
function init() {
    let tipoPerfil = getPerfilSelecionado() ?? 'candidato';
    setPerfilSelecionado(tipoPerfil);
    render(tipoPerfil);
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
        alert(`${tipoPerfil === 'candidato' ? 'Candidato' : 'Empresa'} cadastrado(a)!`);
        // Redireciona para a tela de seleção
        window.location.href = 'selecao.html';
    });
}
window.addEventListener('DOMContentLoaded', init);
//# sourceMappingURL=Cadastro.js.map