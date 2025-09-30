
// - Salva no localStorage
// - Lista na tela
// - Permite deletar

// Tipo de dado salvo
type Candidato = {
  id: string
  nome: string
  email: string
  fotoUrl?: string
  competencias: string[]
}

const STORAGE_KEY = 'candidatos'

// Atalho para pegar elementos do DOM com tipo
// o <T> é o tipo genérico que você quer
//
function el<T extends HTMLElement>(id: string): T {
  const ref = document.getElementById(id)
  if (!ref) throw new Error(`#${id} não encontrado`)
  return ref as T
}

// Gera um "id" simples
function novoId(prefixo: string): string {
  const rand = Math.random().toString(36).slice(2, 8)
  return `${prefixo}-${Date.now()}-${rand}`
}

// Converte "a, b, c" em ["a","b","c"]
function parseCompetencias(texto: string): string[] {
  return texto.split(',').map(s => s.trim()).filter(Boolean)
}

// CRUD local (localStorage)
function listar(): Candidato[] {
  const bruto = localStorage.getItem(STORAGE_KEY)
  try { return bruto ? (JSON.parse(bruto) as Candidato[]) : [] } catch { return [] }
}
// Salva (insere ou atualiza)
function salvar(data: Omit<Candidato, 'id'> & { id?: string }): Candidato {
  const todos = listar()
  const id = data.id ?? novoId('candidato')
  const completo: Candidato = { id, ...data }
  todos.push(completo)
  localStorage.setItem(STORAGE_KEY, JSON.stringify(todos))
  return completo
}

// Remove pelo ID
function remover(id: string): boolean {
  const todos = listar()
  const prox = todos.filter(x => x.id !== id)
  localStorage.setItem(STORAGE_KEY, JSON.stringify(prox))
  return prox.length !== todos.length
}

// UI: mensagens simples
function mostrarMensagem(tipo: 'ok' | 'erro', texto: string) {
  const box = el<HTMLDivElement>('mensagem')
  box.textContent = texto
  box.classList.remove('hidden')
  box.className = 'mt-3 rounded-lg border px-3 py-2 text-sm ' + (
    tipo === 'ok'
      ? 'border-green-300 bg-green-50 text-green-700'
      : 'border-red-300 bg-red-50 text-red-700'
  )
  // esconde depois de alguns segundos
  window.setTimeout(() => box.classList.add('hidden'), 3000)
}

// Renderiza a lista na seção #listaCandidatos
function renderLista() {
  const cont = el<HTMLDivElement>('listaCandidatos')
  cont.innerHTML = ''

  const dados = listar()
  if (!dados.length) {
    const vazio = document.createElement('div')
    vazio.className = 'text-sm text-gray-500'
    vazio.textContent = 'Nenhum candidato cadastrado ainda.'
    cont.appendChild(vazio)
    return
  }
// c = candidato
  for (const c of dados) {
    const card = document.createElement('div')
    card.className = 'flex items-start justify-between gap-4 border rounded-lg p-3 bg-white'

    const info = document.createElement('div')
    info.className = 'text-sm'

    const nome = document.createElement('div')
    nome.className = 'font-semibold text-gray-800'
    nome.textContent = c.nome

    const email = document.createElement('div')
    email.className = 'text-gray-500'
    email.textContent = c.email

    const skills = document.createElement('div')
    skills.className = 'text-gray-600'
    skills.textContent = c.competencias.length
      ? `Skills: ${c.competencias.join(', ')}`
      : 'Skills: -'

    info.appendChild(nome)
    info.appendChild(email)
    info.appendChild(skills)

    const acoes = document.createElement('div')
    acoes.className = 'flex items-center gap-2'
// Botão deletar
    const btnDel = document.createElement('button')
    btnDel.type = 'button'
    btnDel.className = 'px-3 py-1 text-xs rounded bg-red-600 text-white hover:bg-red-700'
    btnDel.textContent = 'Deletar'
    btnDel.addEventListener('click', () => {
      if (confirm('Deseja remover este candidato?')) {
        const ok = remover(c.id)
        if (ok) { renderLista(); mostrarMensagem('ok', 'Candidato deletado.') }
        else { mostrarMensagem('erro', 'Não foi possível deletar.') }
      }
    })

    acoes.appendChild(btnDel)

    card.appendChild(info)
    card.appendChild(acoes)
    cont.appendChild(card)
  }
}

// Inicializa eventos do formulário
function init() {
  const form = el<HTMLFormElement>('formCandidato')
  form.addEventListener('submit', (ev) => {
    ev.preventDefault()

    const nome = (el<HTMLInputElement>('nome').value || '').trim()
    const email = (el<HTMLInputElement>('email').value || '').trim()
    const fotoUrl = (el<HTMLInputElement>('fotoUrl').value || '').trim()
    const competenciasTxt = (el<HTMLInputElement>('competencias').value || '').trim()

    if (!nome || !email) {
      mostrarMensagem('erro', 'Preencha os campos obrigatórios (Nome e Email).')
      return
    }

    salvar({
        nome,
        email,
        fotoUrl: fotoUrl || '',
        competencias: parseCompetencias(competenciasTxt)
    })

    // Limpa campos
    ;['nome','email','fotoUrl','competencias']
      .forEach(id => (el<HTMLInputElement>(id).value = ''))

    mostrarMensagem('ok', 'Candidato cadastrado com sucesso!')
    renderLista()
  })

  renderLista()
}

window.addEventListener('DOMContentLoaded', init)

