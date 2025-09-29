export class Empresa {
    id;
    nomeOuRazao;
    email;
    fotoUrl;
    competencias;
    curtidos = [];
    constructor(id, nomeOuRazao, email, fotoUrl, competencias) {
        this.id = id;
        this.nomeOuRazao = nomeOuRazao;
        this.email = email;
        this.fotoUrl = fotoUrl;
        this.competencias = competencias;
    }
    curtirPerfil(perfilId) {
        if (!this.curtidos.includes(perfilId)) {
            this.curtidos.push(perfilId);
            console.log(`Empresa ${this.nomeOuRazao} curtiu o perfil ${perfilId}`);
        }
        else {
            console.log(`Empresa ${this.nomeOuRazao} já curtiu o perfil ${perfilId}`);
        }
    }
    getCurtidos() {
        return this.curtidos;
    }
}
//# sourceMappingURL=Empresa.js.map