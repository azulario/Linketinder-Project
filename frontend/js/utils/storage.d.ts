export type PerfilTipo = 'candidato' | 'empresa';
export interface UsuarioCadastro {
    id: string;
    nomeOuRazao: string;
    email: string;
    fotoUrl?: string;
    competencias: string[];
}
export declare function getPerfilSelecionado(): PerfilTipo | null;
export declare function generateId(prefix: PerfilTipo): string;
export declare function listUsuarios(tipo: PerfilTipo): UsuarioCadastro[];
export declare function saveUsuario(tipo: PerfilTipo, usuario: Omit<UsuarioCadastro, 'id'> & {
    id?: string;
}): UsuarioCadastro;
export declare function removeUsuario(tipo: PerfilTipo, id: string): boolean;
//# sourceMappingURL=storage.d.ts.map