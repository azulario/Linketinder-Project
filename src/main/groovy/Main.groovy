// Autor: Azulario
// Para executar: gradle run ou groovy Main.groovy

import groovy.transform.Field

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

    static List<Candidato> candidatos = [
        new Candidato("Ana Silva", "ana@email.com", "SP", "01000-000", "Desenvolvedora experiente", ["Java", "Spring Framework"], "123.456.789-00", 28),
        new Candidato("Bruno Souza", "bruno@email.com", "RJ", "20000-000", "Especialista em Frontend", ["Angular", "JavaScript"], "987.654.321-00", 32),
        new Candidato("Carla Lima", "carla@email.com", "MG", "30000-000", "Fullstack Developer", ["Python", "Django"], "456.789.123-00", 25),
        new Candidato("Daniel Costa", "daniel@email.com", "RS", "90000-000", "DevOps", ["AWS", "Docker"], "321.654.987-00", 35),
        new Candidato("Eduarda Alves", "eduarda@email.com", "BA", "40000-000", "Mobile Developer", ["Kotlin", "Flutter"], "654.321.987-00", 27)
    ]

    static List<Empresa> empresas = [
        new Empresa("Arroz-Gostoso", "contato@arrozgostoso.com", "SP", "01001-000", "Empresa de alimentos", ["Java", "Spring Framework"], "12.345.678/0001-00", "Brasil"),
        new Empresa("Império do Boliche", "contato@boliche.com", "RJ", "20001-000", "Entretenimento", ["Angular", "JavaScript"], "98.765.432/0001-00", "Brasil"),
        new Empresa("Tech Solutions", "contato@techsolutions.com", "MG", "30001-000", "Consultoria TI", ["Python", "Django"], "45.678.912/0001-00", "Brasil"),
        new Empresa("Cloud Experts", "contato@cloudexperts.com", "RS", "90001-000", "Serviços em nuvem", ["AWS", "Docker"], "32.165.498/0001-00", "Brasil"),
        new Empresa("Mobile Makers", "contato@mobilemakers.com", "BA", "40001-000", "Desenvolvimento Mobile", ["Kotlin", "Flutter"], "65.432.198/0001-00", "Brasil")
    ]

    static List<Curtida> curtidasCandidato = []
    static List<Curtida> curtidasEmpresa = []
    static List<Curtida> matches = []

    static void listarCandidatos() {
        println "\n--- Lista de Candidatos ---"
        candidatos.each { c ->
            println "Nome: ${c.nome}, Email: ${c.email}, CPF: ${c.cpf}, Idade: ${c.idade}, Estado: ${c.estado}, CEP: ${c.cep}, Descrição: ${c.descricao}, Competências: ${c.competencias.join(', ')}"
        }
    }

    static void listarEmpresas() {
        println "\n--- Lista de Empresas ---"
        empresas.each { e ->
            println "Nome: ${e.nome}, Email: ${e.email}, CNPJ: ${e.cnpj}, País: ${e.pais}, Estado: ${e.estado}, CEP: ${e.cep}, Descrição: ${e.descricao}, Competências: ${e.competencias.join(', ')}"
        }
    }

    static void candidatoCurteVaga() {
        println "\nEscolha o candidato pelo número:"
        candidatos.eachWithIndex { c, i -> println "${i+1} - ${c.nome}" }
        def idxCand = (System.console() ? System.console().readLine() : new Scanner(System.in).nextLine()).toInteger() - 1
        if (idxCand < 0 || idxCand >= candidatos.size()) { println "Candidato inválido!"; return }
        def candidato = candidatos[idxCand]

        println "Escolha a empresa pelo número:"
        empresas.eachWithIndex { e, i -> println "${i+1} - ${e.nome}" }
        def idxEmp = (System.console() ? System.console().readLine() : new Scanner(System.in).nextLine()).toInteger() - 1
        if (idxEmp < 0 || idxEmp >= empresas.size()) { println "Empresa inválida!"; return }
        def empresa = empresas[idxEmp]

        println "Escolha a competência/vaga para curtir:"
        empresa.competencias.eachWithIndex { comp, i -> println "${i+1} - ${comp}" }
        def idxComp = (System.console() ? System.console().readLine() : new Scanner(System.in).nextLine()).toInteger() - 1
        if (idxComp < 0 || idxComp >= empresa.competencias.size()) { println "Competência inválida!"; return }
        def competencia = empresa.competencias[idxComp]

        def curtida = new Curtida(candidato, empresa, competencia)
        curtidasCandidato << curtida
        println "Candidato ${candidato.nome} curtiu a vaga ${competencia} da empresa ${empresa.nome}."
    }

    static void empresaCurteCandidato() {
        println "\nEscolha a empresa pelo número:"
        empresas.eachWithIndex { e, i -> println "${i+1} - ${e.nome}" }
        def idxEmp = (System.console() ? System.console().readLine() : new Scanner(System.in).nextLine()).toInteger() - 1
        if (idxEmp < 0 || idxEmp >= empresas.size()) { println "Empresa inválida!"; return }
        def empresa = empresas[idxEmp]

        println "Escolha o candidato pelo número:"
        candidatos.eachWithIndex { c, i -> println "${i+1} - ${c.nome}" }
        def idxCand = (System.console() ? System.console().readLine() : new Scanner(System.in).nextLine()).toInteger() - 1
        if (idxCand < 0 || idxCand >= candidatos.size()) { println "Candidato inválido!"; return }
        def candidato = candidatos[idxCand]

        println "Escolha a competência/vaga para curtir:"
        candidato.competencias.eachWithIndex { comp, i -> println "${i+1} - ${comp}" }
        def idxComp = (System.console() ? System.console().readLine() : new Scanner(System.in).nextLine()).toInteger() - 1
        if (idxComp < 0 || idxComp >= candidato.competencias.size()) { println "Competência inválida!"; return }
        def competencia = candidato.competencias[idxComp]

        def curtida = new Curtida(candidato, empresa, competencia)
        curtidasEmpresa << curtida
        println "Empresa ${empresa.nome} curtiu o candidato ${candidato.nome} para a vaga ${competencia}."

        // Verifica se já existe curtida recíproca
        def match = curtidasCandidato.find { it.candidato == candidato && it.empresa == empresa && it.competencia == competencia }
        if (match && !matches.find { it.candidato == candidato && it.empresa == empresa && it.competencia == competencia }) {
            curtida.match = true
            match.match = true
            matches << curtida
            println "MATCH! Empresa ${empresa.nome} e candidato ${candidato.nome} para a vaga ${competencia}."
        }
    }

    static void listarMatches() {
        println "\n--- Matches ---"
        if (matches.isEmpty()) {
            println "Nenhum match encontrado ainda."
        } else {
            matches.each { m ->
                println "Empresa: ${m.empresa.nome}, Candidato: ${m.candidato.nome}, Vaga: ${m.competencia}"
            }
        }
    }

    static void main(String[] args) {
        while (true) {
            println "\nMenu Principal:\n1 - Listar Candidatos\n2 - Listar Empresas\n3 - Candidato curtir vaga de empresa\n4 - Empresa curtir candidato\n5 - Listar matches\n0 - Sair"
            def opcao = System.console() ? System.console().readLine() : new Scanner(System.in).nextLine()
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
