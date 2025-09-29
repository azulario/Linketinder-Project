import { getPerfilSelecionado, listUsuarios, PerfilTipo } from './utils/storage.js';

type Usuario = {
  id: string;
  nomeOuRazao: string;
  email: string;
  fotoUrl?: string;
  competencias: string[];
};

// Função utilitária para obter elementos do DOM com verificação de existência
function byId<T extends HTMLElement>(id: string): T {
  const element = document.getElementById(id);
  if (!element) throw new Error(`Elemento #${id} não encontrado`);
  return element as T;
}

function getListaParaExibir(tipoPerfil: PerfilTipo): Usuario[] {
  // Empresa enxerga candidatos; Candidato enxerga empresas
  return tipoPerfil === 'empresa' ? listUsuarios('candidato') : listUsuarios('empresa');
}

// Define a imagem do card, com fallback em caso de erro ou ausência de URL
function setImagemDoCard(imagemEl: HTMLImageElement, url?: string) {
  const fallback = '../assets/undraw_no-signal_nqfa.png';
  imagemEl.src = url && url.length > 0 ? url : fallback;
  imagemEl.onerror = () => {
    imagemEl.src = fallback;
  };
}

// Renderiza o estado quando não há perfis para exibir
function renderEstadoVazio() {
  const tituloCardEl = byId<HTMLHeadingElement>('card-nome');
  const competenciasEl = byId<HTMLParagraphElement>('card-comps');
  const imagemCardEl = byId<HTMLImageElement>('card-img');
  tituloCardEl.textContent = 'Sem perfis para exibir';
  competenciasEl.textContent = 'Cadastre um perfil para começar.';
  setImagemDoCard(imagemCardEl, undefined);
  // desabilita botões
  byId<HTMLButtonElement>('btn-like').disabled = true;
  byId<HTMLButtonElement>('btn-dislike').disabled = true;
}
// Inicializa a lógica da página de seleção
function init() {
  const tipoPerfilSelecionado: PerfilTipo = getPerfilSelecionado() ?? 'candidato';
  let perfisParaExibir: Usuario[] = getListaParaExibir(tipoPerfilSelecionado);
  let indiceAtual = 0;

  const tituloCardEl = byId<HTMLHeadingElement>('card-nome');
  const competenciasEl = byId<HTMLParagraphElement>('card-comps');
  const imagemCardEl = byId<HTMLImageElement>('card-img');
  const botaoCurtirEl = byId<HTMLButtonElement>('btn-like');
  const botaoRecusarEl = byId<HTMLButtonElement>('btn-dislike');

    // Renderiza o perfil atual no card
  function render() {
    const quantidadePerfis = perfisParaExibir.length;
    if (quantidadePerfis === 0) {
      renderEstadoVazio();
      return;
    }
    const indiceParaRenderizar = ((indiceAtual % quantidadePerfis) + quantidadePerfis) % quantidadePerfis;
    const perfilVisualizado = perfisParaExibir[indiceParaRenderizar];
    if (!perfilVisualizado) {
      renderEstadoVazio();
      return;
    }

    tituloCardEl.textContent = 'Perfil Anônimo';
    const listaCompetencias = perfilVisualizado.competencias?.length
      ? perfilVisualizado.competencias.join(', ')
      : '-';
    competenciasEl.textContent = `Competências: ${listaCompetencias}`;
    setImagemDoCard(imagemCardEl, perfilVisualizado.fotoUrl);
  }

  botaoRecusarEl.addEventListener('click', () => {
    if (perfisParaExibir.length === 0) return;
    indiceAtual = (indiceAtual + 1) % perfisParaExibir.length;
    render();
  });

  botaoCurtirEl.addEventListener('click', () => {
    if (perfisParaExibir.length === 0) return;
    // Mock do fluxo: seguir para a página de match
    window.location.href = 'match.html';
  });

  render();
}

window.addEventListener('DOMContentLoaded', init);
