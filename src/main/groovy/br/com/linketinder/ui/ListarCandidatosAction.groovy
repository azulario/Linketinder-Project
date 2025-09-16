package br.com.linketinder.ui

class ListarCandidatosAction {
    private final MenuContext context

    ListarCandidatosAction(MenuContext context) {
        this.context = context
    }

    String label() {
        "Listar Candidatos (perfil público)"
    }

    void execute() {
        context.candidatos.eachWithIndex { candidato, index ->
            def perfil = candidato.obterPerfilPublico()
            println "${index}) id = ${candidato.id} competencias = ${perfil.competencias}"
        }
    }
}
