export const PadroesValidacao = {
    // - deve começar e terminar com letra, pode conter espaços entre nomes
    // - aceita letras maiusculas, minusculas e acentuadas
    // - não aceita números, caracteres especiais ou múltiplos espaços consecutivos
    nome: /^[A-Za-zÀ-ÖØ-öø-ÿ]+( [A-Za-zÀ-ÖØ-öø-ÿ]+)*$/,
    // Validação de CPF e CNPJ no formato padrão:
    // CPF: 000.000.000-00
    // CNPJ: 00.000.000/0000-00
    // - deve conter apenas números, pontos, barras e hífens nos locais corretos
    // - não aceita letras ou caracteres especiais fora do padrão
    // - não valida a lógica do número, apenas o formato
    cpf: /^\d{3}\.\d{3}\.\d{3}-\d{2}$/,
    cnpj: /^\d{2}\.\d{3}\.\d{3}\/\d{4}-\d{2}$/,
    // Validação de email:
    // - deve começar com letra minúscula
    // - pode conter letras, números, pontos, underscores, porcentagem, sinais de mais e hífens antes do @
    // - deve ter um domínio válido após o @ (letras, números, pontos e hífens)
    // - deve terminar com uma extensão de pelo menos dois caracteres (ex: .com, .org, .br)
    // - não aceita espaços ou caracteres especiais fora dos permitidos
    email: /^[a-z][a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$/,
    // - deve começar com http:// ou https://
    // - pode conter letras, números e caracteres especiais comuns em URLs
    // - não deve conter espaços
    // - é case insensitive (maiúsculas e minúsculas são tratadas como iguais)
    url: /^https?:\/\/[^\s]+$/i,
    // - deve ter entre 2 e 30 caracteres
    // - pode conter letras (maiúsculas, minúsculas e acentuadas), números, pontos, sinais de mais, hashtags, hífens e espaços
    // - não deve começar ou terminar com espaço
    // - não deve conter caracteres especiais além dos permitidos
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
    // valotrim = remove espacos em branco no inicio e fim
    // se o campo estiver vazio, retorna true (campo opcional)
    const valorTrim = url.trim();
    if (!valorTrim)
        return true; // campo opcional
    return PadroesValidacao.url.test(valorTrim);
}
// processa a string de competencias, retornando um array limpo
// ex: " Java, Python , , C++ " => ["Java", "Python", "C++"]
export function parseCompetencias(input) {
    return input
        .split(',') // separa por virgulas
        .map(strng => strng.trim()) // remove espacos em branco
        .filter(Boolean); // remove strings vazias
}
export function validarCompetencias(input) {
    const competencias = parseCompetencias(input);
    if (competencias.length === 0) {
        return { valid: false, invalid: ['(lista vazia)'], normalized: [] };
    }
    // filtra as competencias invalidas
    // c = cada competência no array
    const invalid = competencias.filter(c => !PadroesValidacao.competenciaItem.test(c));
    // remove duplicatas (case-insensitive) e EXCLUI itens inválidos da normalização
    const seen = new Set();
    const normalized = [];
    for (const c of competencias) {
        if (!PadroesValidacao.competenciaItem.test(c))
            continue; // ignora inválidas na normalização
        const chave = c.toLowerCase();
        if (seen.has(chave))
            continue; // já vimos (case-insensitive)
        seen.add(chave);
        normalized.push(c);
    }
    return { valid: invalid.length === 0, invalid, normalized };
}
//# sourceMappingURL=validacao_regex.js.map