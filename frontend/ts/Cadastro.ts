import { getPerfilSelecionado, setPerfilSelecionado, saveUsuario, PerfilTipo } from './utils/storage.js';

function byId<T extends HTMLElement>(id: string): T {
  const element = document.getElementById(id);
  if (!element) throw new Error(`Elemento #${id} não encontrado`);
  return element as T;
}

function normalizeCompetencias(inputTexto: string): string[] {
  return inputTexto
    .split(',')
    .map(fragmento => fragmento.trim())
    .filter(Boolean);
}

function render(tipoPerfil: PerfilTipo) {
  const tituloCadastroEl = byId<HTMLHeadingElement>('titulo-cadastro');
  const labelNomeEl = byId<HTMLLabelElement>('label-nome');
  const toggleTextEl = byId<HTMLSpanElement>('toggle-text');

  if (tipoPerfil === 'candidato') {
    tituloCadastroEl.textContent = 'Cadastro de Candidato';
    labelNomeEl.textContent = 'Nome';
    toggleTextEl.textContent = 'Sou Empresa';
  } else {
    tituloCadastroEl.textContent = 'Cadastro de Empresa';
    labelNomeEl.textContent = 'Razão Social';
    toggleTextEl.textContent = 'Sou Candidato';
  }
}

function init() {
  let tipoPerfil: PerfilTipo = getPerfilSelecionado() ?? 'candidato';
  setPerfilSelecionado(tipoPerfil);
  render(tipoPerfil);

  const botaoTogglePerfilEl = byId<HTMLButtonElement>('toggle-perfil');
  botaoTogglePerfilEl.addEventListener('click', () => {
    tipoPerfil = tipoPerfil === 'candidato' ? 'empresa' : 'candidato';
    setPerfilSelecionado(tipoPerfil);
    render(tipoPerfil);
  });

  const formCadastroEl = byId<HTMLFormElement>('form-cadastro');
  formCadastroEl.addEventListener('submit', (event) => {
    event.preventDefault();
    const inputNomeEl = byId<HTMLInputElement>('nome');
    const inputEmailEl = byId<HTMLInputElement>('email');
    const inputFotoUrlEl = byId<HTMLInputElement>('fotoUrl');
    const inputCompetenciasEl = byId<HTMLInputElement>('competencias');

    const nomeOuRazao = (inputNomeEl.value || '').trim();
    const email = (inputEmailEl.value || '').trim();
    const fotoUrlValor = (inputFotoUrlEl.value || '').trim();
    const competenciasTexto = (inputCompetenciasEl.value || '').trim();

    if (!nomeOuRazao || !email) {
      alert('Preencha ao menos Nome/Razão Social e E-mail.');
      return;
    }

    const payload: { nomeOuRazao: string; email: string; competencias: string[]; fotoUrl?: string } = {
      nomeOuRazao,
      email,
      competencias: normalizeCompetencias(competenciasTexto),
    };
    if (fotoUrlValor) payload.fotoUrl = fotoUrlValor;

    saveUsuario(tipoPerfil, payload);

    alert(`${tipoPerfil === 'candidato' ? 'Candidato' : 'Empresa'} cadastrado(a)!`);
    // Redireciona para a tela de seleção
    window.location.href = 'selecao.html';
  });
}

window.addEventListener('DOMContentLoaded', init);
