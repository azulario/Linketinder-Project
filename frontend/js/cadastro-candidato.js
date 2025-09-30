// - Salva no localStorage
// - Lista na tela
// - Permite deletar
const STORAGE_KEY = 'candidatos';
// Atalho para pegar elementos do DOM com tipo
function el(id) {
    const ref = document.getElementById(id);
    if (!ref)
        throw new Error(`#${id} não encontrado`);
    return ref;
}
// Gera um "id" simples
function novoId(prefixo) {
    const rand = Math.random().toString(36).slice(2, 8);
    return `${prefixo}-${Date.now()}-${rand}`;
}
// Converte "a, b, c" em ["a","b","c"]
function parseCompetencias(texto) {
    return texto.split(',').map(s => s.trim()).filter(Boolean);
}
// CRUD local (localStorage)
function listar() {
    const bruto = localStorage.getItem(STORAGE_KEY);
    try {
        return bruto ? JSON.parse(bruto) : [];
    }
    catch {
        return [];
    }
}
// Salva (insere ou atualiza)
function salvar(data) {
    const todos = listar();
    const id = data.id ?? novoId('candidato');
    const completo = { id, ...data };
    todos.push(completo);
    localStorage.setItem(STORAGE_KEY, JSON.stringify(todos));
    return completo;
}
// Remove pelo ID
function remover(id) {
    const todos = listar();
    const prox = todos.filter(x => x.id !== id);
    localStorage.setItem(STORAGE_KEY, JSON.stringify(prox));
    return prox.length !== todos.length;
}
// UI: mensagens simples
function mostrarMensagem(tipo, texto) {
    const box = el('mensagem');
    box.textContent = texto;
    box.classList.remove('hidden');
    box.className = 'mt-3 rounded-lg border px-3 py-2 text-sm ' + (tipo === 'ok'
        ? 'border-green-300 bg-green-50 text-green-700'
        : 'border-red-300 bg-red-50 text-red-700');
    // esconde depois de alguns segundos
    window.setTimeout(() => box.classList.add('hidden'), 3000);
}
// Renderiza a lista na seção #listaCandidatos
function renderLista() {
    const cont = el('listaCandidatos');
    cont.innerHTML = '';
    const dados = listar();
    if (!dados.length) {
        const vazio = document.createElement('div');
        vazio.className = 'text-sm text-gray-500';
        vazio.textContent = 'Nenhum candidato cadastrado ainda.';
        cont.appendChild(vazio);
        return;
    }
    for (const c of dados) {
        const card = document.createElement('div');
        card.className = 'flex items-start justify-between gap-4 border rounded-lg p-3 bg-white';
        const info = document.createElement('div');
        info.className = 'text-sm';
        const nome = document.createElement('div');
        nome.className = 'font-semibold text-gray-800';
        nome.textContent = c.nome;
        const email = document.createElement('div');
        email.className = 'text-gray-500';
        email.textContent = c.email;
        const skills = document.createElement('div');
        skills.className = 'text-gray-600';
        skills.textContent = c.competencias.length
            ? `Skills: ${c.competencias.join(', ')}`
            : 'Skills: -';
        info.appendChild(nome);
        info.appendChild(email);
        info.appendChild(skills);
        const acoes = document.createElement('div');
        acoes.className = 'flex items-center gap-2';
        const btnDel = document.createElement('button');
        btnDel.type = 'button';
        btnDel.className = 'px-3 py-1 text-xs rounded bg-red-600 text-white hover:bg-red-700';
        btnDel.textContent = 'Deletar';
        btnDel.addEventListener('click', () => {
            if (confirm('Deseja remover este candidato?')) {
                const ok = remover(c.id);
                if (ok) {
                    renderLista();
                    mostrarMensagem('ok', 'Candidato deletado.');
                }
                else {
                    mostrarMensagem('erro', 'Não foi possível deletar.');
                }
            }
        });
        acoes.appendChild(btnDel);
        card.appendChild(info);
        card.appendChild(acoes);
        cont.appendChild(card);
    }
}
// Inicializa eventos do formulário
function init() {
    const form = el('formCandidato');
    form.addEventListener('submit', (ev) => {
        ev.preventDefault();
        const nome = (el('nome').value || '').trim();
        const email = (el('email').value || '').trim();
        const fotoUrl = (el('fotoUrl').value || '').trim();
        const competenciasTxt = (el('competencias').value || '').trim();
        if (!nome || !email) {
            mostrarMensagem('erro', 'Preencha os campos obrigatórios (Nome e Email).');
            return;
        }
        salvar({
            nome,
            email,
            fotoUrl: fotoUrl || '',
            competencias: parseCompetencias(competenciasTxt)
        });
        ['nome', 'email', 'fotoUrl', 'competencias']
            .forEach(id => (el(id).value = ''));
        mostrarMensagem('ok', 'Candidato cadastrado com sucesso!');
        renderLista();
    });
    renderLista();
}
window.addEventListener('DOMContentLoaded', init);
export {};
//# sourceMappingURL=cadastro-candidato.js.map