import { describe, it, expect } from 'vitest';
import { PadroesValidacao, nomeEhValido, emailEhValido, urlEhValida, parseCompetencias, validarCompetencias, } from './validacao_regex';
// Testes dos padrões/regex diretamente
describe('PadroesValidacao', () => {
    it('valida nomes simples e completos', () => {
        expect(PadroesValidacao.nome.test('Ana')).toBe(true);
        expect(PadroesValidacao.nome.test('José Silva')).toBe(true);
        expect(PadroesValidacao.nome.test('Álvaro de Souza')).toBe(true);
    });
    it('rejeita nomes com números ou espaços inválidos', () => {
        expect(PadroesValidacao.nome.test('Ana123')).toBe(false);
        expect(PadroesValidacao.nome.test('  Ana')).toBe(false);
        expect(PadroesValidacao.nome.test('Ana  ')).toBe(false);
        expect(PadroesValidacao.nome.test('Ana   Silva')).toBe(false);
    });
    it('valida e-mails comuns', () => {
        expect(PadroesValidacao.email.test('a.teste@example.com')).toBe(true);
        expect(PadroesValidacao.email.test('john_doe-123@empresa.com.br')).toBe(true);
    });
    it('rejeita e-mails fora do padrão', () => {
        expect(PadroesValidacao.email.test('John@exemplo.com')).toBe(false); // começa com maiúscula
        expect(PadroesValidacao.email.test('a@b')).toBe(false); // sem TLD válido
        expect(PadroesValidacao.email.test('teste@@exemplo.com')).toBe(false);
    });
    it('valida URLs http/https', () => {
        expect(PadroesValidacao.url.test('http://site.com')).toBe(true);
        expect(PadroesValidacao.url.test('https://sub.dominio.com/path?x=1#ancora')).toBe(true);
    });
    it('rejeita URLs com espaços ou sem protocolo', () => {
        expect(PadroesValidacao.url.test('site.com')).toBe(false);
        expect(PadroesValidacao.url.test('https: //site.com')).toBe(false);
    });
    it('valida item de competência', () => {
        expect(PadroesValidacao.competenciaItem.test('Java')).toBe(true);
        expect(PadroesValidacao.competenciaItem.test('C++')).toBe(true);
        expect(PadroesValidacao.competenciaItem.test('Node.js')).toBe(true);
        expect(PadroesValidacao.competenciaItem.test('React Native')).toBe(true);
    });
    it('rejeita item de competência muito curto ou com caractere inválido', () => {
        expect(PadroesValidacao.competenciaItem.test('A')).toBe(false);
        expect(PadroesValidacao.competenciaItem.test('@Angular')).toBe(false);
    });
});
// Testes das funções utilitárias
describe('nomeEhValido', () => {
    it('aceita nomes válidos e recusa inválidos', () => {
        expect(nomeEhValido(' Maria ')).toBe(true); // trim
        expect(nomeEhValido('João-Pedro')).toBe(false); // hífen não permitido pela regex atual
        expect(nomeEhValido('João  Pedro')).toBe(false); // espaços duplos
    });
});
describe('emailEhValido', () => {
    it('aceita emails válidos e recusa inválidos', () => {
        expect(emailEhValido('a.b@empresa.com')).toBe(true);
        expect(emailEhValido('A@empresa.com')).toBe(false); // começa com maiúscula não permitido
        expect(emailEhValido('a@b')).toBe(false);
    });
});
describe('urlEhValida', () => {
    it('aceita vazio (opcional) e URLs http(s) válidas', () => {
        expect(urlEhValida('')).toBe(true);
        expect(urlEhValida('   ')).toBe(true);
        expect(urlEhValida('https://exemplo.com/img.png')).toBe(true);
        expect(urlEhValida('ftp://site.com')).toBe(false);
    });
});
describe('parseCompetencias', () => {
    it('divide por vírgula, faz trim e remove vazios', () => {
        expect(parseCompetencias(' Java, Python , , C++ ')).toEqual(['Java', 'Python', 'C++']);
        expect(parseCompetencias('')).toEqual([]);
    });
});
describe('validarCompetencias', () => {
    it('recusa lista vazia', () => {
        const r = validarCompetencias('   ');
        expect(r.valid).toBe(false);
    });
    it('aceita itens válidos, normaliza duplicados e recusa inválidos', () => {
        const r = validarCompetencias('Java, java, Node.js, @angular');
        expect(r.valid).toBe(false);
        expect(r.invalid).toContain('@angular');
        expect(r.normalized).toEqual(['Java', 'Node.js']);
        // A normalização remove duplicatas case-insensitive, mantém primeira ocorrência: ['Java', 'Node.js']
        const r2 = validarCompetencias('Java, java, Node.js');
        expect(r2.valid).toBe(true);
        expect(r2.normalized).toEqual(['Java', 'Node.js']);
    });
});
//# sourceMappingURL=validacao_regex.test.js.map