package br.com.linketinder.ui

class ListarCandidatosAction {
    private final MenuContext context

    ListarCandidatosAction(MenuContext context) {
        this.context = context
    }

    @Override
    String label() {
        "Listar Candidatos (perfil público)"
    }

    @Override
    void execute() {
        context.candidatos.eachWithIndex { candidato, index ->
            def perfil = candidato.perfilPublico(candidato)
            println "${index}) id = ${candidato.id} competencias = ${perfil.competencias}"
        }
    }
}
