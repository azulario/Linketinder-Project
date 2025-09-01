// Autor: Azulario
// Para executar: gradle run ou groovy Main.groovy

import groovy.transform.Field

class Main {
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

    static void main(String[] args) {
        while (true) {
            println "\nMenu Principal:\n1 - Listar Candidatos\n2 - Listar Empresas\n0 - Sair"
            def opcao = System.console() ? System.console().readLine() : new Scanner(System.in).nextLine()
            switch(opcao) {
                case "1": listarCandidatos(); break
                case "2": listarEmpresas(); break
                case "0": println "Saindo..."; return
                default: println "Opção inválida!"
            }
        }
    }
}
