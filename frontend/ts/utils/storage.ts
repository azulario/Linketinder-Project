export type PerfilTipo = 'candidato' | 'empresa';

export interface UsuarioCadastro {
  id: string;
  nomeOuRazao: string;
  email: string;
  fotoUrl?: string;
  competencias: string[];
}

const KEY_MAP: Record<PerfilTipo, string> = {
  candidato: 'candidatos',
  empresa: 'empresas',
};

export function getPerfilSelecionado(): PerfilTipo | null {
  const v = localStorage.getItem('perfilSelecionado');
  if (v === 'candidato' || v === 'empresa') return v;
  return null;
}


export function generateId(prefix: PerfilTipo): string {
  // ID simples, suficiente para localStorage (timestamp + rand)
  const rand = Math.random().toString(36).slice(2, 8);
  return `${prefix}-${Date.now()}-${rand}`;
}

export function listUsuarios(tipo: PerfilTipo): UsuarioCadastro[] {
  const key = KEY_MAP[tipo];
  const raw = localStorage.getItem(key);
  try {
    return raw ? (JSON.parse(raw) as UsuarioCadastro[]) : [];
  } catch {
    return [];
  }
}

export function saveUsuario(tipo: PerfilTipo, usuario: Omit<UsuarioCadastro, 'id'> & { id?: string }): UsuarioCadastro {
  const key = KEY_MAP[tipo];
  const lista = listUsuarios(tipo);
  const id = usuario.id ?? generateId(tipo);
  const completo: UsuarioCadastro = { id, ...usuario } as UsuarioCadastro;
  lista.push(completo);
  localStorage.setItem(key, JSON.stringify(lista));
  return completo;
}

export function removeUsuario(tipo: PerfilTipo, id: string): boolean {
  const key = KEY_MAP[tipo];
  const lista = listUsuarios(tipo);
  const nova = lista.filter(u => u.id !== id);
  localStorage.setItem(key, JSON.stringify(nova));
  return nova.length !== lista.length;
}

