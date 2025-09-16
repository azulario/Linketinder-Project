// Autor: Azulario
// Para executar: gradle run ou groovy Main.groovy
package br.com.linketinder

import br.com.linketinder.controller.EmpresaController
import br.com.linketinder.controller.EmpresaControllerImpl
import br.com.linketinder.controller.CandidatoController
import br.com.linketinder.controller.CandidatoControllerImpl
import br.com.linketinder.dominio.Candidato
import br.com.linketinder.dominio.Empresa
import br.com.linketinder.servico.SistemaMatch
import br.com.linketinder.ui.ListarCandidatosAction
import br.com.linketinder.ui.ListarEmpresasAction
import br.com.linketinder.ui.ListarMatchesAction
import br.com.linketinder.ui.MenuContext
import br.com.linketinder.ui.EmpresaCurteCandidatoAction
import br.com.linketinder.ui.CandidatoCurteVagaAction

class Main {
    /*
     coberto: Implementar curtida às cegas:
         - [x] Candidato só pode ver as competências/vagas que a empresa busca, não detalhes da empresa.
         - [x] Empresa só pode ver as competências que o candidato oferece, não detalhes do candidato.
         - [x] Garantir privacidade dos perfis até o match.
     coberto: Refatorar Main:
         - [x] Deixar o Main apenas como orquestrador, chamando métodos das classes de domínio.
         - [x] Mover lógicas de negócio para as classes apropriadas (Candidato, Empresa, Curtida, etc).
         - [x] Reduzir o tamanho do Main, removendo código duplicado e responsabilidades excessivas.
         - [x] Criar métodos utilitários para entrada/saída, se necessário.
         - [x] Facilitar testes unitários e manutenção.
     TODO: Validar fluxos de match e curtidas recíprocas.
         - [x] Fluxos de match e curtidas recíprocas validados por testes automatizados.
     TODO: Garantir tratamento de erros e entradas inválidas.
         - [ ] Garantir tratamento de erros e entradas inválidas.
     TODO: Melhorar experiência do usuário no menu e fluxos.
         - [ ] Melhorar experiência do usuário no menu e fluxos.
    */


    static SistemaMatch sistema
    static List<Candidato> candidatos
    static List<Empresa> empresas
    static Scanner scanner
    static MenuContext context
    static EmpresaController empresaController
    static CandidatoController candidatoController

    static void main(String[] args) {
        sistema = new SistemaMatch()
        candidatos = List.of(
            new Candidato("cand-1", "Ana Silva", "ana@email.com", "555-0001", List.of("Java", "Spring Framework")),
            new Candidato("cand-2", "Bruno Souza", "bruno@email.com", "555-0002", List.of("Angular", "JavaScript")),
            new Candidato("cand-3", "Carla Lima", "carla@email.com", "555-0003", List.of("Python", "Django")),
            new Candidato("cand-4", "Daniel Costa", "daniel@email.com", "555-0004", List.of("AWS", "Docker")),
            new Candidato("cand-5", "Eduarda Alves", "eduarda@email.com", "555-0005", List.of("Kotlin", "Flutter"))
        )
        empresas = List.of(
            new Empresa("emp-1", "Arroz-Gostoso", "contato@arrozgostoso.com", "555-1001", List.of("Java", "Spring Framework")),
            new Empresa("emp-2", "Império do Boliche", "contato@boliche.com", "555-1002", List.of("Angular", "JavaScript")),
            new Empresa("emp-3", "Tech Solutions", "contato@techsolutions.com", "555-1003", List.of("Python", "Django")),
            new Empresa("emp-4", "Cloud Experts", "contato@cloudexperts.com", "555-1004", List.of("AWS", "Docker")),
            new Empresa("emp-5", "Mobile Makers", "contato@mobilemakers.com", "555-1005", List.of("Kotlin", "Flutter"))
        )
        scanner = new Scanner(System.in)
        context = new MenuContext(sistema, candidatos, empresas, scanner)
        empresaController = new EmpresaControllerImpl()
        candidatoController = new CandidatoControllerImpl()
        while (true) {
            println "\nMenu Principal:" +
                    "\n1 - Listar Candidatos(perfil público)" +
                    "\n2 - Listar Empresas(perfil público)" +
                    "\n3 - Candidato curtir vaga de empresa" +
                    "\n4 - Empresa curtir candidato" +
                    "\n5 - Listar matches e revelar dados \n0 - Sair"
            String opcao
            if (System.console()) {
                opcao = System.console().readLine()
            } else if (scanner.hasNextLine()) {
                opcao = scanner.nextLine()
            } else {
                println "Nenhuma entrada disponível. Encerrando programa."
                return
            }
            switch(opcao) {
                case "1": listarCandidatos(); break
                case "2": listarEmpresas(); break
                case "3": candidatoCurteVaga(); break
                case "4": empresaCurteCandidato(); break
                case "5": listarMatches(); break
                case "0": println "Saindo..."; return
                default: println "Opção inválida!"
            }
        }
    }

    private static void listarCandidatos() {
        new ListarCandidatosAction(context).execute()
    }
    private static void listarEmpresas() {
        new ListarEmpresasAction(context).execute()
    }
    private static void listarMatches() {
        new ListarMatchesAction(context).execute()
    }
    private static void candidatoCurteVaga() {
        println "Digite o ID do candidato:"
        String idCandidato = scanner.nextLine()
        println "Digite o ID da vaga (empresa):"
        String idVaga = scanner.nextLine()
        def action = new CandidatoCurteVagaAction(candidatoController)
        try {
            boolean sucesso = action.executar(idCandidato, idVaga)
            if (sucesso) {
                println "Candidato curtiu a vaga com sucesso!"
            } else {
                println "Não foi possível curtir a vaga."
            }
        } catch (Exception e) {
            println "Erro: ${e.message}"
        }
    }
    private static void empresaCurteCandidato() {
        println "Digite o ID da empresa:"
        String idEmpresa = scanner.nextLine()
        println "Digite o ID do candidato:"
        String idCandidato = scanner.nextLine()
        def action = new EmpresaCurteCandidatoAction(empresaController)
        try {
            boolean sucesso = action.executar(idEmpresa, idCandidato)
            if (sucesso) {
                println "Empresa curtiu o candidato com sucesso!"
            } else {
                println "Não foi possível curtir o candidato."
            }
        } catch (Exception e) {
            println "Erro: ${e.message}"
        }
    }
}
