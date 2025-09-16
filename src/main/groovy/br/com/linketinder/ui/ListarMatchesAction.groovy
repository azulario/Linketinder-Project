package br.com.linketinder.ui

class ListarMatchesAction {
    private final MenuContext context

    ListarMatchesAction(MenuContext context) {
        this.context = context
    }

    void execute() {
        boolean encontrouMatch = false
        context.candidatos.each { candidato ->
            context.empresas.each { empresa ->
                if (context.sistema.deuMatch(candidato, empresa)) {
                    println "${candidato.nome} e ${empresa.razaoSocial} são um match!"
                    encontrouMatch = true
                }
            }
        }
        if (!encontrouMatch) {
            println "Nenhum match encontrado."
        }
    }
}
