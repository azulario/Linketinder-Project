const KEY_MAP = {
    candidato: 'candidatos',
    empresa: 'empresas',
};
export function getPerfilSelecionado() {
    const v = localStorage.getItem('perfilSelecionado');
    if (v === 'candidato' || v === 'empresa')
        return v;
    return null;
}
export function generateId(prefix) {
    // ID simples, suficiente para localStorage (timestamp + rand)
    const rand = Math.random().toString(36).slice(2, 8);
    return `${prefix}-${Date.now()}-${rand}`;
}
export function listUsuarios(tipo) {
    const key = KEY_MAP[tipo];
    const raw = localStorage.getItem(key);
    try {
        return raw ? JSON.parse(raw) : [];
    }
    catch {
        return [];
    }
}
export function saveUsuario(tipo, usuario) {
    const key = KEY_MAP[tipo];
    const lista = listUsuarios(tipo);
    const id = usuario.id ?? generateId(tipo);
    const completo = { id, ...usuario };
    lista.push(completo);
    localStorage.setItem(key, JSON.stringify(lista));
    return completo;
}
export function removeUsuario(tipo, id) {
    const key = KEY_MAP[tipo];
    const lista = listUsuarios(tipo);
    const nova = lista.filter(u => u.id !== id);
    localStorage.setItem(key, JSON.stringify(nova));
    return nova.length !== lista.length;
}
//# sourceMappingURL=storage.js.map