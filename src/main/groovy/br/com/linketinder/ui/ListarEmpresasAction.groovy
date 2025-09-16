package br.com.linketinder.ui

class ListarEmpresasAction {
    private final MenuContext context

    ListarEmpresasAction(MenuContext context) {
        this.context = context
    }

    static String label() {
        "Listar Empresas"
    }

    void execute() {
        println "\n=== Lista de Empresas ==="
        if (context.empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada."
        } else {
            context.empresas.eachWithIndex { empresa, index ->
                println "ID: ${empresa.id} | Competências desejadas: ${empresa.competenciasBuscadas.join(', ')}"
            }
        }
    }
}
