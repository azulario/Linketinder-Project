package br.com.linketinder.ui

class MenuController {
    private final MenuContext context
    private final Map<String, MenuAction> actions

    MenuController(MenuContext context) {
        this.context = context
        this.actions = [
                "1": new ListarCandidatosAction(context),
                "2": new ListarEmpresasAction(context),
                "3": new CandidatoCurteVagaAction(context),
                "4": new EmpresaCurteCandidatoAction(context),
                "5": new ListarMatchesAction(context),
                "0": new ExitAction()
        ]
    }

    void run() {
        while (true) {
            println "\nMenu Principal:"
            actions.each { key, action -> println "$key - ${action.label()}" }
            println "0 - Sair"
            print "> "
            String opcao = System.console() ? System.console().readLine() : context.in.nextLine()

            if (opcao == "0") {
                println "Saindo..."; return
            }

            MenuAction acao = actions.get[opcao]
            if (acao) acao.execute() else println "Opção inválida"

        }
    }
}