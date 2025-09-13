// Autor: Azulario
// Para executar: gradle run ou groovy Main.groovy
package br.com.linketinder

import br.com.linketinder.dominio.Candidato
import br.com.linketinder.dominio.Empresa
import br.com.linketinder.servico.SistemaMatch

class Main {
    /*
     TODO: Implementar curtida às cegas:
         - Candidato só pode ver as competências/vagas que a empresa busca, não detalhes da empresa.
         - Empresa só pode ver as competências que o candidato oferece, não detalhes do candidato.
         - Garantir privacidade dos perfis até o match.
     TODO: Refatorar Main:
         - Deixar o Main apenas como orquestrador, chamando métodos das classes de domínio.
         - Mover lógicas de negócio para as classes apropriadas (Candidato, Empresa, Curtida, etc).
         - Reduzir o tamanho do Main, removendo código duplicado e responsabilidades excessivas.
         - Criar métodos utilitários para entrada/saída, se necessário.
         - Facilitar testes unitários e manutenção.
     TODO: Validar fluxos de match e curtidas recíprocas.
     TODO: Garantir tratamento de erros e entradas inválidas.
     TODO: Melhorar experiência do usuário no menu e fluxos.
    */


    static void main(String[] args) {
        SistemaMatch sistema = new SistemaMatch()

        // Listas no formato enxuto (id, nome, email, telefone, competencias)
        List<Candidato> candidatos = List.of(
            new Candidato("cand-1", "Ana Silva", "ana@email.com", "555-0001", List.of("Java", "Spring Framework")),
            new Candidato("cand-2", "Bruno Souza", "bruno@email.com", "555-0002", List.of("Angular", "JavaScript")),
            new Candidato("cand-3", "Carla Lima", "carla@email.com", "555-0003", List.of("Python", "Django")),
            new Candidato("cand-4", "Daniel Costa", "daniel@email.com", "555-0004", List.of("AWS", "Docker")),
            new Candidato("cand-5", "Eduarda Alves", "eduarda@email.com", "555-0005", List.of("Kotlin", "Flutter"))
        )

        List<Empresa> empresas = List.of(
            new Empresa("emp-1", "Arroz-Gostoso", "contato@arrozgostoso.com", "555-1001", List.of("Java", "Spring Framework")),
            new Empresa("emp-2", "Império do Boliche", "contato@boliche.com", "555-1002", List.of("Angular", "JavaScript")),
            new Empresa("emp-3", "Tech Solutions", "contato@techsolutions.com", "555-1003", List.of("Python", "Django")),
            new Empresa("emp-4", "Cloud Experts", "contato@cloudexperts.com", "555-1004", List.of("AWS", "Docker")),
            new Empresa("emp-5", "Mobile Makers", "contato@mobilemakers.com", "555-1005", List.of("Kotlin", "Flutter"))
        )

        // Menu interativo
        Scanner scanner = new Scanner(System.in)
        while (true) {
            println "\nMenu Principal:" +
                    "\n1 - Listar Candidatos(perfil público)" +
                    "\n2 - Listar Empresas(perfil público)" +
                    "\n3 - Candidato curtir vaga de empresa" +
                    "\n4 - Empresa curtir candidato" +
                    "\n5 - Listar matches e revelar dados \n0 - Sair"
            String opcao = System.console() ? System.console().readLine() : scanner.nextLine()

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
}
