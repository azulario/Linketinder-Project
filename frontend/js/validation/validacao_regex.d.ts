export declare const PadroesValidacao: {
    nome: RegExp;
    cpf: RegExp;
    cnpj: RegExp;
    email: RegExp;
    url: RegExp;
    competenciaItem: RegExp;
};
export declare function nomeEhValido(nome: string): boolean;
export declare function cpfEhValido(cpf: string): boolean;
export declare function cnpjEhValido(cnpj: string): boolean;
export declare function emailEhValido(email: string): boolean;
export declare function urlEhValida(url: string): boolean;
export declare function parseCompetencias(input: string): string[];
export declare function validarCompetencias(input: string): {
    valid: boolean;
    invalid: string[];
    normalized: string[];
};
//# sourceMappingURL=validacao_regex.d.ts.map