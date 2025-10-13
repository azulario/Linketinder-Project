export const PadroesValidacao = {
    // - deve começar e terminar com letra, pode conter espaços entre nomes
    // - aceita letras maiusculas, minusculas e acentuadas
    // - não aceita números, caracteres especiais ou múltiplos espaços consecutivos
    nome: /^[A-Za-zÀ-ÖØ-öø-ÿ]+( [A-Za-zÀ-ÖØ-öø-ÿ]+)*$/,
    // Validação de CPF e CNPJ no formato padrão (apenas formato)
    cpf: /^\d{3}\.\d{3}\.\d{3}-\d{2}$/,
    cnpj: /^\d{2}\.\d{3}\.\d{3}\/\d{4}-\d{2}$/,
    // Validação de email
    email: /^[a-z][a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$/,
    // URL http/https (sem espaços)
    url: /^https?:\/\/[^\s]+$/i,
    // Item de competência: 2–30 chars, letras/números/+.#- e espaço
    competenciaItem: /^[A-Za-zÀ-ÖØ-öø-ÿ0-9.+#\- ]{2,30}$/,
};
export function nomeEhValido(nome) {
    return PadroesValidacao.nome.test(nome.trim());
}
export function cpfEhValido(cpf) {
    return PadroesValidacao.cpf.test(cpf.trim());
}
export function cnpjEhValido(cnpj) {
    return PadroesValidacao.cnpj.test(cnpj.trim());
}
export function emailEhValido(email) {
    return PadroesValidacao.email.test(email.trim());
}
export function urlEhValida(url) {
    const valorTrim = url.trim();
    if (!valorTrim)
        return true; // campo opcional
    return PadroesValidacao.url.test(valorTrim);
}
// processa a string de competencias, retornando um array limpo
export function parseCompetencias(input) {
    return input
        .split(',')
        .map(str => str.trim())
        .filter(Boolean);
}
export function validarCompetencias(input) {
    const competencias = parseCompetencias(input);
    if (competencias.length === 0) {
        return { valid: false, invalid: ['(lista vazia)'], normalized: [] };
    }
    const invalid = competencias.filter(c => !PadroesValidacao.competenciaItem.test(c));
    // remove duplicatas (case-insensitive) e exclui inválidas
    const seen = new Set();
    const normalized = [];
    for (const c of competencias) {
        if (!PadroesValidacao.competenciaItem.test(c))
            continue;
        const key = c.toLowerCase();
        if (seen.has(key))
            continue;
        seen.add(key);
        normalized.push(c);
    }
    return { valid: invalid.length === 0, invalid, normalized };
}
//# sourceMappingURL=validacao_regex.js.map